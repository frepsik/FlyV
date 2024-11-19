package com.example.flyvactions.ViewModels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.flyvactions.Models.DataBase.Queries.Auth
import io.github.jan.supabase.gotrue.user.UserInfo
import kotlinx.coroutines.launch
import java.text.DateFormatSymbols
import java.time.LocalDate
import java.time.format.TextStyle
import java.util.Locale


class MainViewModel : ViewModel() {
    private var auth : Auth = Auth()
    val userInfo : UserInfo? = auth.authorizedUser()

    private val months = listOf("Январь", "Февраль", "Март", "Апрель", "Май", "Июнь",
        "Июль", "Август", "Сентябрь", "Октябрь", "Ноябрь", "Декабрь")

    private val currentDate : LocalDate = LocalDate.now()
    val month: String = months[currentDate.monthValue - 1]
    val dateBeginWeek: LocalDate = currentDate.minusDays(currentDate.dayOfWeek.value.toLong() - 1)
    val dateEndWeek: LocalDate = dateBeginWeek.plusDays(6)
    val dayBeginWeek = dateBeginWeek.dayOfMonth
    val dayEndWeek = dateEndWeek.dayOfMonth

    fun fetchEmployeesByDateAbsence(){
        viewModelScope.launch {  }
    }
}