from pydantic import BaseModel


class OcrModel(BaseModel):
  image_url: str

class ReviewModel(BaseModel):
  review: str
