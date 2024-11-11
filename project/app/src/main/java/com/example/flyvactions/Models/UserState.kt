package com.example.flyvactions.Models

//Состояние
sealed class BaseState {
    object Loading: BaseState()
    data class Success(val message: String): BaseState()
    data class Error(val message: String): BaseState()
}