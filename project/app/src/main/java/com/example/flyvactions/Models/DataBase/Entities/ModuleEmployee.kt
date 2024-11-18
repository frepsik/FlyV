package com.example.flyvactions.Models.DataBase.Entities

import kotlinx.serialization.SerialName //Помогает правильно указать название, потому что в базе snake_cae, а Kotlin camelCase
import kotlinx.serialization.Serializable


@Serializable
data class ModuleEmployee(
    val id : String,
    @SerialName("module_id") val moduleId : String,
    @SerialName("employee_id") val employeeId : String
)
