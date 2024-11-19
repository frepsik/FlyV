package com.example.flyvactions.Models.WorkWithString

import androidx.compose.runtime.Composable

/**
 * Парсер для ФИО сотрудника компании (позволяет представить ФИО в формате Фамилия И.О.)
 */
fun parseFullNameEmployee(fullNameEmployee : String) : String{
    val partsFullName : List<String> = fullNameEmployee.split(" ")
    return " ${partsFullName[0]} ${partsFullName[1].take(1).uppercase()}.${partsFullName[2].take(1).uppercase()}"
}