package com.example.page.models

import android.os.Parcelable
import kotlinx.parcelize.Parcelize


@Parcelize
data class Note(
    val __v: Int,
    val _id: String,
    val content: String,
    val createdAt: String,
    val title: String,
    val updatedAt: String,
    val userId: String
) : Parcelable