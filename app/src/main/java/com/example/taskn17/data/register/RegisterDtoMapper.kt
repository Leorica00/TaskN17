package com.example.taskn17.data.register

import com.example.taskn17.domain.register.RegisterResponse

fun RegisterDto.toDomain(): RegisterResponse {
    return RegisterResponse(
        id = id,
        token = token
    )
}