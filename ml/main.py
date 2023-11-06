from io import BytesIO

import requests
import uuid
from PIL import Image
from fastapi import FastAPI

from dto import OcrModel, ReviewModel
from ocr.execution import ocr_receipt
from review.predict import predict_review

app = FastAPI()


@app.get("/")
async def root():
  return {"message": "Hello World"}


@app.post("/ocr")
async def receipt_ocr(data: OcrModel):
  image_res = requests.get(data.image_url)
  image = Image.open(BytesIO(image_res.content))
  jpg_image = image.convert('RGB')
  uuid_string = uuid.uuid4()
  receipt_path = 'pictures/' + str(uuid_string) + '.jpg'
  jpg_image.save(receipt_path)
  response = ocr_receipt(receipt_path)

  return response


@app.post("/review")
async def review_classification(data: ReviewModel):
  return predict_review(data.review)


