package com.example.flyvactions.Models

import android.util.Log
import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.material3.DrawerState
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.flyvactions.Models.Cache.ProfileCache
import com.example.flyvactions.Views.Burger.DrawerContent
import com.example.flyvactions.Views.Burger.DrawerViews.BusinessTripScreen
import com.example.flyvactions.Views.Burger.DrawerViews.CalendarScreen
import com.example.flyvactions.Views.Burger.DrawerViews.DaysOffScreen
import com.example.flyvactions.Views.Burger.DrawerViews.MedicalScreen
import com.example.flyvactions.Views.Burger.DrawerViews.VacationScreen
import com.example.flyvactions.Views.LoginScreen
import com.example.flyvactions.Views.MainScreen
import com.example.flyvactions.Views.ProfileViews.EditProfileScreen
import com.example.flyvactions.Views.ProfileViews.ProfileScreen
import io.github.jan.supabase.gotrue.user.UserInfo
import kotlinx.coroutines.CoroutineScope

//Навигация приложения
@Composable
fun Navigate(){

    val user : UserInfo? = ProfileCache.profile.userInfo

    Log.d("GetUserAfterExistsApp","${user}")
    val startDestination : String = if(user!= null) {"mainView"}  else {"loginView"}

    val navController:NavHostController = rememberNavController()
    val coroutineScope : CoroutineScope = rememberCoroutineScope()
    val drawerState : DrawerState = rememberDrawerState(initialValue = DrawerValue.Closed)

    val currentBackStackEntry = navController.currentBackStackEntryAsState()
    val currentRoute = currentBackStackEntry.value?.destination?.route

    //Бургре меню
    ModalNavigationDrawer(
        drawerState = drawerState,
        gesturesEnabled = currentRoute!="loginView" && currentRoute != "profileView" && currentRoute!= "editProfileView",
        drawerContent = {
            DrawerContent(
                navController,
                coroutineScope,
                drawerState
            )
        }
    ){
        //Вся структура по возможным маршрутам
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
                MainScreen(navController, drawerState, coroutineScope)
            }

            //Здесь ещё в аргументе передаем необходимое значение,
            // чтобы определить с какого окна приходим и отобразить нужную анимацию
            composable(
                route = "profileView",

                //Срабатывает когда я на окно попадаю через navigate("окно") (то есть с окна mainView, editProfileView (уже не уверен, странно работает))
                enterTransition = {
                    fadeIn(animationSpec = tween(600)) + slideIntoContainer(
                        AnimatedContentTransitionScope.SlideDirection.Left,
                        tween(600))
                },
                //Срабатывает когда я через navigate перехожу на другое окно
                exitTransition = {
                    fadeOut(animationSpec = tween(600)) + slideOutOfContainer(
                        AnimatedContentTransitionScope.SlideDirection.Left,
                        tween(600)
                    )
                },
                //Срабатывает когда я выхожу с окна использовав popBackStack (свайп или кнопка на сенсоре телефона)
                popExitTransition = {
                    fadeOut(animationSpec = tween(600)) + slideOutOfContainer(
                        AnimatedContentTransitionScope.SlideDirection.Right,
                        tween(600)
                    )
                },
                popEnterTransition = {
                    fadeIn(animationSpec = tween(600)) + slideIntoContainer(
                        AnimatedContentTransitionScope.SlideDirection.Right,
                        tween(600))
                }
            ){
                ProfileScreen(navController)
            }

            composable(
                "editProfileView",
                //Срабатывает когда я на окно попадаю через navigate("окно") (то есть с окна profileView)
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
                EditProfileScreen(navController)
            }

            //Календарь
            composable(
                route = "calendarView",

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
            )
            { CalendarScreen(navController) }

            //Отпуск
            composable(
                route = "vacationView",
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
            ) { VacationScreen(navController) }

            //Отгул
            composable(
                route = "daysOffView",
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
            ){ DaysOffScreen(navController) }



            //Больничный
            composable(
                route = "medicalView",
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
            ){ MedicalScreen(navController) }

            //Командировка
            composable(
                route = "businessTripView",
                //Срабатывает когда я на окно попадаю через navigate("окно") (то есть с окна profileView)
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
            ){ BusinessTripScreen(navController) }

        }
    }

}