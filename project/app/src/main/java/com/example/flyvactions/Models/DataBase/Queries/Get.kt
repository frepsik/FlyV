package com.example.flyvactions.Models.DataBase.Queries

import android.util.Log
import com.example.flyvactions.Models.DataBase.Entities.AbsenceEmployee
import com.example.flyvactions.Models.DataBase.Entities.Employee
import com.example.flyvactions.Models.DataBase.SupabaseConnection
import com.example.flyvactions.Models.convertStringToLocalDate
import com.example.flyvactions.Models.dateBetweenBeginDateAndEndDate
import io.github.jan.supabase.postgrest.from
import java.time.LocalDate


class Get() {
    private val db = SupabaseConnection.supabase


    


    /**
     * Метод получения всех отсутствующих пользователей и причины отсутствия
     */
    suspend fun getAbsenceEmployees() : List<AbsenceEmployee>?{
        var listAbsenceEmployees : List<AbsenceEmployee>?= null
        try {
            listAbsenceEmployees = db
                .from("AbsencesEmployees")
                .select()
                .decodeList<AbsenceEmployee>()
            Log.d("GetAbsenceEmployees", "SuccessFetch")
        }
        catch (e:Exception){
            Log.d("ExceptionGetAbsenceEmployees","${e}")
        }
        return listAbsenceEmployees
    }


    /**
     * Метод предназначенный для получения uuid пользователя и причины отсутствия
     */
    suspend fun getIdAbsenceEmployeesAndReasonByDate(date : LocalDate) : List<List<String>>? {

        var listAbsenceEmployees : List<AbsenceEmployee>?
        var listUuidEmployeesAndUuidReason : List<List<String>>? = null

        try {
            listAbsenceEmployees = getAbsenceEmployees()
            if(listAbsenceEmployees != null){
                listAbsenceEmployees.forEach {
                    if(dateBetweenBeginDateAndEndDate(date, convertStringToLocalDate(it.beginDate),
                            convertStringToLocalDate(it.beginDate).plusDays(it.amountDay.toLong()))
                        ){
                        listUuidEmployeesAndUuidReason!!.plus(listOf(it.employeeId, it.reasonId))
                    }
                }
            }
        }
        catch (e: Exception){
            Log.d("ExceptionGetIdAbsenceEmployeesByDate", "${e}")
        }
        return listUuidEmployeesAndUuidReason
    }


//
//    suspend fun getAbsenceEmployeesByDate(date : LocalDate) : List<Employee>?{
//
//        var listEmployees : List<Employee>? = null
//        val needListIdEmployee : List<String>? = getIdAbsenceEmployeesByDate(date)
//
//        if(!needListIdEmployee.isNullOrEmpty()){
//            try {
//                listEmployees = db.from("Employees").select(){
//                    filter {
//                        eq("id",)
//                    }
//                }.decodeList<Employee>()
//                Log.d("GetAbsenceEmployeesByDate", "SuccessFetch")
//            }
//            catch (e:Exception){
//                Log.d("ExceptionGetAbsenceEmployeesByDate", "${e}")
//            }
//        }
//
//        return listEmployees
//    }
}