package com.example.page.api

import com.example.page.models.UserRequest
import com.example.page.models.UserResponce
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface UserAPI {

    @POST("users/signup")
    suspend fun signup(@Body userRequest: UserRequest): Response<UserResponce>

    @POST("users/signin")
    suspend fun signin(@Body userRequest: UserRequest): Response<UserResponce>
}