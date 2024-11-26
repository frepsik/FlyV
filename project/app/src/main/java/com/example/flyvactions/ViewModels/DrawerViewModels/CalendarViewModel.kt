package com.example.flyvactions.ViewModels.DrawerViewModels

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.flyvactions.Models.DataBase.Queries.Get
import com.example.flyvactions.Models.DataClasses.AbsenceEmployeeCalendar
import kotlinx.coroutines.launch
import java.time.LocalDate

/**
 * Бизнес-логика окна CalendarView
 */
class CalendarViewModel : ViewModel() {

    private val get : Get = Get()

    private val currentDate : LocalDate = LocalDate.now()

    private val months = listOf("Январь", "Февраль", "Март", "Апрель", "Май", "Июнь",
        "Июль", "Август", "Сентябрь", "Октябрь", "Ноябрь", "Декабрь")

    private var indexCurrentMonth = currentDate.monthValue-1
    private val currentMonth : String = months[indexCurrentMonth]
    private val currentYear : Int = currentDate.year
    private var newYear = currentYear
    //Текущий месяц и год
    var monthAndYear by mutableStateOf("$currentMonth, $currentYear")

    //Первый день месяца и последний
    var beginDayMonth by mutableStateOf(currentDate.withDayOfMonth(1))
    var endDayMonth by mutableStateOf(currentDate.withDayOfMonth(currentDate.lengthOfMonth())) //Считаем длину месяца и это число пихаем в функцию,
    // которая создаёт новый объект LocalDate, с новым числом дня месяца

    //Выбранная дата
    var selectedDate by mutableStateOf<LocalDate?>(null)

    //Список отсутствующих сотрудников
    private var _listAEC : MutableList<AbsenceEmployeeCalendar> = mutableListOf()
    val listAEC : MutableList<AbsenceEmployeeCalendar> = _listAEC

    var flagLazyColumn by mutableStateOf(false)
    var isData by mutableStateOf(false)
    var isToday by mutableStateOf(false)
    var isReset by mutableStateOf(false)

    /**
     * Функция получения следующего месяца
     */
    fun nextMonth(){

        if(indexCurrentMonth in 0..10){
            indexCurrentMonth += 1
            editBeginAndEndDateMonth(indexCurrentMonth, newYear)
        }
        else{
            indexCurrentMonth = 0
            newYear += 1
            editBeginAndEndDateMonth(indexCurrentMonth, newYear)
            Log.d("NewYear", "")
        }
        val month = months[indexCurrentMonth]


        monthAndYear = "$month, $newYear"
    }

    /**
     * Функция получения предыдущего месяца
     */
    fun prevMonth(){
        //Добавил на ограничение на 2023 год, будем считать, что компания начала работу с этого года
        if(newYear >= 2023){
            if(indexCurrentMonth in 1..11){
                indexCurrentMonth -= 1
                editBeginAndEndDateMonth(indexCurrentMonth, newYear)
            }
            //Здесь для того, чтобы не перейти на предыдущий месяц и год, если это 0 по индексу месяц 2023
            else if(newYear > 2023){
                indexCurrentMonth = 11
                newYear -= 1
                editBeginAndEndDateMonth(indexCurrentMonth, newYear)
            }
            val month = months[indexCurrentMonth]


            monthAndYear = "$month, $newYear"
        }
    }


    /**
     * Функция для изменения первой и последней даты месяца
     */
    private fun editBeginAndEndDateMonth(indexCurrentMonth : Int, year : Int){
        beginDayMonth = LocalDate.of(year,indexCurrentMonth+1, 1)
        endDayMonth = beginDayMonth.withDayOfMonth(beginDayMonth!!.lengthOfMonth())
    }


    /**
     * Функция поулчения сегодняшней даты
     */
    fun today(){
        monthAndYear = "$currentMonth, $currentYear"
        selectedDate = currentDate
        isToday = true
    }

    /**
     * Метод для вызова функций из Models, с запросами к базе на получение пользователей, отсуствующих в определённую дату по определённой причине
     */
    fun fetchEmployeesByDateAbsence(){

        viewModelScope.launch {
            val aec = get.getAbsenceEmployeesCalendarByDate(selectedDate!!)
            _listAEC.addAll(aec)
            Log.d("_listAEC", "${_listAEC}")
            Log.d("listAEC", "${listAEC}")

            //Проверяем, есть ли кто то в списке по дате в зависимости от этого выводим список отсутствующих или запись, что все на месте
            flagLazyColumn = _listAEC.isNotEmpty()
            isData = true

        }
    }

    /**
     * Очиста списка с пользователями (для того, чтобы не переполнялся)
     */
    fun clearListAEC(){
        _listAEC.clear()
        Log.d("_listAEC", "${ _listAEC}")
        flagLazyColumn = false
        isData = false
    }
}