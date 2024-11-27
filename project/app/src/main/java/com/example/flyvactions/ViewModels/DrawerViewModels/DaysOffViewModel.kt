package com.example.flyvactions.ViewModels.DrawerViewModels

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.flyvactions.Models.Cache.ProfileCache
import com.example.flyvactions.Models.DataBase.Entities.AbsenceEmployee
import com.example.flyvactions.Models.DataBase.Queries.Get
import com.example.flyvactions.Models.DataBase.Queries.Insert
import com.example.flyvactions.Models.DataBase.Queries.Update
import com.example.flyvactions.Models.WorkWithStringAndDate.convertStringToLocalDate
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.util.UUID

/**
 * Бизнес-логика для окна оформления отгула (DaysOffView)
 */
class DaysOffViewModel : ViewModel() {
    private val get : Get = Get()
    private val insert : Insert = Insert()
    private val update : Update = Update()

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
    var selectedDate = mutableStateOf<LocalDate?>(null)

    var isShowCardBalanceHoliday by mutableStateOf(false)
    var isEnabledPlannedFirst by mutableStateOf(false)
    var isEnabledPlannedSecond by mutableStateOf(false)

    var listDaysOff = mutableStateListOf<LocalDate>()

    private var idReasonDaysOff = ""
    var isSuccessInsert by mutableStateOf<Boolean?>(null)
    var isShowInsert by mutableStateOf(false)
    var isShowHint by mutableStateOf(false)

    var hint by mutableStateOf("")

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

    private fun addDateInDaysOffList(date : LocalDate){
        listDaysOff.add(date)
    }

    /**
     * Функция для получения отгулов, что были сделаны пользователем
     */
    fun fetchDatesDaysOff(){
        val borderDate = LocalDate.now().minusDays(31)
        viewModelScope.launch {
            idReasonDaysOff = get.getReasonAbsenceByName("Отгул")!!.id

            //Получаем отсутствия по пользователю и причине отгул где дата начинается с месяц назад
            val listReasonsAbsencesEmployeeByDaysOff : List<AbsenceEmployee> = get
                .getAbsencesEmployeesByIdUserAndReasonId(ProfileCache.profile.userInfo!!.id, idReasonDaysOff)
                .filter {
                    convertStringToLocalDate(it.beginDate) >= borderDate //В учёт берём минус месяц от текущей даты и до конца, что там будет (больше двух отпусков пользователь не возьмёт)
                }
            listReasonsAbsencesEmployeeByDaysOff.forEach {
                addDateInDaysOffList(convertStringToLocalDate(it.beginDate))
            }
        }
    }

    /**
     * Метод для проверки, имеет ли сотрудник отгулы и может ли оформлять отгул
     */
    fun checkIsHaveRegistrationDaysOff(){
        if(ProfileCache.profile.daysOff > 0){
            if(selectedDate.value!! > currentDate){
                isEnabledPlannedSecond = true
            }
            else{
                hint = "Отгул нужно оформлять за день и более"
                Log.d("DaysOffPlannedUnCorrect","False")
                isEnabledPlannedSecond = false
                isShowHint = true
            }
        }
        else {
            hint = "К сожалению, у вас больше нет"
            Log.d("DaysOffAmount","False")
            isEnabledPlannedSecond = false
            isShowHint = true
        }
    }

    /**
     * Метод для оформления отгула
     */
    fun daysOffRegistration(){
        viewModelScope.launch {
            isSuccessInsert = insert.insertAbsencesEmployees(
                AbsenceEmployee(
                    UUID.randomUUID().toString(),
                    idReasonDaysOff,
                    ProfileCache.profile.userInfo!!.id,
                    selectedDate.value.toString(),
                    1
                )
            )
            if(isSuccessInsert!!){
                updateDate()
                update.updateDaysOffByUserId(ProfileCache.profile.userInfo!!.id, ProfileCache.profile.daysOff)
            }
            isShowInsert = isSuccessInsert!!
        }
    }

    /**
     * Функция обновления старых данных до новых
     */
    private fun updateDate(){
        addDateInDaysOffList(selectedDate.value!!)
        ProfileCache.profile.daysOff -=1
    }
}