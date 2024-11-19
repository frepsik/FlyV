package com.example.flyvactions.ViewModels

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.flyvactions.Models.DataBase.Queries.Auth
import com.example.flyvactions.Models.DataBase.Queries.Get
import com.example.flyvactions.Models.DataClasses.AbsenceEmployeeCalendar
import io.github.jan.supabase.gotrue.user.UserInfo
import kotlinx.coroutines.launch
import java.text.DateFormatSymbols
import java.time.LocalDate
import java.time.format.TextStyle
import java.util.Locale


class MainViewModel : ViewModel() {
    private var auth : Auth = Auth()
    private var get : Get = Get()

    var selectedDate by mutableStateOf<LocalDate?>(null)

    private var _listAEC : MutableList<AbsenceEmployeeCalendar> = mutableListOf()
    val listAEC : MutableList<AbsenceEmployeeCalendar> = _listAEC
    var flagLazyColumn by mutableStateOf(false)
    var flagAdditionalText by mutableStateOf(false)

    val userInfo : UserInfo? = auth.authorizedUser()

    private val months = listOf("Январь", "Февраль", "Март", "Апрель", "Май", "Июнь",
        "Июль", "Август", "Сентябрь", "Октябрь", "Ноябрь", "Декабрь")

    private val currentDate : LocalDate = LocalDate.now()
    val month: String = months[currentDate.monthValue - 1]
    val dateBeginWeek: LocalDate = currentDate.minusDays(currentDate.dayOfWeek.value.toLong() - 1)
    val dateEndWeek: LocalDate = dateBeginWeek.plusDays(6)
    val dayBeginWeek = dateBeginWeek.dayOfMonth
    val dayEndWeek = dateEndWeek.dayOfMonth


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

    fun clearListAEC(){
        _listAEC.clear()
        Log.d("_listAEC", "${ _listAEC}")
        flagLazyColumn = false
        flagAdditionalText = false
    }
}


