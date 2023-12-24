package com.example.taskn17.di

import com.example.taskn17.data.login.LoginService
import com.example.taskn17.data.register.RegisterService
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {
    private const val BASE_URL = "https://reqres.in/api/"

    @Singleton
    @Provides
    fun provideRetrofitClient(): Retrofit {
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(
                MoshiConverterFactory.create(
                    Moshi.Builder().add(KotlinJsonAdapterFactory()).build()
                )
            )
            .build()
    }

    @Singleton
    @Provides
    fun loginService(retrofit: Retrofit): LoginService = retrofit.create(LoginService::class.java)

    @Singleton
    @Provides
    fun registerService(retrofit: Retrofit): RegisterService =
        retrofit.create(RegisterService::class.java)
}