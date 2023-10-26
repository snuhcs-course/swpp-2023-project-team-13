import random
import time

import numpy as np
import pandas as pd
import torch
from model import BertModelInitialization, get_model, get_model_with_params
from preprocessing import preprocessing
from tqdm.notebook import tqdm


def accuracy(preds, labels):
    f_pred = np.argmax(preds, axis=1).flatten()
    f_labels = labels.flatten()
    return np.sum(f_pred == f_labels) / len(f_labels)


seed_val = 2023

random.seed(seed_val)
np.random.seed(seed_val)
torch.manual_seed(seed_val)
torch.cuda.manual_seed_all(seed_val)

from tokenization import KoBertTokenizer

# whole_dataset =

tokenizer = KoBertTokenizer.from_pretrained("monologg/kobert")

train_dataloader, validation_dataloader = preprocessing(tokenizer, whole_dataset)

# 초기 사용시에만 선언
BertModelInitialization()

device = torch.device("cuda" if torch.cuda.is_available() else "cpu")

model, optimizer, scheduler, epochs, criterion = get_model_with_params(
    len(train_dataloader), device, torch.cuda.is_available()
)

model.zero_grad()

for epoch_i in range(epochs):
    print("")
    print("========{:}번째 Epoch / 전체 {:}회 ========".format(epoch_i + 1, epochs))
    print("훈련 중")

    t0 = time.time()
    total_loss = 0
    sum_loss = 0
    model.train()  # 훈련모드

    for step, batch in enumerate(tqdm(train_dataloader)):
        if step % 50 == 0:
            print("{}번째 까지의 평균 loss : {}".format(step, sum_loss / 50))
            sum_loss = 0

        batch = tuple(t.to(device) for t in batch)
        b_input_ids, b_input_mask, b_labels = batch

        outputs = model(b_input_ids, b_input_mask)

        loss = criterion(outputs, b_labels.long())
        total_loss += loss.item()
        sum_loss += loss.item()

        loss.backward()
        torch.nn.utils.clip_grad_norm_(model.parameters(), 1.0)
        optimizer.step()
        scheduler.step()
        model.zero_grad()

    avg_train_loss = total_loss / len(train_dataloader)
    print("")
    print("  Average training loss: {0:.2f}".format(avg_train_loss))

    print("검증 중")

    t0 = time.time()
    model.eval()

    eval_loss, eval_accuracy = 0, 0
    nb_eval_steps, nb_eval_examples = 0, 0

    for batch in validation_dataloader:
        batch = tuple(t.to(device) for t in batch)

        b_input_ids, b_input_mask, b_labels = batch

        with torch.no_grad():
            outputs = model(b_input_ids, attention_mask=b_input_mask)

        outputs = outputs.detach().cpu().numpy()
        label_ids = b_labels.to("cpu").numpy()

        tmp_eval_accuracy = accuracy(outputs, label_ids)
        eval_accuracy += tmp_eval_accuracy
        nb_eval_steps += 1

    print("  Accuracy: {0:.4f}".format(eval_accuracy / nb_eval_steps))

PATH = "model.pt"
torch.save(model.state_dict(), PATH)

print("Training complete")
