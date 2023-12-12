package com.example.taskn17.authentfication.login.api

import com.squareup.moshi.Json

data class LoginResponse(
    @Json(name = "token") val token: String
)