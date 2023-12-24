package com.example.taskn17.data.register

import com.example.taskn17.data.error.AppError
import com.example.taskn17.data.Resource
import com.example.taskn17.domain.register.RegisterRepository
import com.example.taskn17.domain.register.RegisterResponse
import com.example.taskn17.domain.register.RegisterRequest
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.HttpException
import javax.inject.Inject

class RegisterRepositoryImpl @Inject constructor(private val registerService: RegisterService): RegisterRepository {
    override suspend fun register(email: String, password: String): Flow<Resource<RegisterResponse>> {
        return flow {
            emit(Resource.Loading(loading = true))
            try {
                val response = registerService.register(RegisterRequest(email, password))
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