import import_ipynb
import torch
from model import get_model
from preprocessing import data_to_token_ids, token_ids_to_mask
from tokenization import KoBertTokenizer

device = torch.device("cuda" if torch.cuda.is_available() else "cpu")

model = get_model(device, torch.cuda.is_available())


def predict_sentence(input_str):
    tokenizer = KoBertTokenizer.from_pretrained("monologg/kobert")
    tokenized_data = [data_to_token_ids(tokenizer, input_str)]
    attention_masks = [token_ids_to_mask(token_ids) for token_ids in tokenized_data]

    inputs_tensor = torch.tensor(tokenized_data).to(device)
    masks_tensor = torch.tensor(attention_masks).to(device)

    outputs = model(inputs_tensor, attention_mask=masks_tensor)

    softmax = torch.nn.Softmax()
    output = softmax(outputs)

    return output


if __name__ == "__main__":
    while True:
        input_str = input()
        if input_str == "q":
            break

        tokenizer = KoBertTokenizer.from_pretrained("monologg/kobert")
        tokenized_data = [data_to_token_ids(tokenizer, input_str)]
        attention_masks = [token_ids_to_mask(token_ids) for token_ids in tokenized_data]

        inputs_tensor = torch.tensor(tokenized_data).to(device)
        masks_tensor = torch.tensor(attention_masks).to(device)

        outputs = model(inputs_tensor, attention_mask=masks_tensor)

        softmax = torch.nn.Softmax()
        output = softmax(outputs)
        print(output)
