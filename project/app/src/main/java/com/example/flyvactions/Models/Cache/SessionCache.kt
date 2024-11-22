package com.example.flyvactions.Models.Cache

import android.content.Context
import android.util.Log
import com.example.flyvactions.Models.DataBase.Queries.Auth
import kotlinx.coroutines.runBlocking


/**
 * Функция удаления SharedPreferences файла
 */
fun deleteSharedPreferencesByName(context: Context, nameFile : String){
    context.deleteSharedPreferences(nameFile)
}

/**
 * Функция сохранения сессии пользователя
 */
fun saveSession(auth: Auth, context: Context){
    val userSession = auth.sessionUser()

    //SharedPreferences - инструмент для создания, получения и хранения файлов формата: ключ, значение в памяти мобильного телефона
    val sharedPreferences = context.getSharedPreferences("auth", Context.MODE_PRIVATE)
    //with вместо постоянного обращения к sharedPreferences.edit().
    with(sharedPreferences.edit()){
        putString("accessToken",userSession?.accessToken) //Сохраняем токен доступа к запросам на сервер (к API)
        putString("refreshToken",userSession?.refreshToken) //Сохраняем токен обновления (можем пересоздать accessToken)
        apply() //Асинхронно сохраняем
    }
    Log.d("SavedTokens","POBEDANAHUI")
}



/**
 * Функция восстановления сессии пользователя
 */
fun recoverySession(auth: Auth, context: Context) : Boolean{
    val sharedPreferences = context.getSharedPreferences("auth", Context.MODE_PRIVATE)
    val accessToken: String? = sharedPreferences.getString("accessToken",null) //Второй парметр значнеие по умолчанию, вернётся если значение по ключу не будет найдено
    val refreshToken: String? = sharedPreferences.getString("refreshToken",null)
    if(!accessToken.isNullOrEmpty() and !refreshToken.isNullOrEmpty()){
        runBlocking {
            try{
                ProfileCache.profile.userInfo  = auth.restoreSession(accessToken!!)
                Log.d("ЕБАНЫЙ ЮЗЕР","${ProfileCache.profile.userInfo }}")
            }
            catch (e: Exception){
                Log.d("Произошла ошибка при получении данных","xz")
            }
        }
        return true
    }
    return false
}

/**
 * Функция сохранения начала времени, от которого была завершена работа программы
 */
fun saveBackgroundTime(backgroundTime : Long, context: Context){
    val sharedPreferences = context.getSharedPreferences("startTime",Context.MODE_PRIVATE)
    sharedPreferences.edit().putString("startTimeBackground",backgroundTime.toString()).apply()
    Log.d("SavedTime","POBEDANAHUI")
}

/**
 * Функция восстановления времени, что было сохранено
 */
fun recoveryTime(context: Context) : Long?{
    val sharedPreferences = context.getSharedPreferences("startTime", Context.MODE_PRIVATE)
    val time : Long? = sharedPreferences.getString("startTimeBackground", null)?.toLongOrNull()
    return time
}