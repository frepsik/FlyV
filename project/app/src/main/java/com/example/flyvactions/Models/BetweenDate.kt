package com.example.flyvactions.Models

import java.time.LocalDate

/**
 * Функция предназначенная для определения, того, находится ли специальная дата в диапазоне дат
 */
fun dateBetweenBeginDateAndEndDate(needDate : LocalDate, beginDate : LocalDate, endDate : LocalDate) : Boolean{
   return needDate in beginDate..endDate
}