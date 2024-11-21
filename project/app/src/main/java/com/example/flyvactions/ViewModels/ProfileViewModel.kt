package com.example.flyvactions.ViewModels

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.example.flyvactions.Models.Cache.ProfileCache

/**
 * Бизнес-логика профиля сотрудника (ProfileView)
 */
class ProfileViewModel : ViewModel() {

    var urlProfileImage by mutableStateOf(ProfileCache.profile.urlPhotoProfile)
    var nameUser by mutableStateOf(ProfileCache.profile.fullName)
    var numberPhone by mutableStateOf(ProfileCache.profile.numberPhone)
    var email by mutableStateOf(ProfileCache.profile.email)
    var city by mutableStateOf(ProfileCache.profile.city)

    var isEnabledBack by mutableStateOf(false)
    var isEnabledExit by mutableStateOf(false)


    /**
     * Обновление данных если они изменились
     */
    fun refreshData(){
        email = ProfileCache.profile.email
        numberPhone = ProfileCache.profile.numberPhone
        city = ProfileCache.profile.city
    }
}