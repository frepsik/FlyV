package com.example.flyvactions.Models.DataBase.Entities

import kotlinx.serialization.Contextual
import kotlinx.serialization.SerialName //Помогает правильно указать название, потому что в базе snake_cae, а Kotlin camelCase
import kotlinx.serialization.Serializable



@Serializable
data class AbsenceEmployee(
    val id : String,
    @SerialName("reason_id") val reasonId : String,
    @SerialName("employee_id") val employeeId : String,
    @SerialName("begin_date") val beginDate : String,
    @SerialName("amount_day") var amountDay : Int
)
