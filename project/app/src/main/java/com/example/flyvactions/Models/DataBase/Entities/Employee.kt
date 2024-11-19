package com.example.flyvactions.Models.DataBase.Entities

import kotlinx.serialization.SerialName //Помогает правильно указать название, потому что в базе snake_cae, а Kotlin camelCase
import kotlinx.serialization.Serializable


@Serializable
data class Employee(
    val id : String,
    @SerialName("full_name") val fullName : String,
    @SerialName("city_id") var cityId : String,
    @SerialName("hire_date") val hireDate : String,
    @SerialName("days_off") var daysOff : Int,
    @SerialName("days_vacations") var daysVacation : Int,
    @SerialName("number_phone") var numberPhone : String,
    @SerialName("is_works") var isWorks : Boolean,
    @SerialName("email_personal") var email : String?,
    @SerialName("url_photo_profile") var urlPhotoProfile : String?
)
