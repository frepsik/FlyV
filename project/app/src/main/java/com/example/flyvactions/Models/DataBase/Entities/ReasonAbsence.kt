package com.example.flyvactions.Models.DataBase.Entities

import kotlinx.serialization.Serializable

@Serializable
data class ReasonAbsence(
    val id : String,
    val reason : String
)
