package com.example.flyvactions.Models.DataBase.Queries

import android.util.Log
import com.example.flyvactions.Models.DataBase.SupabaseConnection
import io.github.jan.supabase.gotrue.auth
import io.github.jan.supabase.gotrue.providers.builtin.Email
import io.github.jan.supabase.gotrue.user.UserInfo
import io.github.jan.supabase.gotrue.user.UserSession

class Auth {
    private val db = SupabaseConnection.supabase

    /**
     * Функция получения авторизованного пользователя
     */
    fun authorizedUser() : UserInfo?{
        return db.auth.currentUserOrNull()
    }

    /**
     * Функция для восстановления сессии по токену пользователя, что ранее аавторизовывался в системе
     */
    suspend fun restoreSession(accessToken : String) : UserInfo{
        Log.d("AUTH ЗАШЛИ","POBEDANAHUI")
        return db.auth.retrieveUser(accessToken)
    }

    /**
     * Функция проверки на существование сессии
     */
    fun isExistsSession() : Boolean{
        return db.auth.currentSessionOrNull() != null
    }

    /**
     *  Функция получения текущенй сессии пользователя
     */
    fun sessionUser() : UserSession?{
        return db.auth.currentSessionOrNull()
    }


    /**
     * Функция авторизации пользователя по введённым данным
     */
    suspend fun authorization(insertEmail : String, insertPassword : String){
        db.auth.signInWith(Email){
            email = insertEmail
            password = insertPassword
        }
    }

    /**
     * Функция завершения сессии пользователя
     */
    suspend fun logOut(){
        db.auth.signOut()
    }
}