package com.example.page.api

import com.example.page.models.NoteRequest
import com.example.page.models.NoteResponce
import com.example.page.models.NotesResponceGet
import com.example.page.models.UserRequest
import com.example.page.models.UserResponce
import com.example.page.models.updateProfileResponse
import com.example.page.models.UpdateUserdeatilsRequest
import com.example.page.models.UpdateUserdetailsResponse
import okhttp3.MultipartBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Part
import retrofit2.http.Path

interface API {
    //auth apis
    @POST("users/signup")
    suspend fun signup(@Body userRequest: UserRequest): Response<UserResponce>

    @POST("users/signin")
    suspend fun signin(@Body userRequest: UserRequest): Response<UserResponce>

    // notes apis
    @GET("/notes")
    suspend fun getNotes(): Response<NotesResponceGet>

    @POST("/notes")
    suspend fun createNote(@Body noteRequest: NoteRequest):Response<NoteResponce>

    @PUT("/notes/{noteId}")
    suspend fun updateNote(@Path("noteId") noteId:String, @Body noteRequest: NoteRequest): Response<NoteResponce>


    @DELETE("/notes/{noteId}")
    suspend fun deleteNote(@Path("noteId")noteId: String): Response<NoteResponce>

    @GET("users/user_detail")
    suspend fun getUserdetails(): Response<UserResponce>

    @Multipart
    @POST("/users/upload-profile")
    suspend fun updateProfile(@Part profile: MultipartBody.Part):Response<updateProfileResponse>

    @PUT("/users/update-user-profile")
    suspend fun updateUserdetails(@Body userdeatilsRequest: UpdateUserdeatilsRequest): Response<UpdateUserdetailsResponse>

}