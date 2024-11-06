package com.example.flyvactions.Models.DataBase.Entities

import kotlinx.serialization.SerialName //Помогает правильно указать название, потому что в базе snake_cae, а Kotlin camelCase
import kotlinx.serialization.Serializable

@Serializable
data class Substitution(
    val id : String,
    @SerialName("employee_first_id") val employeeFirstId : String,
    @SerialName("employee_second_id") val employeeSecondId: String
)
