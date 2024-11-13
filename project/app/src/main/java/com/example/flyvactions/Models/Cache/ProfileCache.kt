package com.example.flyvactions.Models.Cache

import io.github.jan.supabase.gotrue.user.UserInfo

/**
 * Объект фиксирующий данные о пользователе на время использования им приложения
 */
object ProfileCache {
    var profile : UserProfile = UserProfile(
        userInfo = null,
        fullName = "",
        city = "",
        numberPhone = "",
        email = "",
        daysOff = 0,
        daysVacation = 0,
        daysVacationPlanned = null,
        daysVacationForExperience = 0
    )

    data class UserProfile(
        var userInfo: UserInfo? = null,
        var fullName : String,
        var city : String,
        var numberPhone : String,
        var email : String,
        var daysOff: Int,
        var daysVacation : Int,
        var daysVacationPlanned : Int?,
        var daysVacationForExperience : Int
    )
}