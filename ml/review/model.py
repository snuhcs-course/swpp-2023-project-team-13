import torch
from torch import nn
from transformers import (
    BertConfig,
    BertForSequenceClassification,
    BertModel,
    get_linear_schedule_with_warmup,
)


class BertClassifier(nn.Module):
    def __init__(self, num_labels=7):
        super(BertClassifier, self).__init__()

        self.bert = BertModel.from_pretrained("monologg/kobert")
        self.linear = nn.Linear(768, num_labels)

    def forward(self, input_ids, attention_mask):
        _, pooled_output = self.bert(input_ids=input_ids, attention_mask=attention_mask)
        linear_output = self.linear(pooled_output)

        return linear_output


def BertModelInitialization():
    PATH = "model.pt"
    model = BertClassifier()

    torch.save(model.state_dict(), PATH)


def get_model(device, cuda_available):
    PATH = "model.pt"

    model = BertClassifier()

    if cuda_available:
        model.load_state_dict(torch.load(PATH), strict=False)

        model = model.to(device)
    else:
        model.load_state_dict(torch.load(PATH, map_location=device), strict=False)

    return model


def get_model_with_params(num_data, device, cuda_available):
    model = get_model(device, cuda_available)

    optimizer = torch.optim.AdamW(model.parameters(), lr=1e-5, eps=1e-8)
    epochs = 5

    total_steps = num_data * epochs

    scheduler = get_linear_schedule_with_warmup(
        optimizer, num_warmup_steps=0, num_training_steps=total_steps
    )
    criterion = nn.CrossEntropyLoss()

    return model, optimizer, scheduler, epochs, criterion


def main():
    device = torch.device("cuda" if torch.cuda.is_available() else "cpu")

    BertModelInitialization()
    print(get_model_with_params(34388, device, torch.cuda.is_available()))


if __name__ == "__main__":
    main()
