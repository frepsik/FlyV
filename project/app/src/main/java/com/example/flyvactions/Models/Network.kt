package com.example.flyvactions.Models

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities

//Проверяет, есть ли соединение с интернетом
fun isInternetConnection(context: Context) : Boolean{

    //Получает менеджер по соединениею
    val connectivityManager  = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager //as приведение к типу (используем потому что getSystemService возвращет тип Any?)

    //Проверяем есть ли активное соединение (включен WiFi или мобильные данные)
    val activeNetwork = connectivityManager.activeNetwork ?: return false //Если выражение слева null, возвращается false

    //Получаем все характеристики по активной сети (В данных характеристиках содержится информация о том, есть ли интернет соединение)
    val networkCapabilities = connectivityManager.getNetworkCapabilities(activeNetwork) ?: return false //Вернёт null, в том случае, если какие то технические проблемы с сетью (не может поулчить характеристик), или отсуствует активное соединение

    //Проверяем, поддерживается ли соединение с интернетом у данной сети
    return networkCapabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
}