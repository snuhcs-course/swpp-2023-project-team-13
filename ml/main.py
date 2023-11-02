from io import BytesIO

import requests
import uuid
from PIL import Image
from fastapi import FastAPI

app = FastAPI()


@app.get("/")
async def root():
  return {"message": "Hello World"}


@app.get("/ocr")
async def receipt_ocr(url_uuid: str):
  url = "https://d2ghbk063u2bdn.cloudfront.net/" + url_uuid
  print(url)

  image_res = requests.get(url)
  image = Image.open(BytesIO(image_res.content))
  jpg_image = image.convert('RGB')
  uuid_string = uuid.uuid4()
  jpg_image.save('pictures/' + str(uuid_string) + '.jpg')

  return {"ocr": "통닭치킨"}


@app.get("/review")
async def review_classification(review_str: str):
  # res = predict_sentence(review_str)
  # return {"output": res}
  pass


