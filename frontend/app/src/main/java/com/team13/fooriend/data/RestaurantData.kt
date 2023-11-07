package com.team13.fooriend.data

data class Restaurant(
    val id: Int,
    val name: String,
    val longitude: Double,
    val latitude: Double,
    val good: Int,
    val bad: Int,
    val reviewList: List<Int>,
    val reviewCount: Int,
)