package com.example.taskn17.splash

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.taskn17.authentfication.DataStoreManager
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.launch

class SplashViewModel: ViewModel() {
    private val _sessionFlow = MutableSharedFlow<Boolean>()
    var sessionFlow: SharedFlow<Boolean> = _sessionFlow
    fun readSession() {
        viewModelScope.launch {
            DataStoreManager.isSessionActive().collect {
                delay(2000)
                _sessionFlow.emit(it)
            }
        }
    }
}