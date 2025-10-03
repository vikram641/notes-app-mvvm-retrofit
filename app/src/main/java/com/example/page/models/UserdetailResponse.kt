package com.example.page.models

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class UserdetailResponse(
    val success: Boolean,
    val user: UserX
):Parcelable