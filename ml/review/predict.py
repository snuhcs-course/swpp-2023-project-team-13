import torch
from torch import nn
from transformers import AutoTokenizer, BertModel

tokenizer = AutoTokenizer.from_pretrained("WhitePeak/bert-base-cased-Korean-sentiment")

device = "cuda" if torch.cuda.is_available() else "cpu"
model = torch.load("model.pt", map_location=device)


class CustomModel(nn.Module):
    def __init__(self, num_classes=2):
        super(CustomModel, self).__init__()
        self.bert = BertModel.from_pretrained("bert-base-uncased")
        self.dropout = nn.Dropout(0.1)
        self.classifier = nn.Linear(768, num_classes)

    def forward(self, input_ids, attention_mask):
        outputs = self.bert(input_ids=input_ids, attention_mask=attention_mask)
        pooled_output = outputs.pooler_output
        pooled_output = self.dropout(pooled_output)
        logits = self.classifier(pooled_output)
        return logits


def predict_review(sentence):
    inputs = tokenizer(sentence, return_tensors="pt")

    outputs = model(**inputs)

    probabilities = torch.nn.functional.softmax(outputs.logits, dim=1)

    predicted_class = torch.argmax(probabilities, dim=1).item()

    result = []

    if predicted_class == 1:
        result.append("긍정")
    else:
        result.append("부정")

    return result[0]
