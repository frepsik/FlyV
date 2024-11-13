package com.example.flyvactions.ViewModels

import androidx.lifecycle.ViewModel
import com.example.flyvactions.Models.DataBase.Queries.Auth
import io.github.jan.supabase.gotrue.user.UserInfo

class MainViewModel : ViewModel() {
    var auth : Auth = Auth()
    val userInfo : UserInfo? = auth.authorizedUser()
}