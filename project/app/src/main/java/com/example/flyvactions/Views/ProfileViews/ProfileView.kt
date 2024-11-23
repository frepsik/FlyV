package com.example.flyvactions.Views.ProfileViews

import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import com.example.flyvactions.Models.Cache.clearProfileCache
import com.example.flyvactions.Models.isInternetConnection
import com.example.flyvactions.R
import com.example.flyvactions.ViewModels.ProfileViewModels.ProfileViewModel
import com.example.flyvactions.ui.theme.BlueMain
import com.example.flyvactions.ui.theme.ColorBorderData
import com.example.flyvactions.ui.theme.ColorTextDark
import com.example.flyvactions.ui.theme.ColorTextLight
import com.example.flyvactions.ui.theme.interFontFamily
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

/**
 * Окно профиля пользователя
 */
@Composable
fun ProfileScreen(navHostController: NavHostController, viewModel: ProfileViewModel = viewModel()){
    val context = LocalContext.current
    LaunchedEffect(true) {
        delay(800)
        viewModel.isEnabledBack = true
        viewModel.isEnabledExit = true

        viewModel.refreshData()
        Log.d("Update", "Success")
    }

    Box(
        modifier = Modifier.fillMaxSize()
            .pointerInput(Unit){
            detectTapGestures(
                onTap = {
                    viewModel.isShowCardBalanceHoliday = false
                }
            )
        }
    ){
        Column(
            modifier = Modifier.background(Color.White)
                .padding(start = 25.dp, top = 55.dp, end = 25.dp, bottom = 55.dp)
                ,
            verticalArrangement = Arrangement.spacedBy(60.dp)
        ){
            Column(
                verticalArrangement = Arrangement.spacedBy(35.dp)
            ) {
                //Up-bar
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = ImageVector.vectorResource(id = R.drawable.back),
                        contentDescription = "BackMain",
                        tint = ColorTextDark,
                        modifier = Modifier
                            .size(30.dp)
                            .clickable(
                                enabled = viewModel.isEnabledBack
                            ) {
                                navHostController.navigate("mainView"){
                                    popUpTo("profileView"){
                                        inclusive = true
                                    }
                                    launchSingleTop = true
                                }
                                //Чтоб не тыкали тысячу раз во время анимации
                                viewModel.isEnabledBack = false
                                CoroutineScope(Dispatchers.Main).launch {
                                    delay(1000)
                                    viewModel.isEnabledBack = true
                                }
                            }
                    )
                    Text(
                        text = "Выйти",
                        fontSize = 14.sp,
                        fontFamily = interFontFamily,
                        color = ColorTextLight,
                        modifier = Modifier.clickable(
                            enabled = viewModel.isEnabledExit
                        ) {
                            clearProfileCache()
                            navHostController.navigate("loginView"){
                                popUpTo(0)
                            }
                        }
                    )
                }

                //Фотка профиля и имя пользователя
                Column(
                    verticalArrangement = Arrangement.spacedBy(35.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.fillMaxWidth()
                ){
                    AsyncImage(
                        model = viewModel.urlProfileImage,
                        contentDescription = "imageProfile",
                        modifier = Modifier
                            .size(205.dp)
                            .clip(CircleShape)
                            .border(1.dp ,BlueMain, CircleShape)
                    )

                    Text(
                        text = viewModel.nameUser,
                        fontSize = 30.sp,
                        fontWeight = FontWeight.Medium,
                        fontFamily = interFontFamily,
                        color = ColorTextDark
                    )
                }
            }
            Column(
                verticalArrangement = Arrangement.spacedBy(20.dp)
            ) {
                //Верхняя граница
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(2.dp)
                        .background(ColorBorderData)
                )

                //Блок информации
                Column(
                    modifier = Modifier.padding(start = 5.dp, end = 5.dp),
                    verticalArrangement = Arrangement.spacedBy(25.dp)
                ) {
                    //Телефон
                    Column(
                        verticalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        Text(
                            text = "Мобильный телефон",
                            fontSize = 12.sp,
                            fontFamily = interFontFamily,
                            fontWeight = FontWeight.Medium,
                            color = ColorTextDark
                        )
                        Text(
                            text = viewModel.numberPhone,
                            fontSize = 12.sp,
                            fontFamily = interFontFamily,
                            color = ColorTextLight
                        )
                    }

                    //Email
                    Column(
                        verticalArrangement = Arrangement.spacedBy(5.dp)
                    ) {
                        Text(
                            text = "Email",
                            fontSize = 12.sp,
                            fontFamily = interFontFamily,
                            fontWeight = FontWeight.Medium,
                            color = ColorTextDark
                        )
                        Text(
                            text = viewModel.email,
                            fontSize = 12.sp,
                            fontFamily = interFontFamily,
                            color = ColorTextLight
                        )
                    }

                    //Город
                    Column(
                        verticalArrangement = Arrangement.spacedBy(5.dp)
                    ) {
                        Text(
                            text = "Город проживания",
                            fontSize = 12.sp,
                            fontFamily = interFontFamily,
                            fontWeight = FontWeight.Medium,
                            color = ColorTextDark
                        )
                        Text(
                            text = viewModel.city,
                            fontSize = 12.sp,
                            fontFamily = interFontFamily,
                            color = ColorTextLight
                        )
                    }

                    //Редактирование и баланс отдыха
                    Row(
                        modifier = Modifier.padding(top = 10.dp).fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ){
                        Text(
                            text = "Редактировать профиль",
                            fontSize = 12.sp,
                            color = BlueMain,
                            fontFamily = interFontFamily,
                            fontWeight = FontWeight.Medium,
                            modifier = Modifier.clickable{
                                navHostController.navigate("editProfileView")
                                viewModel.isShowCardBalanceHoliday = false
                            }
                        )

                        //Баланс отдыха
                        Row(
                            modifier = Modifier.clickable{
                                if(isInternetConnection(context = context)){
                                    viewModel.isShowCardBalanceHoliday = !viewModel.isShowCardBalanceHoliday
                                }
                                else{
                                    Toast.makeText(context, "Проблемы с интернетом. Восстановите соединение", Toast.LENGTH_SHORT).show()
                                }
                               
                            },
                            horizontalArrangement = Arrangement.spacedBy(5.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ){
                            Text(
                                text = "Баланс отдыха",
                                fontSize = 12.sp,
                                color = BlueMain,
                                fontFamily = interFontFamily,
                                fontWeight = FontWeight.Medium
                            )
                            Icon(
                                imageVector = ImageVector.vectorResource(id = R.drawable.balance),
                                contentDescription = "balanceRest",
                                tint = BlueMain
                            )
                        }
                    }
                }


                //Нижняя граница
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(2.dp)
                        .background(ColorBorderData)
                )
            }
        }
        Box(
            modifier = Modifier.zIndex(1f).align(Alignment.BottomCenter).padding(bottom = 145.dp)
        ){
            if(viewModel.isShowCardBalanceHoliday){
                //Карточка
                BalanceHolidayCardScreen()
            }
        }
    }
}
