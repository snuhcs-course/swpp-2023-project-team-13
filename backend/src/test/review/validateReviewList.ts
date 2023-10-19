import { validateDtoKeys } from '../utils';

export function validateReviewList(body: any) {
  validateDtoKeys(body, ['reviewList']);
}

function validateReview(body: any) {
  validateDtoKeys(body, [
    'id',
    'content',
    'images',
    'receiptImage',
    'issuedAt',
    'restaurant',
    'user',
  ]);

  validateRestaurant(body.restaurant);
  validateUserSummary(body.user);
  validateImages(body.images);
}

export function validateRestaurant(body: any) {
  validateDtoKeys(body, ['id', 'googleMapPlaceId', 'longitude', 'latitude']);
}

export function validateUserSummary(body: any) {
  validateDtoKeys(body, ['id', 'name']);
}

export function validateImages(body: any) {
  function validateImage(image: any) {
    validateDtoKeys(image, ['id', 'url', 'isReceipt', 'isReceiptVerified']);
  }

  for (const image of body) {
    validateImage(image);
  }
}
