package com.example.flyvactions.Models.DataBase.Queries

import android.util.Log
import com.example.flyvactions.Models.DataBase.Entities.Employee
import com.example.flyvactions.Models.DataBase.SupabaseConnection
import io.github.jan.supabase.postgrest.from

/**
 * Класс содержащий запросы на обновление данных в базе
 */
class Update {
    private val db = SupabaseConnection.supabase

    /**
     * Запрос на обновление статуса работника
     */
    suspend fun updateIsWorksByUserId(idUser : String, isWorks : Boolean){
        try {
            db.from("Employees")
                .update(
                    {
                        set("is_works", isWorks)
                    }
                ){
                    filter {
                        eq("id", idUser)
                    }
                }
            Log.d("UpdateIsWorksByUserId", "SuccessUpdate")
        }
        catch (e: Exception){
            Log.d("ExceptionUpdateIsWorksByUserId", "$e")
        }
    }

    /**
     * Запрос на обновление количества отпускных дней пользователя
     */
    suspend fun updateDaysVacationsByUserId(idUser : String, daysVacations : Int){
        try {
            db.from("Employees")
                .update(
                    {
                        set("days_vacations", daysVacations)
                    }
                ){
                    filter {
                        eq("id", idUser)
                    }
                }
            Log.d("UpdateDaysVacationsByUserId", "SuccessUpdate")
        }
        catch (e: Exception){
            Log.d("ExceptionUpdateDaysVacationsByUserId", "$e")
        }
    }

    /**
     * Запрос на обновление пользователской почтоы сотрудника компании по его уникальному uuid
     */
    suspend fun updateUserEmailByEmployeeId(uuid:String, email: String) : Employee?{
        var updateEmployee : Employee? = null
        try{
            updateEmployee = db
                .from("Employees")
                .update(
                    {
                        set("email_personal", email)
                    }
                ){
                    select()
                    filter {
                        eq("id", uuid)
                    }
                }.decodeSingle<Employee>()

            Log.d("UpdateUserEmailByEmployeeId", "UpdateSuccess")
            Log.d("NewRecord", "${updateEmployee}")
        }
        catch (e:Exception){
            Log.d("ExceptionUpdateUserEmailByEmployeeId", "$e")
        }
        return updateEmployee
    }


    /**
     * Запрос на обновление номера телефона сотрудника компании по его уникальному uuid
     */
    suspend fun updateUserNumberPhoneByEmployeeId(uuid:String, numberPhone: String) : Employee?{
        var updateEmployee : Employee? = null
        try{
            updateEmployee = db
                .from("Employees")
                .update(
                    {
                        set("number_phone", numberPhone)
                    }
                ){
                    select()
                    filter {
                        eq("id", uuid)
                    }
                }.decodeSingle<Employee>()

            Log.d("UpdateUserNumberPhoneByEmployeeId", "UpdateSuccess")
            Log.d("NewRecord", "${updateEmployee}")
        }
        catch (e:Exception){
            Log.d("ExceptionUpdateUserNumberPhoneByEmployeeId", "$e")
        }
        return updateEmployee
    }

    /**
     * Запрос на обновление города сотрудника компании по его уникальному uuid
     */
    suspend fun updateUserCityByEmployeeId(uuid:String, idCity: String) : Employee?{
        var updateEmployee : Employee? = null
        try{
            updateEmployee = db
                .from("Employees")
                .update(
                    {
                        set("city_id", idCity)
                    }
                ){
                    select()
                    filter {
                        eq("id", uuid)
                    }
                }.decodeSingle<Employee>()

            Log.d("UpdateUserCityByEmployeeId", "UpdateSuccess")
            Log.d("NewRecord", "${updateEmployee}")
        }
        catch (e:Exception){
            Log.d("ExceptionUpdateUserCityByEmployeeId", "$e")
        }
        return updateEmployee
    }
}