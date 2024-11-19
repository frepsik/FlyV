package com.example.flyvactions.Models.DataClasses

/**
 * Дата класс предназначенный для хранения экземпляра пользователя для календаря
 */
data class AbsenceEmployeeCalendar(
    val fullName : String,
    val urlPhotoProfile : String?,
    val reasonAbsence : String
)
