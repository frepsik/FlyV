package com.example.flyvactions.Views.Calendars

import android.content.Context
import android.content.Intent
import android.provider.CalendarContract
import android.util.Log
import android.widget.Toast
import java.util.Calendar

/**
 * Функция для открытия календаря на телефоне, для того, чтобы создать событие об отпуске
 */
fun openCalendarEvent(context: Context, firstDateEvent: Calendar, lastDateEvent: Calendar) {

    //Устанавливаем время 12 часов ночи для первого дня события
    firstDateEvent.set(Calendar.HOUR_OF_DAY, 0)
    firstDateEvent.set(Calendar.MINUTE, 0)
    firstDateEvent.set(Calendar.SECOND, 0)
    firstDateEvent.set(Calendar.MILLISECOND, 0)

    //Устанавливаем время без одной минуты 12 часов для последнего дня события
    lastDateEvent.set(Calendar.HOUR_OF_DAY, 23)
    lastDateEvent.set(Calendar.MINUTE, 59)
    lastDateEvent.set(Calendar.SECOND, 59)
    lastDateEvent.set(Calendar.MILLISECOND, 0)

    val intent = Intent(Intent.ACTION_INSERT).apply {
        type = "vnd.android.cursor.item/event"
        putExtra(CalendarContract.Events.TITLE, "Отпуск")  // Название события

        putExtra(CalendarContract.EXTRA_EVENT_BEGIN_TIME, firstDateEvent.timeInMillis)  // Время начала отпуска
        putExtra(CalendarContract.EXTRA_EVENT_END_TIME, lastDateEvent.timeInMillis)  // Время окончания отпуска

        putExtra(CalendarContract.Events.DESCRIPTION, "Описание события: отпуск")  // Описание
    }

    try {
        context.startActivity(intent)
    }
    catch (e: Exception) {
        Toast.makeText(context, "Не удалось открыть календарь", Toast.LENGTH_SHORT).show()
        Log.d("ExceptionOpenCalendarEvent", "$e")
    }
}
