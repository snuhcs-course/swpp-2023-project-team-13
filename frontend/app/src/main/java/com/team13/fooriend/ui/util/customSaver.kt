package com.team13.fooriend.ui.util

import androidx.compose.runtime.saveable.Saver
import androidx.compose.runtime.saveable.listSaver

val ReviewsListSaver: Saver<List<Review>, *> = listSaver(
    save = { reviews ->
        reviews.map{ review->
            listOf(
                review.id.toString(),
                review.content,
                // Serialize the list of images
                review.images.joinToString(separator = "@@@") { image ->
                    "${image.id},${image.url},${image.isReceipt},${image.isReceiptVerified}"
                },
                // Serialize the receiptImage if it exists
                review.receiptImage?.let { "${it.id},${it.url},${it.isReceipt},${it.isReceiptVerified}" },
                review.issuedAt,
                // Serialize the restaurant object
                "${review.restaurant.id},${review.restaurant.googleMapPlaceId},${review.restaurant.longitude},${review.restaurant.latitude},${review.restaurant.name}",
                review.isPositive.toString(),
                review.user.let { user ->
                    listOf(
                        user.id.toString(),
                        user.name,
                    ).joinToString(separator = ",")
                }
            ).joinToString(separator = "|")
        }
    },
    restore = { list ->
        list.map{ serializedReview->
            val reviewParts = serializedReview.split('|')
            Review(
                id = reviewParts[0].toInt(),
                content = reviewParts[1] as String,
                images = (reviewParts[2] as String).split("@@@").map { img ->
                    img.split(',').let {
                        Image(
                            id = it[0].toInt(),
                            url = it[1],
                            isReceipt = it[2].toBoolean(),
                            isReceiptVerified = it[3].toBoolean()
                        )
                    }
                },
                receiptImage = (reviewParts[3] as String?)?.split(',')?.let {
                    if(it[0] == "null") null
                    else Image(
                        id = it[0].toInt(),
                        url = it[1],
                        isReceipt = it[2].toBoolean(),
                        isReceiptVerified = it[3].toBoolean()
                    )
                },
                issuedAt = reviewParts[4] as String,
                restaurant = (reviewParts[5] as String).split(',').let { res ->
                    Restaurant(
                        id = res[0].toInt(),
                        googleMapPlaceId = res[1],
                        longitude = res[2].toDouble(),
                        latitude = res[3].toDouble(),
                        name = res[4]
                    )
                },
                isPositive = (reviewParts[6] as String).compareTo("true") == 0,
                user = (reviewParts[7] as String).split(',').let { userParts ->
                    AbstractUser(
                        id = userParts[0].toInt(),
                        name = userParts[1],
                    )
                }
            )
        }
    }
)