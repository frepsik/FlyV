package com.example.flyvactions.ViewModels

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.flyvactions.Models.Cache.ProfileCache
import com.example.flyvactions.Models.DataBase.Entities.AbsenceEmployee
import com.example.flyvactions.Models.DataBase.Entities.Employee
import com.example.flyvactions.Models.DataBase.Queries.Auth
import com.example.flyvactions.Models.DataBase.Queries.Get
import com.example.flyvactions.Models.DataClasses.AbsenceEmployeeCalendar
import com.example.flyvactions.Models.WorkWithStringAndDate.convertStringToLocalDate
import com.example.flyvactions.Models.WorkWithStringAndDate.parseFullNameEmployee
import io.github.jan.supabase.gotrue.user.UserInfo
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.format.DateTimeFormatter

/**
 * Бизнес-логика главного окна (MainView)
 */
class MainViewModel : ViewModel() {


    private var auth : Auth = Auth()
    private var get : Get = Get()

    val userInfo : UserInfo? = auth.authorizedUser()

    var selectedDate by mutableStateOf<LocalDate?>(null)

    private var _listAEC : MutableList<AbsenceEmployeeCalendar> = mutableListOf()
    val listAEC : MutableList<AbsenceEmployeeCalendar> = _listAEC


    var flagLazyColumn by mutableStateOf(false)
    var flagAdditionalText by mutableStateOf(false)

    var flagVacationSoon by mutableStateOf(false)
    var isVacation by mutableStateOf(false)

    var isVacationEnd by mutableStateOf(false)
    var flagEndVacation by mutableStateOf(false)



    private val months = listOf("Январь", "Февраль", "Март", "Апрель", "Май", "Июнь",
        "Июль", "Август", "Сентябрь", "Октябрь", "Ноябрь", "Декабрь")

    //Определяем нынешний месяц и текущую неделю (начало и конец)
    private val currentDate : LocalDate = LocalDate.now()

    val dateBeginWeek: LocalDate = currentDate.minusDays(currentDate.dayOfWeek.value.toLong() - 1)
    val dateEndWeek: LocalDate = dateBeginWeek.plusDays(6)
    val dayBeginWeek = dateBeginWeek.dayOfMonth
    val dayEndWeek = dateEndWeek.dayOfMonth
    val monthSecond = if(dateEndWeek.dayOfMonth >= 1) {"${months[dateEndWeek.monthValue-1]}"} else { "" }
    val month: String = if(monthSecond.isEmpty()) {months[currentDate.monthValue - 1]} else {"${months[currentDate.monthValue - 1]} - ${monthSecond.lowercase()}"}
    private val outputFormatter = DateTimeFormatter.ofPattern("dd.MM")

    private var listAbsencesEmployees : List<AbsenceEmployee> = listOf()
    var messageSoonVacation : String by mutableStateOf("")
    var messageEndVacation : String by mutableStateOf("")


    private var employee : Employee? = null

    var urlProfile by mutableStateOf("")
    var isEnabledProfile by mutableStateOf(true)


    /**
     * Метод для вызова функций из Models, с запросами к базе на получение пользователей, отсуствующих в определённую дату по определённой причине
     */
    fun fetchEmployeesByDateAbsence(date: LocalDate){

        viewModelScope.launch {
            val aec = get.getAbsenceEmployeesCalendarByDate(date)
            _listAEC.addAll(aec)
            Log.d("_listAEC", "${_listAEC}")
            Log.d("listAEC", "${listAEC}")

            //Проверяем, есть ли кто то в списке по дате в зависимости от этого выводим список отсутствующих или запись, что все на месте
            flagLazyColumn = _listAEC.isNotEmpty()
            flagAdditionalText = !flagLazyColumn
        }
    }

    /**
     * Очиста списка с пользователями
     */
    fun clearListAEC(){
        _listAEC.clear()
        Log.d("_listAEC", "${ _listAEC}")
        flagLazyColumn = false
        flagAdditionalText = false
    }

    /**
     * Метод для определения, осталось ли (3, 2, 1) дня до отпуска вошедшего пользователя
     */
    fun isVacationSoon(){
        viewModelScope.launch {
            listAbsencesEmployees = get.getEmployeeByVacation()

            listAbsencesEmployees.forEach{

                if(it.employeeId == ProfileCache.profile.userInfo?.id){
                    if(convertStringToLocalDate(it.beginDate).dayOfYear - currentDate.dayOfYear == 3){
                        isVacation = true
                        messageSoonVacation = "3 дня"
                        flagVacationSoon = true
                    }
                    else if(convertStringToLocalDate(it.beginDate).dayOfYear - currentDate.dayOfYear == 2){
                        isVacation = true
                        messageSoonVacation = "2 дня"
                        flagVacationSoon = true
                    }
                    else if(convertStringToLocalDate(it.beginDate).dayOfYear - currentDate.dayOfYear == 1){
                        isVacation = true
                        messageSoonVacation = "1 день"
                        flagVacationSoon = true
                    }
                    else if(currentDate.dayOfYear in
                        convertStringToLocalDate(it.beginDate).dayOfYear..convertStringToLocalDate(it.beginDate).plusDays(it.amountDay.toLong()-1).dayOfYear){
                        isVacationEnd = true
                        flagEndVacation = true
                        messageEndVacation = convertStringToLocalDate(it.beginDate).plusDays(it.amountDay.toLong()-1).format(outputFormatter)

                    }
                }
            }
        }
    }

    /**
     * Метод для получения пользователя, при запуске приложения (во время навигации не будет срабатывать)
     */
    fun getUser(){
        viewModelScope.launch {
            employee = get.getEmployeeById(ProfileCache.profile.userInfo!!.id)
            val cityName : String = get.getCityById(employee!!.cityId)!!.city
            urlProfile = employee!!.urlPhotoProfile ?: "https://lpdnebdhpgflnqtlksnj.supabase.co/storage/v1/object/public/photosProfileUsers/UnnamedProfile.jpg"
            with(ProfileCache.profile){
                urlPhotoProfile = employee?.urlPhotoProfile
                city = cityName
                fullName = parseFullNameEmployee(employee!!.fullName)
                numberPhone = employee!!.numberPhone
                email = employee!!.email ?: "-"
                daysOff = employee!!.daysOff
                daysVacation = employee!!.daysVacation
                hireDate = convertStringToLocalDate(employee!!.hireDate)
            }
        }
    }
}


