package com.example.flyvactions.Models.DataBase.Queries

import android.util.Log
import com.example.flyvactions.Models.DataBase.Entities.AbsenceEmployee
import com.example.flyvactions.Models.DataBase.Entities.Employee
import com.example.flyvactions.Models.DataBase.Entities.ReasonAbsence
import com.example.flyvactions.Models.DataBase.SupabaseConnection
import com.example.flyvactions.Models.DataClasses.AbsenceEmployeeCalendar
import com.example.flyvactions.Models.WorkWithString.convertStringToLocalDate
import com.example.flyvactions.Models.WorkWithString.parseFullNameEmployee
import com.example.flyvactions.Models.dateBetweenBeginDateAndEndDate
import io.github.jan.supabase.postgrest.from
import java.time.LocalDate


class Get() {
    private val db = SupabaseConnection.supabase

    /**
     * Метод для получения пользователя по uuid
     */
    suspend fun getEmployeeById(uuid : String) : Employee?{
        var employee : Employee? = null

        try {

            employee = db
                .from("Employees")
                .select(){
                    filter {
                        eq("id", uuid)
                    }
                }.decodeSingle<Employee>()

            Log.d("GetEmployeeById", "SuccessFetch")
        }
        catch (e:Exception){
            Log.d("ExceptionGetEmployeeById", "$e")
        }

        return employee
    }


    /**
     * Меттод для получения причины отсутствия по uuid
     */
    suspend fun getReasonById(uuid : String) : ReasonAbsence?{
        var reason : ReasonAbsence? = null

        try {

            reason = db
                .from("ReasonsAbsences")
                .select(){
                    filter {
                        eq("id", uuid)
                    }
                }
                .decodeSingle<ReasonAbsence>()

            Log.d("GetReasonById", "SuccessFetch")
        }
        catch (e: Exception){
            Log.d("ExceptionGetReasonById", "$e")
        }
        return reason
    }


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
    suspend fun getIdAbsenceEmployeesAndReasonByDate(date : LocalDate) : List<List<String>> {

        val listAbsenceEmployees : List<AbsenceEmployee>?
        val listUuidEmployeesAndUuidReason : MutableList<List<String>> = mutableListOf()

        try {
            listAbsenceEmployees = getAbsenceEmployees()
            Log.d("ListAE", "${listAbsenceEmployees}")
            if(listAbsenceEmployees != null){
                listAbsenceEmployees.forEach {
                    if(dateBetweenBeginDateAndEndDate(date, convertStringToLocalDate(it.beginDate),
                            convertStringToLocalDate(it.beginDate).plusDays(it.amountDay.toLong()-1))
                        ){
                        listUuidEmployeesAndUuidReason.add(listOf(it.employeeId, it.reasonId))
                    }
                }
            }
            Log.d("GetIdAbsenceEmployeesByDate", "SuccessFetch")
        }
        catch (e: Exception){
            Log.d("ExceptionGetIdAbsenceEmployeesByDate", "${e}")
        }
        return listUuidEmployeesAndUuidReason
    }


    /**
     * Метод позволяющий получить список отсутствующих и причину под вывод в рамках требования приложения
     */
    suspend fun getAbsenceEmployeesCalendarByDate(date : LocalDate) : MutableList<AbsenceEmployeeCalendar>{

        var listAEC : MutableList<AbsenceEmployeeCalendar> = mutableListOf()
        val needUuidEmployeeAndReason : List<List<String>>? = getIdAbsenceEmployeesAndReasonByDate(date)
        Log.d("ListUuidEA", "${needUuidEmployeeAndReason}")
        if(!needUuidEmployeeAndReason.isNullOrEmpty()){

            try {

                needUuidEmployeeAndReason.forEach{
                    val employee : Employee ? = getEmployeeById(it[0])
                    val reasonAbsence : ReasonAbsence ? = getReasonById(it[1])
                    if(employee!=null && reasonAbsence!=null){
                        if(employee.urlPhotoProfile==null){ employee.urlPhotoProfile = "https://lpdnebdhpgflnqtlksnj.supabase.co/storage/v1/object/public/photosProfileUsers/UnnamedProfile.jpg"}
                        listAEC.add(AbsenceEmployeeCalendar(parseFullNameEmployee(employee.fullName), employee.urlPhotoProfile, reasonAbsence.reason))
                        Log.d("ListER","NotEmpty")
                    }
                    else{
                        Log.d("ListER","Empty")
                        listAEC = mutableListOf()
                        return listAEC
                    }
                }

                Log.d("GetAbsenceEmployeesCalendarByDate", "SuccessFetch")
            }
            catch (e:Exception){
                Log.d("ExceptionGetAbsenceEmployeesCalendarByDate", "${e}")
            }
        }

        return listAEC
    }
}