package com.example.taskn17.authentfication

import com.example.taskn17.authentfication.login.api.LoginApi
import com.example.taskn17.authentfication.register.api.RegisterApi
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

object RetrofitClient {
    private const val BASE_URL = "https://reqres.in/api/"

    private val moshi: Moshi = Moshi.Builder().add(KotlinJsonAdapterFactory()).build()

    private val retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(MoshiConverterFactory.create(moshi))
        .build()

    fun loginService(): LoginApi = retrofit.create(LoginApi::class.java)
    fun registerService(): RegisterApi = retrofit.create(RegisterApi::class.java)
}