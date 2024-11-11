package com.example.flyvactions.Models.DataBase.Entities


import kotlinx.serialization.SerialName //Помогает правильно указать название, потому что в базе snake_cae, а Kotlin camelCase
import kotlinx.serialization.Serializable


@Serializable
data class Module(
    val id : String,
    val module : String,
    @SerialName("begin_date") val beginDate : String,
    @SerialName("end_date")  val endDate : String,
    @SerialName("need_amount_employees") val needAmountEmployees : Int
)
