package com.example.flyvactions.ViewModels.DrawerViewModels

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.flyvactions.Models.Cache.ProfileCache
import com.example.flyvactions.Models.DataBase.Entities.AbsenceEmployee
import com.example.flyvactions.Models.DataBase.Queries.Delete
import com.example.flyvactions.Models.DataBase.Queries.Get
import com.example.flyvactions.Models.DataBase.Queries.Insert
import com.example.flyvactions.Models.DataBase.Queries.Update
import com.example.flyvactions.Models.WorkWithStringAndDate.convertStringToLocalDate
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.temporal.ChronoUnit
import java.util.UUID

/**
 * Бизнес-логика окна связанного с больничным (MdeicalView)
 */
class MedicalViewModel : ViewModel() {
    val insert : Insert = Insert()
    val update : Update = Update()
    val get : Get = Get()
    val delete : Delete = Delete()

    val currentDate : LocalDate = LocalDate.now()

    var amountDays by mutableIntStateOf(0)
    var isOpen by mutableStateOf(false)
    private var idReasonMedical = ""

    var hint by mutableStateOf("")
    var isShowHint by mutableStateOf(false)

    /**
     * Метод для получения больничного если такой открыт
     */
    fun fetchMedicalPeriod(){
        viewModelScope.launch {
            idReasonMedical = get.getReasonAbsenceByName("Больничный")!!.id

            //Получаем отсутствия по пользователю и причине отгул где дата начинается с месяц назад
            val listReasonsAbsencesEmployeeByMedical: List<AbsenceEmployee> = get
                .getAbsencesEmployeesByIdUserAndReasonId(ProfileCache.profile.userInfo!!.id, idReasonMedical)

            if(listReasonsAbsencesEmployeeByMedical.isNotEmpty()){
                Log.d("ListMedicalIsNotEmpty", "True")
                amountDays = listReasonsAbsencesEmployeeByMedical[0].amountDay
                val beginDate = convertStringToLocalDate(listReasonsAbsencesEmployeeByMedical[0].beginDate)
                if(currentDate > beginDate){
                    amountDays =  ChronoUnit.DAYS.between(beginDate, currentDate).toInt() + 1
                    Log.d("AmountDaysCalculation", "$amountDays")
                }
                isOpen = true
                if(beginDate.plusDays(amountDays.toLong()-1) < currentDate){
                    Log.d("UpdateAmountDates", "Begin")
                    //Обновляем количество дней в базе
                    medicalUpdate()
                }
            }
            else{
                isOpen = false
            }
        }
    }


    /**
     * Метод для обновления количества дней больничного
     */
    fun medicalUpdate(){
        viewModelScope.launch {
            update.updateAmountDaysForMedicalPeriodByUserId(ProfileCache.profile.userInfo!!.id, amountDays)
        }
    }


    /**
     * Метод для открытия больничного
     */
    fun medicalRegistration(){
        Log.d("RegistrationMedical","True")
        viewModelScope.launch {
            val isSuccess = insert.insertAbsencesEmployees(
                AbsenceEmployee(
                    UUID.randomUUID().toString(),
                    idReasonMedical,
                    ProfileCache.profile.userInfo!!.id,
                    currentDate.toString(),
                    1
                )
            )
            if (isSuccess){
                isOpen = true
                amountDays = 1
            }
            else{
                hint = "Во время открытия больничного произошла ошибка"
                isShowHint = true
            }
        }
    }

    /**
     * Метод для закрытия больничного
     */
    fun medicalDelete(){
        Log.d("DeleteMedical","True")
        viewModelScope.launch {
            val isSuccess = delete.deleteAbsencesEmployeesMedicalByUseId(ProfileCache.profile.userInfo!!.id)
            if(!isSuccess){
                hint = "Во время закрытия больничного произошла ошибка"
                isShowHint = true
            }
            else{
                isOpen = false
                amountDays = 0
            }
        }
    }
}