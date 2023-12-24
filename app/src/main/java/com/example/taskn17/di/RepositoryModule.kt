package com.example.taskn17.di

import com.example.taskn17.data.login.LoginRepositoryImpl
import com.example.taskn17.data.login.LoginService
import com.example.taskn17.data.register.RegisterRepositoryImpl
import com.example.taskn17.data.register.RegisterService
import com.example.taskn17.domain.login.LoginRepository
import com.example.taskn17.domain.register.RegisterRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {

    @Singleton
    @Provides
    fun provideLoginRepository(loginService: LoginService): LoginRepository = LoginRepositoryImpl(loginService = loginService)

    @Singleton
    @Provides
    fun provideRegisterRepository(registerService: RegisterService): RegisterRepository = RegisterRepositoryImpl(registerService = registerService)

}