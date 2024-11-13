package com.example.flyvactions.Models.SLCT

import android.content.Context
import android.util.Log
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import com.example.flyvactions.Models.Cache.ProfileCache
import com.example.flyvactions.Models.Cache.deleteSharedPreferencesByName
import com.example.flyvactions.Models.Cache.recoverySession
import com.example.flyvactions.Models.Cache.recoveryTime
import com.example.flyvactions.Models.Cache.saveBackgroundTime
import com.example.flyvactions.Models.Cache.saveSession
import com.example.flyvactions.Models.DataBase.Queries.Auth

/**
 * Класс, предназначенный для отслеживания жизненного цикла сессии пользователя
 */
class SessionLifeCycleTracking(private val context: Context) : DefaultLifecycleObserver {

    private var backgroundTime : Long? = if(recoveryTime(context)==null){-1} else{ recoveryTime(context) }
    private var sessionTimeOut : Long = 1 * 60 * 1000L
    private val auth : Auth = Auth()
    private var flag : Boolean = false

    /**
     * Функция реализуется в момент включаения приложения
     */
    override fun onStart(owner: LifecycleOwner) {

        if(System.currentTimeMillis() - backgroundTime!! > sessionTimeOut && backgroundTime!! > -1){
            Log.d("Завершение существовашей сессии","то что надо")
            deleteSharedPreferencesByName(context, "auth")
            deleteSharedPreferencesByName(context, "startTime")
        }
        else if( recoverySession(auth, context) ){
            Log.d("Восстановление сессии","то что надо")
            Log.d("USER", "$${ProfileCache.profile.userInfo}  ${ProfileCache.profile}")
            flag = true
        }
        else{
            Log.d("Создание сессии","то что надо")
            flag = true
        }

    }

    /**
     * Функция реализуется в момент завершения работы программы
     */
    override fun onStop(owner: LifecycleOwner) {

        if(flag){
            backgroundTime = System.currentTimeMillis()
            saveBackgroundTime(backgroundTime!!, context)
            saveSession(auth, context)
            Log.d("Сохранение данных", "Те что надо")
        }
    }

}