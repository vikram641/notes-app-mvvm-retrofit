package com.example.page.models

data class UserResponce(
    val success: Boolean,
    val token: String,
    val user: User
)

//data class BaseResponse<T>(
//    val status: Boolean,
//    val code: Int,
//    val message: String,
//    val data: T
//)

