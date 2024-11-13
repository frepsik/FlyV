package com.example.flyvactions.Models

import android.util.Log
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.flyvactions.Models.Cache.ProfileCache
import com.example.flyvactions.Views.LoginScreen
import com.example.flyvactions.Views.MainScreen
import io.github.jan.supabase.gotrue.user.UserInfo

//Навигация приложения
@Composable
fun Navigate(){

    val user : UserInfo? = ProfileCache.profile?.userInfo

    Log.d("ЕБУЧЕЕ СОЕДИНЕНИЕ","${user}")
    val startDestination : String = if(user!= null) {"mainView"}  else {"loginView"}

    val navController:NavHostController = rememberNavController()
    NavHost(navController = navController, startDestination = startDestination){

        composable(route = "loginView"){
            LoginScreen(navController)
        }

        composable(route = "mainView"){
            MainScreen(navController)
        }
    }
}