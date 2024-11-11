package com.example.flyvactions.ViewModels

import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.flyvactions.Models.BaseState
import com.example.flyvactions.Models.DataBase.Queries.Get
import kotlinx.coroutines.launch

//Viewmodel окна LoginView
class LoginViewModel:ViewModel() {
    //Свойства для полей ввода почты и пароля
    var emailUser by mutableStateOf("")
    var passwordUser by mutableStateOf("")

    var get : Get = Get()


    //Можно изменять по .value, потому что тип MutableState (этот тип относится к изменяемым объектам (int, string, List - нет))
    //По сути val означает, что я не могу изменить ссылку на объект, но сам объект могу изменить
    private val _userState = mutableStateOf<BaseState>(BaseState.Loading)
    val userState: State<BaseState> = _userState

    fun onSignInEmailPassword(){
        viewModelScope.launch {
            try {
                _userState.value = BaseState.Loading
                get.authorization(emailUser,passwordUser)

                _userState.value = BaseState.Success("SuccessSignIn")
                Log.d("SignIn","${_userState.value}")
            }
            catch (e: Exception){
                _userState.value = BaseState.Error("ErrorSignIn: ${e}")
                Log.d("SignIn", "${ _userState.value}")
            }

        }
    }
}