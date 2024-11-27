package com.example.flyvactions.Views.Burger

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.DrawerState
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.flyvactions.Models.isInternetConnection
import com.example.flyvactions.ui.theme.BlueMain
import com.example.flyvactions.ui.theme.interFontFamily
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

/**
 * Содержимое бургер-меню
 */
@Composable
fun DrawerContent(
    navController: NavHostController,
    coroutineScope: CoroutineScope,
    drawerState: DrawerState
){
    val context = LocalContext.current
    ModalDrawerSheet(
        modifier = Modifier.width(290.dp),
        drawerShape = RoundedCornerShape(5.dp)
    ) {
        Column(
            modifier = Modifier.fillMaxSize()
        ) {
            //Шапка
            Box(
                modifier = Modifier
                    .background(BlueMain)
                    .fillMaxWidth()
                    .height(155.dp),
                contentAlignment = Alignment.Center

            ){
                Text(
                    text = "FlyV",
                    fontFamily = interFontFamily,
                    textAlign = TextAlign.Center,
                    color = Color.White,
                    fontSize = 62.sp
                )
            }
            HorizontalDivider()

            Spacer(modifier = Modifier.height(50.dp))
            //Странички, на которые можно перейти
            Column(
               modifier = Modifier.padding(start = 5.dp)
            ) {
                //Календарь
                NavigationDrawerItem(
                    label = {
                        Text(
                            text = "Календарь",
                            fontFamily = interFontFamily,
                            fontSize = 18.sp,
                            color = BlueMain
                        )
                    },
                    selected = false,
                    onClick = {
                        coroutineScope.launch {
                            drawerState.close()
                        }
                        if(!isInternetConnection(context)){
                            Toast.makeText(context, "Проблемы с интернетом. Восстановите соединение", Toast.LENGTH_SHORT).show()
                        }
                        else{
                            navController.navigate("calendarView"){
                                popUpTo("mainView")
                            }
                        }

                    }
                )
                HorizontalDivider()
                //Отпуск
                NavigationDrawerItem(
                    label = {
                        Text(
                            text = "Отпуск",
                            fontFamily = interFontFamily,
                            fontSize = 18.sp,
                            color = BlueMain
                        )
                    },
                    selected = false,
                    onClick = {
                        coroutineScope.launch {
                            drawerState.close()
                        }
                        if(!isInternetConnection(context)){
                            Toast.makeText(context, "Проблемы с интернетом. Восстановите соединение", Toast.LENGTH_SHORT).show()
                        }
                        else{
                            navController.navigate("vacationView"){
                                popUpTo("mainView")
                            }
                        }
                    }
                )
                //Отгул
                NavigationDrawerItem(
                    label = {
                        Text(
                            text = "Отгул",
                            fontFamily = interFontFamily,
                            fontSize = 18.sp,
                            color = BlueMain
                        )
                    },
                    selected = false,
                    onClick = {
                        coroutineScope.launch {
                            drawerState.close()
                        }
                        if(!isInternetConnection(context)){
                            Toast.makeText(context, "Проблемы с интернетом. Восстановите соединение", Toast.LENGTH_SHORT).show()
                        }
                        else{
                            navController.navigate("daysOffView"){
                                popUpTo("mainView")
                            }
                        }

                    }
                )
                //Больничный
                NavigationDrawerItem(
                    label = {
                        Text(
                            text = "Больничный",
                            fontFamily = interFontFamily,
                            fontSize = 18.sp,
                            color = BlueMain
                        )
                    },
                    selected = false,
                    onClick = {
                        coroutineScope.launch {
                            drawerState.close()
                        }
                        if(!isInternetConnection(context)){
                            Toast.makeText(context, "Проблемы с интернетом. Восстановите соединение", Toast.LENGTH_SHORT).show()
                        }
                        else{
                            navController.navigate("medicalView"){
                                popUpTo("mainView")
                            }
                        }
                    }
                )


               HorizontalDivider()

               //Командировка
               NavigationDrawerItem(
                   label = {
                       Text(
                           text = "Командировка",
                           fontFamily = interFontFamily,
                           fontSize = 18.sp,
                           color = BlueMain
                       )
                   },
                   selected = false,
                   onClick = {
                       coroutineScope.launch {
                           drawerState.close()
                       }
                       if(!isInternetConnection(context)){
                           Toast.makeText(context, "Проблемы с интернетом. Восстановите соединение", Toast.LENGTH_SHORT).show()
                       }
                       else{
                           navController.navigate("businessTripView"){
                               popUpTo("mainView")
                           }
                       }
                   }
               )
            }
            //Сделал так, чтобы прижалось вниз
            Spacer(modifier = Modifier.weight(1f))
            //Нижний блок
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp)
                    .background(BlueMain)
            )
        }
    }
}