package com.example.flyvactions.Models.DataBase.Queries

import com.example.flyvactions.Models.DataBase.SupabaseConnection
import io.github.jan.supabase.gotrue.auth
import io.github.jan.supabase.gotrue.providers.builtin.Email
import io.github.jan.supabase.postgrest.from

class Get() {
    private val db = SupabaseConnection.supabase

    //Авторизация пользователя по введённым данным
    suspend fun authorization(insertEmail : String, insertPassword : String){
        db.auth.signInWith(Email){
            email = insertEmail
            password = insertPassword
        }
    }

}