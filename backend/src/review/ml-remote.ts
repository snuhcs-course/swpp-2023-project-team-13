import axios from 'axios';

export async function getReceiptOcr(imageUrl: string) {
  return (
    await axios.post(`${process.env.ML_URL}ocr`, {
      image_url: imageUrl,
    })
  ).data;
}

export async function getReviewIsPositive(review: string) {
  return (
    (
      await axios.post(`${process.env.ML_URL}review`, {
        review,
      })
    ).data['result'] === '긍정'
  );
}
