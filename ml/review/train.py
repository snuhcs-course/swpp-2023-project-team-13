import gluonnlp as nlp
import numpy as np
import pandas as pd
import torch
from kobert_tokenizer import KoBERTTokenizer
from preprocessing import preprocessing
from torch import nn
from tqdm.notebook import tqdm
from transformers import AdamW, BertModel
from transformers.optimization import get_cosine_schedule_with_warmup

from model import BERTClassifier, BERTDataset

data = pd.read_csv("review_data.csv")

device = torch.device("cuda" if torch.cuda.is_available() else "cpu")

PATH = "../model"


def calc_accuracy(X, Y):
    max_vals, max_indices = torch.max(X, 1)
    train_acc = (max_indices == Y).sum().data.cpu().numpy() / max_indices.size()[0]
    return train_acc


max_len = 64
batch_size = 64
warmup_ratio = 0.1
num_epochs = 5
max_grad_norm = 1
log_interval = 200
learning_rate = 5e-5

ko_tokenizer = KoBERTTokenizer.from_pretrained("skt/kobert-base-v1")
ko_bertmodel = BertModel.from_pretrained("skt/kobert-base-v1", return_dict=False)
ko_vocab = nlp.vocab.BERTVocab.from_sentencepiece(ko_tokenizer.vocab_file, padding_token="[PAD]")


def train(data):
    dataset_train, dataset_test = preprocessing(data)
    data_train = BERTDataset(dataset_train, 0, 1, ko_tokenizer, ko_vocab, max_len, True, False)
    data_test = BERTDataset(dataset_test, 0, 1, ko_tokenizer, ko_vocab, max_len, True, False)

    train_dataloader = torch.utils.data.DataLoader(
        data_train, batch_size=batch_size, num_workers=5
    )
    test_dataloader = torch.utils.data.DataLoader(data_test, batch_size=batch_size, num_workers=5)

    model = BERTClassifier(ko_bertmodel, dr_rate=0.5).to(device)
    no_decay = ["bias", "LayerNorm.weight"]
    optimizer_grouped_parameters = [
        {
            "params": [
                p for n, p in model.named_parameters() if not any(nd in n for nd in no_decay)
            ],
            "weight_decay": 0.01,
        },
        {
            "params": [p for n, p in model.named_parameters() if any(nd in n for nd in no_decay)],
            "weight_decay": 0.0,
        },
    ]

    optimizer = AdamW(optimizer_grouped_parameters, lr=learning_rate)
    loss_fn = nn.CrossEntropyLoss()

    t_total = len(train_dataloader) * num_epochs
    warmup_step = int(t_total * warmup_ratio)

    scheduler = get_cosine_schedule_with_warmup(
        optimizer, num_warmup_steps=warmup_step, num_training_steps=t_total
    )

    train_history = []
    test_history = []
    loss_history = []

    for e in range(num_epochs):
        train_acc = 0.0
        test_acc = 0.0
        model.train()
        for batch_id, (token_ids, valid_length, segment_ids, label) in enumerate(
            tqdm(train_dataloader)
        ):
            optimizer.zero_grad()
            token_ids = token_ids.long().to(device)
            segment_ids = segment_ids.long().to(device)
            valid_length = valid_length
            label = label.long().to(device)
            out = model(token_ids, valid_length, segment_ids)

            loss = loss_fn(out, label)
            loss.backward()
            torch.nn.utils.clip_grad_norm_(model.parameters(), max_grad_norm)
            optimizer.step()
            scheduler.step()  # Update learning rate schedule
            train_acc += calc_accuracy(out, label)
            if batch_id % log_interval == 0:
                print(
                    "epoch {} batch id {} loss {} train acc {}".format(
                        e + 1, batch_id + 1, loss.data.cpu().numpy(), train_acc / (batch_id + 1)
                    )
                )
                train_history.append(train_acc / (batch_id + 1))
                loss_history.append(loss.data.cpu().numpy())
        print("epoch {} train acc {}".format(e + 1, train_acc / (batch_id + 1)))
        model.eval()
        for batch_id, (token_ids, valid_length, segment_ids, label) in enumerate(
            tqdm(test_dataloader)
        ):
            token_ids = token_ids.long().to(device)
            segment_ids = segment_ids.long().to(device)
            valid_length = valid_length
            label = label.long().to(device)
            out = model(token_ids, valid_length, segment_ids)
            test_acc += calc_accuracy(out, label)
        print("epoch {} test acc {}".format(e + 1, test_acc / (batch_id + 1)))
        test_history.append(test_acc / (batch_id + 1))
    torch.save(model, PATH)
