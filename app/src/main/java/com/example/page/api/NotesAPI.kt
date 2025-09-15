package com.example.page.api

import com.example.page.models.Note
import com.example.page.models.NoteRequest
import com.example.page.models.NoteResponce
import com.example.page.models.NotesResponceGet
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface NotesAPI {

    @GET("/notes")
    suspend fun getNotes(): Response<NotesResponceGet>

    @POST("/notes")
    suspend fun createNote(@Body noteRequest: NoteRequest):Response<NoteResponce>

    @PUT("/notes/{noteId}")
    suspend fun updateNote(@Path("noteId") noteId:String,@Body noteRequest: NoteRequest): Response<NoteResponce>


    @DELETE("/notes/{noteId}")
    suspend fun deleteNote(@Path("noteId")noteId: String): Response<NoteResponce>



}