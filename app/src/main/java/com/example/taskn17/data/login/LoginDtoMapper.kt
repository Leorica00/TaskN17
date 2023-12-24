package com.example.taskn17.data.login

import com.example.taskn17.domain.login.LoginResponse

fun LoginDto.toDomain(): LoginResponse {
    return LoginResponse(token = token)
}