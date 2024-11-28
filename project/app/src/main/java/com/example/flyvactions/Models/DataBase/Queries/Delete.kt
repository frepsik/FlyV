package com.example.flyvactions.Models.DataBase.Queries

import android.util.Log
import com.example.flyvactions.Models.DataBase.SupabaseConnection
import io.github.jan.supabase.postgrest.from

/**
 * Класс, предназначенный для хранения запросов на удаление записей из базы
 */
class Delete {
    private val db = SupabaseConnection.supabase

    /**
     * Запрос на удаления больничного по пользователю
     */
    suspend fun deleteAbsencesEmployeesMedicalByUseId(idUser : String) : Boolean{
        var isSuccess = true
        try {
            db.from("AbsencesEmployees").delete{
                filter {
                    eq("employee_id",idUser)
                    eq("reason_id", "a02fb81d-b9e2-4079-b1ee-ba0798959127")
                }
            }
            Log.d("DeleteAbsencesEmployeesMedicalByUseId", "SuccessDelete")
        }
        catch (e: Exception){
            Log.d("ExceptionDeleteAbsencesEmployeesMedicalByUseId", "$e")
            isSuccess = false
        }
        return isSuccess
    }
}