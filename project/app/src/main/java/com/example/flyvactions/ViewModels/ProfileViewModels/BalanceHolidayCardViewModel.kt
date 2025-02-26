package com.example.flyvactions.ViewModels.ProfileViewModels

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.flyvactions.Models.Cache.ProfileCache
import com.example.flyvactions.Models.DataBase.Entities.AbsenceEmployee
import com.example.flyvactions.Models.DataBase.Queries.Get
import com.example.flyvactions.Models.WorkWithStringAndDate.convertStringToLocalDate
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

    private val difference = Period.between(ProfileCache.profile.hireDate, LocalDate.now()).years
    //Считает разницу в годах между двумя датами (за счёт этого, определим, сколько первично у сотрудника, должно быть добавлено к отпускным дням) (но небольше 10 дополнительных дней за стаж)
    var daysVacationsForExperience by mutableIntStateOf(
        if(difference > 3) { 3 }
        else {difference}
    )

    var daysVacationPlanned by mutableIntStateOf(0)
    var daysVacationsForExperiencePlanned by mutableIntStateOf(0)

    /**
     * Функция на получение отсутствий пользователя, чтобы определить, запланированные дни отсуствия по отпуску
     */
    fun fetchAbsencesEmployee(){
        if(ProfileCache.profile.userInfo!=null){
            var _daysVacationsForExperience = if(Period.between(ProfileCache.profile.hireDate, LocalDate.now()).years > 3) { 3 }
            else {Period.between(ProfileCache.profile.hireDate, LocalDate.now()).years}
            val borderDate = LocalDate.now().minusDays(31)
            viewModelScope.launch {

                //Получаем id необходимой причины
                val idReasonVacation = get.getReasonAbsenceByName("Отпуск")!!.id

                //Получаем все записи отсуствия пользователя по причине отпуск (их может быть не более двух по логике моего "предприятия")
                val listReasonsAbsencesEmployee : List<AbsenceEmployee> = get
                    .getAbsencesEmployeesByIdUserAndReasonId(ProfileCache.profile.userInfo!!.id, idReasonVacation)
                    .filter { convertStringToLocalDate(it.beginDate) >= borderDate } //В учёт берём минус месяц от текущей даты и до конца, что там будет (больше двух отпусков пользователь не возьмёт)
                Log.d("listVacations", "$listReasonsAbsencesEmployee")
                var getDaysVacationPlanned : Int = 0
                listReasonsAbsencesEmployee.forEach{
                    getDaysVacationPlanned += it.amountDay
                }
                if(getDaysVacationPlanned in 14..17){
                    daysVacationsForExperiencePlanned = getDaysVacationPlanned - 14 //Получаем количество запланированных за стаж
                    _daysVacationsForExperience -= daysVacationsForExperiencePlanned //Получаем нынешний баланс за стаж с учётом запланированных
                    daysVacationPlanned = 14 //Тут два сценария, либо 14, либо 28, остальное это дни за стаж, которые можно добавить к 14 или 28
                    daysVacationsForExperience = _daysVacationsForExperience
                }
                else if(getDaysVacationPlanned in 28..31){
                    daysVacationsForExperiencePlanned = getDaysVacationPlanned - 28
                    _daysVacationsForExperience -= daysVacationsForExperiencePlanned
                    daysVacationPlanned = 28
                    daysVacationsForExperience = _daysVacationsForExperience
                }
            }
        }
    }
}