package com.example.flyvactions.Models.WorkWithStringAndDate

import java.time.LocalDate
import java.util.Calendar

/**
 * Конвертер localDate в Calendar
 */
fun localDateToCalendar(date: LocalDate): Calendar {
    val calendar = Calendar.getInstance()
    calendar.set(date.year, date.monthValue - 1, date.dayOfMonth) // У месяца в calendar отсчёт идёт от 0
    return calendar
}