import traceback

import gluonnlp as nlp
import numpy as np
import pandas as pd
import torch
import torch.nn.functional as F
import torch.optim as optim
from keras.preprocessing.sequence import pad_sequences
from sklearn.model_selection import train_test_split
from torch import nn
from torch.utils.data import DataLoader, TensorDataset
from tqdm import tqdm, tqdm_notebook


def data_processing(raw_data):
    emotion_list = ["긍정", "부정"]  # 감정 라벨링 추가 가능

    for i in range(len(emotion_list)):
        raw_data.loc[raw_data.Emotion == emotion_list[i], "Emotion"] = i

    processed_data = pd.concat([raw_data["Sentence"], raw_data["Emotion"]], axis=1)

    processed_data.columns = ["sentence", "label"]

    return processed_data


def data_to_token_ids(tokenizer, single_sentence):
    special_token_added = "[CLS] " + str(single_sentence) + " [SEP]"

    tokenized_text = tokenizer.tokenize(special_token_added)

    token_ids = [tokenizer.convert_tokens_to_ids(tokenized_text)]

    MAX_LEN = 128
    token_ids_padded = pad_sequences(
        token_ids, maxlen=MAX_LEN, dtype="long", truncating="post", padding="post"
    )

    token_ids_flatten = token_ids_padded.flatten()
    return token_ids_flatten


def token_ids_to_mask(token_ids):
    mask = [float(i > 0) for i in token_ids]

    return mask


def tokenize_processed_data(tokenizer, processed_dataset):
    labels = processed_dataset["label"].to_numpy()

    labels = labels.astype(np.int)

    tokenized_data = [
        data_to_token_ids(tokenizer, processed_data)
        for processed_data in processed_dataset["sentence"]
    ]

    attention_masks = [token_ids_to_mask(token_ids) for token_ids in tokenized_data]

    return tokenized_data, labels, attention_masks


def split_into_train_validation(whole_data, whole_label, whole_masks):
    print("length of whole_data : " + str(len(whole_data)))

    train_inputs, validation_inputs, train_labels, validation_labels = train_test_split(
        whole_data, whole_label, random_state=2022, test_size=0.1
    )
    train_masks, validation_masks, _, _ = train_test_split(
        whole_masks, whole_data, random_state=2022, test_size=0.1
    )

    print("length of train_data : " + str(len(train_inputs)))

    return (
        train_inputs,
        validation_inputs,
        train_labels,
        validation_labels,
        train_masks,
        validation_masks,
    )


def data_to_tensor(inputs, labels, masks):
    inputs_tensor = torch.tensor(inputs)
    labels_tensor = torch.tensor(labels)
    masks_tensor = torch.tensor(masks)
    return inputs_tensor, labels_tensor, masks_tensor


def tensor_to_dataloader(inputs, labels, masks, mode):
    from torch.utils.data import RandomSampler, SequentialSampler

    batch_size = 32
    data = TensorDataset(inputs, masks, labels)

    if mode == "train":
        sampler = RandomSampler(data)
    else:
        sampler = SequentialSampler(data)

    dataloader = DataLoader(data, sampler=sampler, batch_size=batch_size)

    return dataloader


def preprocessing(tokenizer, whole_dataset):
    processed_dataset = data_processing(whole_dataset)

    tokenized_dataset, labels, attention_masks = tokenize_processed_data(
        tokenizer, processed_dataset
    )

    (
        train_inputs,
        validation_inputs,
        train_labels,
        validation_labels,
        train_masks,
        validation_masks,
    ) = split_into_train_validation(tokenized_dataset, labels, attention_masks)

    train_inputs, train_labels, train_masks = data_to_tensor(
        train_inputs, train_labels, train_masks
    )
    validation_inputs, validation_labels, validation_masks = data_to_tensor(
        validation_inputs, validation_labels, validation_masks
    )

    train_dataloader = tensor_to_dataloader(train_inputs, train_labels, train_masks, "train")
    validation_dataloader = tensor_to_dataloader(
        validation_inputs, validation_labels, validation_masks, "validation"
    )

    return train_dataloader, validation_dataloader


def main():
    from tokenization import KoBertTokenizer

    # whole_dataset =

    tokenizer = KoBertTokenizer.from_pretrained("monologg/kobert")

    # train, valid = preprocessing(tokenizer, whole_dataset)


if __name__ == "__main__":
    main()
