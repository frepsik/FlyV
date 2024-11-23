package com.example.flyvactions.ViewModels.ProfileViewModels

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.flyvactions.Models.Cache.ProfileCache
import com.example.flyvactions.Models.DataBase.Entities.AbsenceEmployee
import com.example.flyvactions.Models.DataBase.Queries.Get
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.Period


/**
 * Бизнес-логика окна BalanceHolidayCardView
 */
class BalanceHolidayCardViewModel : ViewModel() {

    private var get : Get = Get()

    var daysVacation by mutableIntStateOf(ProfileCache.profile.daysVacation)
    var daysOff by mutableIntStateOf(ProfileCache.profile.daysOff)
    //Считает разницу в годах между двумя датами (за счёт этого, определим, сколько первично у сотрудника, должно быть добавлено к отпускным дням) (но небольше 10 дополнительных дней за стаж)
    var daysVacationsForExperience by mutableIntStateOf(
        if(Period.between(ProfileCache.profile.hireDate, LocalDate.now()).years > 10) { 10 }
        else {Period.between(ProfileCache.profile.hireDate, LocalDate.now()).years}
    )

    var daysVacationPlanned by mutableIntStateOf(0)
    var daysVacationsForExperiencePlanned by mutableIntStateOf(0)

    /**
     * Функция на получение отсутствий пользователя, чтобы определить, запланированные дни отсуствия по отпуску
     */
    fun fetchAbsencesEmployee(){
        var _daysVacationsForExperience = if(Period.between(ProfileCache.profile.hireDate, LocalDate.now()).years > 10) { 10 }
        else {Period.between(ProfileCache.profile.hireDate, LocalDate.now()).years}

        viewModelScope.launch {
            //Получаем id необходимой причины
            val idReasonVacation = get.getReasonAbsenceByName("Отпуск")!!.id

            //Получаем все записи отсуствия пользователя по причине отпуск (их может быть не более двух по логике моего "предприятия")
            val listReasonsAbsencesEmployee : List<AbsenceEmployee> = get.getAbsencesEmployeesByIdUserAndReasonId(ProfileCache.profile.userInfo!!.id, idReasonVacation)
            var getDaysVacationPlanned : Int = 0
            listReasonsAbsencesEmployee.forEach{
                getDaysVacationPlanned += it.amountDay
            }
            if(getDaysVacationPlanned in 14..24){
                daysVacationsForExperiencePlanned = getDaysVacationPlanned - 14 //Получаем количество запланированных за стаж
                _daysVacationsForExperience -= daysVacationsForExperiencePlanned //Получаем нынешний баланс за стаж с учётом запланированных
                daysVacationPlanned = 14 //Тут два сценария, либо 14, либо 28, остальное это дни за стаж, которые можно добавить к 14 или 28
                daysVacationsForExperience = _daysVacationsForExperience
            }
            else if(getDaysVacationPlanned in 28..38){
                daysVacationsForExperiencePlanned = getDaysVacationPlanned - 28
                _daysVacationsForExperience -= daysVacationsForExperiencePlanned
                daysVacationPlanned = 28
                daysVacationsForExperience = _daysVacationsForExperience
            }
        }
    }
}