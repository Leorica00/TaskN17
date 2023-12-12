package com.example.taskn17.authentfication.register.api

import com.squareup.moshi.Json

data class RegisterResponse (
    @Json(name = "id") val id: Int,
    @Json(name = "token") val token: String
)