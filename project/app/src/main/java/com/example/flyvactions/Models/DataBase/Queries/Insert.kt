package com.example.flyvactions.Models.DataBase.Queries

import android.util.Log
import com.example.flyvactions.Models.DataBase.Entities.AbsenceEmployee
import com.example.flyvactions.Models.DataBase.SupabaseConnection
import io.github.jan.supabase.postgrest.from

/**
 * Класс содержащий запросы на добавление данных в базу
 */
class Insert {
    private val db = SupabaseConnection.supabase

    /**
     * Запрос для добавления данных в сущность AbsencesEmployees
     */
    suspend fun insertAbsencesEmployees(objectAbsenceEmployee : AbsenceEmployee) : Boolean{
        var isSuccess = true
        try {
            db
                .from("AbsencesEmployees")
                .insert(objectAbsenceEmployee)
            Log.d("InsertAbsencesEmployees", "InsertSuccess")
        }
        catch (e : Exception){
            isSuccess = false
            Log.d("ExceptionInsertAbsencesEmployees", "$e")
        }
        return isSuccess
    }
}