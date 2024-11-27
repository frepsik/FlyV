package com.example.flyvactions.Models.DataBase.Queries

import android.util.Log
import com.example.flyvactions.Models.DataBase.Entities.AbsenceEmployee
import com.example.flyvactions.Models.DataBase.Entities.City
import com.example.flyvactions.Models.DataBase.Entities.Employee
import com.example.flyvactions.Models.DataBase.Entities.ReasonAbsence
import com.example.flyvactions.Models.DataBase.Entities.Substitution
import com.example.flyvactions.Models.DataBase.SupabaseConnection
import com.example.flyvactions.Models.DataClasses.AbsenceEmployeeCalendar
import com.example.flyvactions.Models.WorkWithStringAndDate.convertStringToLocalDate
import com.example.flyvactions.Models.WorkWithStringAndDate.parseFullNameEmployee
import com.example.flyvactions.Models.dateBetweenBeginDateAndEndDate
import io.github.jan.supabase.postgrest.from
import java.time.LocalDate

/**
 * Класс содержащий запросы на получение данных из базы
 */
class Get() {
    private val db = SupabaseConnection.supabase

    /**
     * Запрос на получение отсутствий пользователя
     */
    suspend fun getAbsencesEmployeesByUserId(idUser : String) : List<AbsenceEmployee> {
        var listAbsencesEmployees : List<AbsenceEmployee> = listOf()
        try {
            listAbsencesEmployees = db
                .from("AbsencesEmployees")
                .select {
                    filter {
                        eq("employee_id", idUser)
                    }
                }
                .decodeList<AbsenceEmployee>()
            Log.d("GetAbsencesEmployeesByUserId", "FetchSuccess")
        }
        catch (e : Exception){
            Log.d("ExceptionGetAbsencesEmployeesByUserId", "$e")
        }
        return listAbsencesEmployees
    }

    /**
     * Запрос на получение тех кого подменяет конкретный пользователь
     */
    suspend fun getSubstitutions() : List<Substitution>{
        var substitution : List<Substitution> = listOf()
        try {
            substitution = db
                .from("Substitutions")
                .select()
                .decodeList<Substitution>()
            Log.d("GetSubstitutions", "FetchSuccess")
        }
        catch (e: Exception){
            Log.d("ExceptionGetSubstitutions", "$e")
        }
        return substitution
    }

    /**
     * Запрос на получение uuid причины отсутствия по названию
     */
    suspend fun getReasonAbsenceByName(name: String) : ReasonAbsence?{
        var uuidReason : ReasonAbsence? = null
        try {
            uuidReason = db
                .from("ReasonsAbsences")
                .select{
                    filter {
                        eq("reason", name)
                    }
                }
                .decodeSingle<ReasonAbsence>()
            Log.d("GetReasonAbsenceByName","FetchSuccess")
        }
        catch (e: Exception){
            Log.d("ExceptionGetReasonAbsenceByName", "$e")
        }
        return uuidReason
    }


    /**
     * Запрос на получение отсутствий пользователя по его uuid и uuid причины
     */
    suspend fun getAbsencesEmployeesByIdUserAndReasonId(idUser : String, idReason: String) : List<AbsenceEmployee>{
        var listAbsenceEmployee = listOf<AbsenceEmployee>()
        try {
            listAbsenceEmployee = db
                .from("AbsencesEmployees")
                .select(){
                    filter {
                        eq("reason_id", idReason)
                        eq("employee_id", idUser)
                    }
                }
                .decodeList<AbsenceEmployee>()
            Log.d("GetAbsencesEmployeesByIdUserAndReasonId", "SuccessFetch")
        }
        catch (e : Exception){
            Log.d("ExceptionGetAbsencesEmployeesByIdUserAndAbsencesId", "$e")
        }
        return listAbsenceEmployee
    }


    /**
     * Запрос на получение всех городов
     */
    suspend fun getCityList() : MutableList<City> {
        var listCity = listOf<City>()
        var mutableListCity = mutableListOf<City>()
        try {
            listCity = db.from("Cities").select().decodeList<City>()
            mutableListCity = listCity.toMutableList()
            Log.d("GetCityList", "SuccessFetch")
        }
        catch (e: Exception){
            Log.d("ExceptionGetCityList", "${e}")
        }
        return mutableListCity
    }

    /**
     * Запрос на получение названия города по uuid
     */
    suspend fun getCityById(uuid : String) : City?{

        var city : City? = null
        try {
            city = db
                .from("Cities")
                .select(){
                    filter {
                        eq("id", uuid)
                    }
                }.decodeSingle()
            Log.d("GetCityById", "SuccessFetch")
        }
        catch (e : Exception){
            Log.d("ExceptionGetCityById", "$e")
        }

        return city
    }


    /**
     * Запрос для получения пользователя по uuid
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

            employee.urlPhotoProfile ?:"https://lpdnebdhpgflnqtlksnj.supabase.co/storage/v1/object/public/photosProfileUsers/UnnamedProfile.jpg"
            Log.d("GetEmployeeById", "SuccessFetch")
        }
        catch (e:Exception){
            Log.d("ExceptionGetEmployeeById", "$e")
        }

        return employee
    }

    /**
     * Запрос для получения причины отсутствия по uuid
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
     * Запрос для получения пользователей отсутствующих по причине отпуска
     */
    suspend fun getEmployeeByVacation() : List<AbsenceEmployee>{
        var employeeByVacation : List<AbsenceEmployee> = listOf()

        try {
            employeeByVacation = db
                .from("AbsencesEmployees").select() {
                    filter {
                        eq("reason_id", "336e77aa-2a3b-425a-85fe-76e5f76ab0ac")
                    }
                }
                .decodeList<AbsenceEmployee>()
            Log.d("GetEmployeeByVacation", "SuccessFetch")
        }
        catch (e: Exception){
            Log.d("ExceptionGetEmployeeByVacation", "$e")
        }

        return employeeByVacation
    }


    /**
     * Запрос для получения всех отсутствующих пользователей и причины отсутствия
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
     * Запрос предназначенный для получения uuid пользователя и причины о-тсутствия
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
     * Запрос позволяющий получить список отсутствующих и причину под вывод в рамках требования приложения
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