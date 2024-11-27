package com.example.flyvactions.ViewModels.DrawerViewModels

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.flyvactions.Models.Cache.ProfileCache
import com.example.flyvactions.Models.DataBase.Entities.AbsenceEmployee
import com.example.flyvactions.Models.DataBase.Entities.Substitution
import com.example.flyvactions.Models.DataBase.Queries.Get
import com.example.flyvactions.Models.DataBase.Queries.Insert
import com.example.flyvactions.Models.DataBase.Queries.Update
import com.example.flyvactions.Models.WorkWithStringAndDate.convertStringToLocalDate
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.Period
import java.util.UUID

/**
 * Бизнес-логика окна VacationView
 */
class VacationVIewModel : ViewModel() {
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
    var firstSelectedDate = mutableStateOf<LocalDate?>(null)
    var lastSelectedDate = mutableStateOf<LocalDate?>(null)

    var isShowCardBalanceHoliday by mutableStateOf(false)

    var isEnabledPlannedFirst by mutableStateOf(false)
    var isEnabledPlannedSecond by mutableStateOf(false)
    var isVisionHint by mutableStateOf(false)

    //List - не является изменяемым, а MutableList - является изменяемым списком
    var listFirstAndLastDaysVacation = mutableStateListOf<List<LocalDate>>()

    var amountDaysPlanned by mutableIntStateOf(0)

    var hint by mutableStateOf("")

    var isSuccessInsert by mutableStateOf<Boolean?>(null)
    var isShowInsert by mutableStateOf(false)
    private var idVacation : String = ""

    var resetState = mutableStateOf(false)

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


    private fun addListInFirstAndLastDaysVacation(firstDate : LocalDate, lastDate : LocalDate){
        //Заполняем список диапазонами дат, когда у пользователя отпуск
        listFirstAndLastDaysVacation.add(listOf(firstDate, lastDate))
    }

    /**
     * Функция для получения отпусков пользователя в ближайшее время
     */
    fun fetchDatesVacation(){
        val borderDate = LocalDate.now().minusDays(31)
        viewModelScope.launch {
            val idReasonVacation = get.getReasonAbsenceByName("Отпуск")!!.id
            idVacation = idReasonVacation
            val listReasonsAbsencesEmployeeByVacation : List<AbsenceEmployee> = get
                .getAbsencesEmployeesByIdUserAndReasonId(ProfileCache.profile.userInfo!!.id, idReasonVacation)
                .filter {
                    convertStringToLocalDate(it.beginDate) >= borderDate //В учёт берём минус месяц от текущей даты и до конца, что там будет (больше двух отпусков пользователь не возьмёт)
                }


            listReasonsAbsencesEmployeeByVacation.forEach{
                //Заполняем список диапазонами дат, когда у пользователя отпуск
                addListInFirstAndLastDaysVacation(convertStringToLocalDate(it.beginDate), convertStringToLocalDate(it.beginDate).plusDays(it.amountDay.toLong()-1))
            }
        }
    }

    /**
     * Функция предназначенная для получения необходимых данных и определения может ли пользователь оформить отпуск
     */
    fun checkPossibilityVacation(){
        val difference = Period.between(ProfileCache.profile.hireDate, LocalDate.now()).years
        ProfileCache.profile.daysVacationForExperience =
            if(difference > 3) { 3 }
            else {difference}

        viewModelScope.launch {
            //Отпуск нужно оформить хотя бы за 7 дней до него
            if(firstSelectedDate.value!! >= currentDate.plusDays(7)){
                //Проверяем, что число выбранных дней не более количества доступных для отпуска дней, и что выбранное количество от 14 по 17 или от 28 по 31
                if(amountDaysPlanned <= ProfileCache.profile.daysVacation + ProfileCache.profile.daysVacationForExperience &&
                    (
                        (ProfileCache.profile.daysVacationForExperience == 0 && (amountDaysPlanned == 14 || amountDaysPlanned == 28)) ||
                        (ProfileCache.profile.daysVacationForExperience == 1 && (amountDaysPlanned == 15 || amountDaysPlanned == 29)) ||
                        (ProfileCache.profile.daysVacationForExperience == 2 && (amountDaysPlanned == 16 || amountDaysPlanned == 30)) ||
                        (ProfileCache.profile.daysVacationForExperience == 3 && (amountDaysPlanned == 17 || amountDaysPlanned == 31))
                    )
                ){
                    val substitutions = get.getSubstitutions()
                    var substitution : Substitution? = null
                    substitutions.forEach{
                        if(it.employeeFirstId == ProfileCache.profile.userInfo!!.id || it.employeeSecondId == ProfileCache.profile.userInfo!!.id) {
                            //Нашли и получили объект, где указано, кого мы заменяем
                            substitution = it
                        }
                    }

                    //Получаем id пользователя которого должны заменять
                    val idUserSubstitution =
                        if(substitution!!.employeeFirstId == ProfileCache.profile.userInfo!!.id) substitution!!.employeeSecondId
                        else substitution!!.employeeFirstId
                    Log.d("idUserSubstitution", idUserSubstitution)

                    val listPlanned : List<AbsenceEmployee> = get.getAbsencesEmployeesByUserId(idUserSubstitution)
                    var isRangeNotTouch : Boolean = true

                    for(it in listPlanned){
                        //Проверяем (истина), что отпуск кончается раньше начала даты отсутствия или начинается после даты окончания отсутствия подменяемого сотрудника
                        if(!(lastSelectedDate.value!! < convertStringToLocalDate(it.beginDate) ||
                                    firstSelectedDate.value!! > convertStringToLocalDate(it.beginDate).plusDays(it.amountDay.toLong()-1))){
                            isRangeNotTouch = false
                            break
                        }
                    }

                    //Проверяем подошёл ли выбранный диапазон
                    if(isRangeNotTouch){
                        var isRangeVacationsNotTouch : Boolean = true
                        for(it in listFirstAndLastDaysVacation){
                            //Проверяем, что последний день отпуска выбранного заканчивается ранее первого дня отпуска, что был уже запланирован,
                            // и первый день выбранного отпуска больше дня конца запланированного отпуска
                            if(!(lastSelectedDate.value!! < it[0] ||
                                        firstSelectedDate.value!! > it[1])){
                                isRangeVacationsNotTouch = false
                                break
                            }
                        }

                        if(isRangeVacationsNotTouch){
                            //Получаем даты отсутствия по причине командировки пользователя
                            val idReasonVacation = get.getReasonAbsenceByName("Командировка")!!.id
                            val listReasonsAbsencesEmployee : List<AbsenceEmployee> = get
                                .getAbsencesEmployeesByIdUserAndReasonId(ProfileCache.profile.userInfo!!.id, idReasonVacation)

                            var isRangeBusinessTripNotTouch = true
                            for(it in listReasonsAbsencesEmployee){
                                //Здесь проверяем не совпадают ли даты с возможно назначенной командировкой
                                if(!(lastSelectedDate.value!! < convertStringToLocalDate(it.beginDate) ||
                                            firstSelectedDate.value!! > convertStringToLocalDate(it.beginDate).plusDays(it.amountDay.toLong()-1))){
                                    isRangeBusinessTripNotTouch = false
                                    break
                                }
                            }
                            if(isRangeBusinessTripNotTouch){
                                Log.d("Check", "Success")
                                isEnabledPlannedSecond = true
                            }
                            else{
                                hint = "В данный период у вас запланирована командировка"
                                Log.d("PeriodDaysUnCorrectBusinessTrip", "False")
                                isEnabledPlannedSecond = false
                                isVisionHint = !isEnabledPlannedSecond
                            }
                        }
                        else{
                            hint = "В данный период у вас уже запланирован отпуск"
                            Log.d("PeriodDaysUnCorrectVacations", "False")
                            isEnabledPlannedSecond = false
                            isVisionHint = !isEnabledPlannedSecond
                        }
                    }
                    else{
                        hint = "В данный период вы заменяете сотрудника"
                        Log.d("PeriodDaysUnCorrectSubstitution", "False")
                        isEnabledPlannedSecond = false
                        isVisionHint = !isEnabledPlannedSecond
                    }
                }
                else{
                    hint = "Количество дней, должно быть: 14-17, 28-31"
                    Log.d("AmountDays", "False ${amountDaysPlanned}")
                    isEnabledPlannedSecond = false
                    isVisionHint = !isEnabledPlannedSecond
                }
            }
            else{
                hint = "Планировать необходимо за 7 дней"
                Log.d("7DaysBeforeStart", "False")
                isEnabledPlannedSecond = false
                isVisionHint = !isEnabledPlannedSecond
            }

        }
    }

    /**
     * Метод для оформления отпуска
     */
    fun vacationRegistration(){
        viewModelScope.launch {
            isSuccessInsert = insert.insertAbsencesEmployees(
                AbsenceEmployee(
                    UUID.randomUUID().toString(),
                    idVacation,
                    ProfileCache.profile.userInfo!!.id,
                    firstSelectedDate.value.toString(),
                    amountDaysPlanned
                )
            )
            if(isSuccessInsert!!){
                //Производим в данной корутине обновление данных ( viewModelScope.launch - это корутина, но корутина это некоторый, более лёгкий аналог потока)
                updateData()
                //Обновляю дни отпуска в базе данных
                update.updateDaysVacationsByUserId(ProfileCache.profile.userInfo!!.id, ProfileCache.profile.daysVacation)
            }
            isShowInsert = isSuccessInsert!!
        }
    }

    /**
     * Обновление данных до новых
     */
    private fun updateData(){
        //Обновляем список отпусков
        addListInFirstAndLastDaysVacation(firstSelectedDate.value!!,  lastSelectedDate.value!!)

        Log.d("amountDaysPlanned", "${amountDaysPlanned}")
        //Обновляем данные по балансу (запланированные динамически рассчитываются, как только открыть кароточку)
        if(amountDaysPlanned in 14..17){

            ProfileCache.profile.daysVacation -= 14
            ProfileCache.profile.daysVacationForExperience -= amountDaysPlanned - 14
            Log.d("ProfileCache", "$ProfileCache.profile.daysVacation")
        }
        else if(amountDaysPlanned in 28..31){
            ProfileCache.profile.daysVacation -= 28
            ProfileCache.profile.daysVacationForExperience -= amountDaysPlanned - 28
            Log.d("ProfileCache", "${ProfileCache.profile.daysVacation}")
        }

        Log.d("UpdateData", "Success")
    }
}