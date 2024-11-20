package com.example.flyvactions.Models.Cache

import com.example.flyvactions.Models.Cache.ProfileCache.UserProfile

/**
 * Метод для очистки данных по пользователю
 */
fun clearProfileCache(){
    ProfileCache.profile = UserProfile(
        userInfo = null,
        fullName = "",
        city = "",
        numberPhone = "",
        email = "",
        daysOff = 0,
        daysVacation = 0,
        daysVacationPlanned = null,
        daysVacationForExperience = 0,
        urlPhotoProfile = null,
        hireDate = null
    )
}