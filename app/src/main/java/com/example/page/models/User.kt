package com.example.page.models

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class User(
    @SerializedName("__v")
    val version: Int,

    @SerializedName("_id")
    val id: String,

    val createdAt: String,
    val email: String,
    val password: String?,
    val updatedAt: String,
    val username: String,
    val img_url: String
):Parcelable
