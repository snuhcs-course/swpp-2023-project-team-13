import pandas as pd
from sklearn.model_selection import train_test_split


def preprcessing(data):
    data_list = []

    for q, label in zip(data["review"], data["y"]):
        data = []
        data.append(q)
        data.append(str(label))

        data_list.append(data)

    dataset_train, dataset_test = train_test_split(
        data_list, test_size=0.2, shuffle=True, random_state=23
    )
    return dataset_train, dataset_test
