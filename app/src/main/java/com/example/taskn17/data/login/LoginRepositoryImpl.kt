package com.example.taskn17.data.login

import com.example.taskn17.data.error.AppError
import com.example.taskn17.data.Resource
import com.example.taskn17.domain.login.LoginRepository
import com.example.taskn17.domain.login.LoginRequest
import com.example.taskn17.domain.login.LoginResponse
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.HttpException
import javax.inject.Inject

class LoginRepositoryImpl @Inject constructor(private val loginService: LoginService) : LoginRepository {
    override suspend fun login(email: String, password: String): Flow<Resource<LoginResponse>> {
        return flow {
            emit(Resource.Loading(loading = true))
            try {
                val response = loginService.login(LoginRequest(email, password))
                if (response.isSuccessful) {
                    emit(Resource.Success(response = response.body()!!.toDomain()))
                } else {
                    throw HttpException(response)
                }
            } catch (e: Exception) {
                val error: AppError = AppError.fromException(e)
                emit(Resource.Error(message = error.message))
            }
            emit(Resource.Loading(loading = false))
        }
    }
}