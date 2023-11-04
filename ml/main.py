from io import BytesIO

import requests
import uuid
from PIL import Image
from fastapi import FastAPI

from dto import OcrModel, ReviewModel
from review.predict import predict

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
  jpg_image.save('pictures/' + str(uuid_string) + '.jpg')

  return {"ocr": "통닭치킨"}


@app.post("/review")
async def review_classification(data: ReviewModel):
  return predict(data.review)


