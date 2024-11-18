package com.example.flyvactions.Models.DataBase

import io.github.jan.supabase.createSupabaseClient
import io.github.jan.supabase.gotrue.Auth
import io.github.jan.supabase.gotrue.FlowType
import io.github.jan.supabase.postgrest.Postgrest
import io.github.jan.supabase.realtime.Realtime
import io.github.jan.supabase.storage.Storage


//Объект соединение с Supabase
object SupabaseConnection {
    val supabase = createSupabaseClient(supabaseUrl = "https://lpdnebdhpgflnqtlksnj.supabase.co",
        supabaseKey = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpc3MiOiJzdXBhYmFzZSIsInJlZiI6ImxwZG5lYmRocGdmbG5xdGxrc25qIiwicm9sZSI6ImFub24iLCJpYXQiOjE3Mjk4MzQ2NjIsImV4cCI6MjA0NTQxMDY2Mn0.otQfBHJZ-K6bWOZf5i5eFSxEVsoZtN3jmQj13PFYJp0"
    ){
        install(Postgrest)
        install(Auth){
            flowType = FlowType.PKCE
        }
        install(Realtime)//Дальше это позволит получать уведомления о событиях произошедших с сущностями базы
        install(Storage)
    }
}