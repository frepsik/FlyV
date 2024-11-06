package com.example.flyvactions.Models.DataBase.Entities

import kotlinx.serialization.Contextual //Помогает сериализовать и десериализовать в тип даты, а то котлиновский не имеет типа даты никакого
import kotlinx.serialization.SerialName //Помогает правильно указать название, потому что в базе snake_cae, а Kotlin camelCase
import kotlinx.serialization.Serializable
import java.time.LocalDate


@Serializable
data class Module(
    val id : String,
    val module : String,
    @SerialName("begin_date") @Contextual val beginDate : LocalDate,
    @SerialName("end_date") @Contextual val endDate : LocalDate,
    @SerialName("need_amount_employees") val needAmountEmployees : Int
)
