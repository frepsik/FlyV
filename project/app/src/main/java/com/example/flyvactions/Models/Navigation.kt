package com.example.flyvactions.Models

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.flyvactions.Views.LoginScreen
import com.example.flyvactions.Views.MainScreen

//Навигация приложения
@Composable
fun Navigate(){
    val navController:NavHostController = rememberNavController()
    NavHost(navController = navController, startDestination = "loginView"){

        composable(route = "loginView"){
            LoginScreen(navController)
        }

        composable(route = "mainView"){
            MainScreen(navController)
        }
    }
}