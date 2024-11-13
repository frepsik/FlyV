package com.example.flyvactions

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.example.flyvactions.Models.Navigate
import com.example.flyvactions.Models.SLCT.SessionLifeCycleTracking


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()


        val sessionTracking = SessionLifeCycleTracking(this) //Создаём класс, что реализует функции, в момент запуска программы и завершения работы
        lifecycle.addObserver(sessionTracking) //Подписываемся на изменения

        setContent {
            Navigate()
        }
    }
}

