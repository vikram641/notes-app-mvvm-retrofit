package com.example.page.models

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class UserX(
    val __v: Int,
    val _id: String,
    val createdAt: String,
    val email: String,
    val img_url: String,
    val updatedAt: String,
    val username: String
): Parcelable