package com.example.page.models

import com.google.gson.annotations.SerializedName

data class User(
    @SerializedName("__v")
    val version: Int,

    @SerializedName("_id")
    val id: String,

    val createdAt: String,
    val email: String,
    val password: String,
    val updatedAt: String,
    val username: String
)
