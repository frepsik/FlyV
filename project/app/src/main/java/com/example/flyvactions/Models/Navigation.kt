package com.example.flyvactions.Models

import android.util.Log
import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.flyvactions.Models.Cache.ProfileCache
import com.example.flyvactions.Views.LoginScreen
import com.example.flyvactions.Views.MainScreen
import com.example.flyvactions.Views.ProfileScreen
import io.github.jan.supabase.gotrue.user.UserInfo

//Навигация приложения
@Composable
fun Navigate(){

    val user : UserInfo? = ProfileCache.profile?.userInfo

    Log.d("GetUserAfterExistsApp","${user}")
    val startDestination : String = if(user!= null) {"mainView"}  else {"loginView"}

    val navController:NavHostController = rememberNavController()
    NavHost(navController = navController, startDestination = startDestination){

        composable(route = "loginView"){
            LoginScreen(navController)
        }

        composable(
            route = "mainView",
            //Срабатывает, когда я нажимаю на кнопку профиля пользователя (navigate отрабатывает в этот момент)
            exitTransition = {
                fadeOut(animationSpec = tween(1500)) + slideOutOfContainer(
                    AnimatedContentTransitionScope.SlideDirection.Start,
                    tween(1500)
                )
            }
        ){
            MainScreen(navController)
        }

        composable(
            route = "profileView",
            //Срабатывает когда я на окно попадаю через navigate("окно") (то есть с окна mainView)
            enterTransition = {
                fadeIn(animationSpec = tween(600)) + slideIntoContainer(
                    AnimatedContentTransitionScope.SlideDirection.Left,
                    tween(600))
            },
            //Срабатывает когда я через navigate перехожу на другое окно
            exitTransition = {
                fadeOut(animationSpec = tween(600)) + slideOutOfContainer(
                    AnimatedContentTransitionScope.SlideDirection.Right,
                    tween(600)
                )
            },
            //Срабатывает когда я выхожу с окна использовав popBackStack (свайп или кнопка на сенсоре телефона)
            popExitTransition = {
                fadeOut(animationSpec = tween(600)) + slideOutOfContainer(
                    AnimatedContentTransitionScope.SlideDirection.Right,
                    tween(600)
                )
            }
        ){
            ProfileScreen(navController)
        }
    }
}