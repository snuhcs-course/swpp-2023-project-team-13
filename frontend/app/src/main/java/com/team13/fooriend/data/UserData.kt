package com.team13.fooriend.data

data class User(
    val id: Int,
    val userID: String,
    val name: String,
//    val email: String,
    val password: String,
){
    fun doesMatchSearchQuery(query: String): Boolean {
        return name.contains(query, ignoreCase = true)
    }
}