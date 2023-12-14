package com.example.taskn17.authentfication.login

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class User(
    @Json(name = "token") val token: String,
    @Json(name = "email") val email: String,
    @Json(name = "password") val password: String
)