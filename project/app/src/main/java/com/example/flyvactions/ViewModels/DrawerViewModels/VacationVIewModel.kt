package com.example.flyvactions.ViewModels.DrawerViewModels

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.flyvactions.Models.Cache.ProfileCache
import com.example.flyvactions.Models.DataBase.Entities.AbsenceEmployee
import com.example.flyvactions.Models.DataBase.Queries.Get
import com.example.flyvactions.Models.WorkWithStringAndDate.convertStringToLocalDate
import kotlinx.coroutines.launch
import java.time.LocalDate

/**
 * Бизнес-логика окна VacationView
 */
class VacationVIewModel : ViewModel() {
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

    //Выбранные даты
    var firstSelectedDate = mutableStateOf<LocalDate?>(null)
    var lastSelectedDate = mutableStateOf<LocalDate?>(null)

    var isShowCardBalanceHoliday by mutableStateOf(false)
    var isEnabledPlanned by mutableStateOf(false)


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
        //Нельзя переключать на месяц позднее нынешнего в логике оформления отпусков
        if(newYear > currentYear || (newYear == currentYear && indexCurrentMonth + 1 > currentDate.monthValue) ){
            if(indexCurrentMonth in 1..11){
                indexCurrentMonth -= 1
                editBeginAndEndDateMonth(indexCurrentMonth, newYear)
            }
            else{
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
     * Функция для получения отпусков пользователя в ближайшее время
     */
    fun fetchDatesVacation(){

        val borderDate = LocalDate.now().minusDays(31)

        viewModelScope.launch {
            val idReasonVacation = get.getReasonAbsenceByName("Отпуск")!!.id
            val listReasonsAbsencesEmployee : List<AbsenceEmployee> = get
                .getAbsencesEmployeesByIdUserAndReasonId(ProfileCache.profile.userInfo!!.id, idReasonVacation)
                .filter {
                    convertStringToLocalDate(it.beginDate) >= borderDate //В учёт берём минус месяц от текущей даты и до конца, что там будет (больше двух отпусков пользователь не возьмёт)
                }

        }
    }
}