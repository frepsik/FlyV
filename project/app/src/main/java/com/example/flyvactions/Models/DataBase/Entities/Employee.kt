package com.example.flyvactions.Models.DataBase.Entities

import kotlinx.serialization.Contextual //Помогает сериализовать и десериализовать в тип даты, а то котлиновский не имеет типа даты никакого
import kotlinx.serialization.SerialName //Помогает правильно указать название, потому что в базе snake_cae, а Kotlin camelCase
import kotlinx.serialization.Serializable
import java.time.LocalDate

@Serializable
data class Employee(
    val id : String,
    @SerialName("full_name") val fullName : String,
    @SerialName("city_id") var cityId : String,
    @SerialName("hire_date") @Contextual val hireDate : LocalDate,
    @SerialName("days_off") var daysOff : Int,
    @SerialName("days_vacation") var daysVacation : Int,
    @SerialName("number_phone") var numberPhone : String,
    @SerialName("is_works") var isWorks : Boolean,
    @SerialName("email_personal") var email : String
)
