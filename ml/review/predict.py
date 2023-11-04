import gluonnlp as nlp
import numpy as np
import torch
from kobert_tokenizer import KoBERTTokenizer

from review.model import BERTDataset

device = torch.device("cuda" if torch.cuda.is_available() else "cpu")
PATH = ""


def predict(predict_sentence):
    max_len = 64
    batch_size = 64
    data = [predict_sentence, "0"]
    dataset_another = [data]
    ko_tokenizer = KoBERTTokenizer.from_pretrained("skt/kobert-base-v1")
    ko_vocab = nlp.vocab.BERTVocab.from_sentencepiece(
        ko_tokenizer.vocab_file, padding_token="[PAD]"
    )

    another_test = BERTDataset(dataset_another, 0, 1, ko_tokenizer, ko_vocab, max_len, True, False)
    test_dataloader = torch.utils.data.DataLoader(
        another_test, batch_size=batch_size, num_workers=5
    )
    model = torch.load(PATH)
    model.eval()

    for batch_id, (token_ids, valid_length, segment_ids, label) in enumerate(test_dataloader):
        token_ids = token_ids.long().to(device)
        segment_ids = segment_ids.long().to(device)

        valid_length = valid_length
        label = label.long().to(device)

        out = model(token_ids, valid_length, segment_ids)

        test_eval = []
        for i in out:
            logits = i
            logits = logits.detach().cpu().numpy()

            if np.argmax(logits) == 0:
                test_eval.append("부정")
            elif np.argmax(logits) == 1:
                test_eval.append("긍정")

        return ">> 입력하신 문장은 " + test_eval[0] + "적 리뷰입니다."
