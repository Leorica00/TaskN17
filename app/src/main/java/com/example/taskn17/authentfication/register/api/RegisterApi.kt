package com.example.taskn17.authentfication.register.api

import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface RegisterApi {
    @POST("register")
    suspend fun register(@Body registerRequest: RegisterRequest): Response<RegisterResponse>
}