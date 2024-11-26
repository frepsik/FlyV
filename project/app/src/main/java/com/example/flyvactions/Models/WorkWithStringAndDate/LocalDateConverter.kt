package com.example.flyvactions.Models.WorkWithStringAndDate


import java.time.LocalDate
import java.time.format.DateTimeFormatter

/**
 * Преобразование типа String к типу LocalDate
 */
fun convertStringToLocalDate(date : String) : LocalDate {
    return LocalDate.parse(date, DateTimeFormatter.ISO_LOCAL_DATE)
}
