package com.example.flyvactions.Models


import java.time.LocalDate
import java.time.format.DateTimeFormatter

//Преобразование типа String к типу LocalDate
fun ConvertStringToLocalDate(date : String) : LocalDate {
    return LocalDate.parse(date, DateTimeFormatter.ISO_LOCAL_DATE)
}
