package com.team13.fooriend.data

data class Review(
    val id: Int,
    val restaurantId: Int,
    val writerId: Int,
    val title: String,
    val content: String,
    val image: List<Int>,
    val confirm: Boolean,
)