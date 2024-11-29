package com.example.flyvactions

import android.icu.util.Calendar
import com.example.flyvactions.Models.Cache.ProfileCache
import com.example.flyvactions.Models.WorkWithStringAndDate.convertStringToLocalDate
import com.example.flyvactions.Models.WorkWithStringAndDate.localDateToCalendar
import com.example.flyvactions.Models.dateBetweenBeginDateAndEndDate
import com.example.flyvactions.ViewModels.ProfileViewModels.ProfileViewModel
import org.junit.Test
import org.junit.Assert.*
import java.time.LocalDate

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class ExampleUnitTest {

    /**
     * Тест проверящий корректность работы конвертера String в LocalDate
     */
    @Test
    fun convertStringToLocalDateIsSuccess(){
        val expected = LocalDate.of(2024,11,29)

        val stringDate = "2024-11-29"
        val result = convertStringToLocalDate(stringDate)

        assertEquals(expected, result)
    }

    /**
     * Тест проверяющий корректность работы конвертеа LocalDate в Calendar
     */
    @Test
    fun convertLocalDateToCalendarIsSuccess(){
        val expectedYear = 2023
        val expectedMonth = Calendar.AUGUST //Число 7
        val expectedDay = 20

        val date : LocalDate = LocalDate.of(2023,8,20)

        val calendar = localDateToCalendar(date)

        assertEquals(expectedYear, calendar.get(Calendar.YEAR))
        assertEquals(expectedMonth, calendar.get(Calendar.MONTH))
        assertEquals(expectedDay, calendar.get(Calendar.DAY_OF_MONTH))
    }

    /**
     * Тест проверяющий корректность определения того, что находится дата в заданном диапазоне дат
     */
    @Test
    fun dateBetweenBeginDateAndEndDateIsSuccess(){
        val expected = false

        val needDate = LocalDate.of(2024,11,29)
        val beginDateRange = LocalDate.of(2024,11,30)
        val endDateRange = LocalDate.of(2077, 10, 17)

        val result = dateBetweenBeginDateAndEndDate(needDate, beginDateRange, endDateRange)

        assertEquals(expected, result)
    }

    /**
     * Тест на проверку правильности обновления пользовательских данных
     */
    @Test
    fun refreshProfileDataIsSuccess(){
        //Устанавливаем проверочные данные
        ProfileCache.profile.email = ""
        ProfileCache.profile.numberPhone = ""
        ProfileCache.profile.city = ""

        val viewModel = ProfileViewModel()

        //Устанавливаем изменённые данные
        ProfileCache.profile.email = "kto@mail.ru"
        ProfileCache.profile.numberPhone = "+79345672319"
        ProfileCache.profile.city = "Владик"

        val expectedEmail= ProfileCache.profile.email
        val expectedNumberPhone = ProfileCache.profile.numberPhone
        val expectedCity =  ProfileCache.profile.city

        viewModel.refreshData()

        assertEquals(expectedEmail, viewModel.email)
        assertEquals(expectedNumberPhone, viewModel.numberPhone)
        assertEquals(expectedCity, viewModel.city)
    }

    /**
     * Тест на проверку того, что при открытии профиля, карточка с балансом отдыха не открыта
     */
    @Test
    fun cardBalanceHolidayIsClosed(){
        val viewModel = ProfileViewModel()

        val expected = false

        assertEquals(expected, viewModel.isShowCardBalanceHoliday)
    }
}