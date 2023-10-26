from io import BytesIO

import requests
from PIL.Image import Image
from fastapi import FastAPI
from review.execution import predict_sentence

app = FastAPI()


@app.get("/")
async def root():
  return {"message": "Hello World"}


@app.get("/ocr")
async def receipt_ocr(url: str):
  image_res = requests.get(url)
  image = Image.open(BytesIO(image_res.content))
  jpg_image = image.convert('RGB')

  return {"ocr": "통닭치킨"}


@app.get("/review")
async def review_classification(review_str: str):
  res = predict_sentence(review_str)
  return {"output": res}


