import { validateDtoKeys } from '../utils';

export function validateReviewList(body: any) {
  validateDtoKeys(body, ['reviewList']);
  for (const review of body.reviewList) {
    validateReview(review);
  }
}

export function validateReview(body: any) {
  validateDtoKeys(body, [
    'id',
    'content',
    'images',
    'receiptImage',
    'issuedAt',
    'restaurant',
    'user',
    'isPositive',
  ]);

  validateRestaurant(body.restaurant);
  validateUserSummary(body.user);
  validateImages(body.images);
}

export function validateRestaurantList(body: any) {
  validateDtoKeys(body, ['restaurantList']);
  for (const restaurant of body.restaurantList) {
    validateRestaurant(restaurant);
  }
}

export function validateRestaurant(body: any) {
  validateDtoKeys(body, [
    'id',
    'googleMapPlaceId',
    'longitude',
    'latitude',
    'name',
  ]);
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
