package com.example.page.models

data class NoteResponce(
    val message: String,
    val note: Note,
    val success: Boolean
)