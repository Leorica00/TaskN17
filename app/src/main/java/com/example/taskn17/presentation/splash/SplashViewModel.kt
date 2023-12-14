package com.example.taskn17.presentation.splash

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.taskn17.data.data_store.UserDataPreferencesRepositoryImpl
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SplashViewModel @Inject constructor(private val userDataPreferencesRepositoryImpl: UserDataPreferencesRepositoryImpl) :
    ViewModel() {
    private val _sessionFlow = MutableStateFlow<DataStoreState>(DataStoreState.Pending)
    val sessionFlow: StateFlow<DataStoreState> get() = _sessionFlow

    fun readSession() {
        viewModelScope.launch {
            userDataPreferencesRepositoryImpl.isSessionActive().collect {
                delay(2000)
                _sessionFlow.value = DataStoreState.State(it)
            }
        }
    }
}

sealed class DataStoreState {
    data object Pending : DataStoreState()
    data class State(val boolean: Boolean) : DataStoreState()
}