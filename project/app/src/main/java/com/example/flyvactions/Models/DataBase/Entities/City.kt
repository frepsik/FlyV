package com.example.flyvactions.Models.DataBase.Entities

import kotlinx.serialization.Serializable

@Serializable
data class City(
    val id : String,
    val city : String
)
