package com.example.flyvactions.Views.ProfileViews

import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.flyvactions.Models.Cache.ProfileCache
import com.example.flyvactions.Models.isInternetConnection
import com.example.flyvactions.ViewModels.ProfileViewModels.BalanceHolidayCardViewModel
import com.example.flyvactions.ui.theme.ColorBackground
import com.example.flyvactions.ui.theme.ColorTextDark
import com.example.flyvactions.ui.theme.ColorTextLight
import com.example.flyvactions.ui.theme.interFontFamily


/**
 * Карточка баланса отдыха
 */
@Composable
fun BalanceHolidayCardScreen(viewModel: BalanceHolidayCardViewModel = viewModel()){
    val context = LocalContext.current
    if(!isInternetConnection(context = context)){
        Toast.makeText(context, "Проблемы с интернетом. Восстановите соединение", Toast.LENGTH_SHORT).show()
    }
    else{
        LaunchedEffect(Unit) {
            //Получаем значения в карточку
            viewModel.fetchAbsencesEmployee()
            viewModel.daysVacation = ProfileCache.profile.daysVacation
            viewModel.daysOff = ProfileCache.profile.daysOff
        }
    }


    Column(
        modifier = Modifier
            .background(ColorBackground, RoundedCornerShape(8.dp))
            .height(245.dp).width(305.dp)
            .padding(start = 12.dp, top = 5.dp, bottom = 5.dp, end = 12.dp),
        verticalArrangement = Arrangement.Center
    ) {
        Column(
            verticalArrangement = Arrangement.spacedBy(13.dp)
        ) {
            //Ежегодный отпуск
            Column(
                verticalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                Text(
                    text = "ЕЖЕГОДНЫЙ ОТПУСК",
                    fontSize = 10.sp,
                    fontFamily = interFontFamily,
                    color = ColorTextDark,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = buildAnnotatedString {
                        withStyle(style = SpanStyle(color = ColorTextLight)) {
                            append("Можешь запланировать: ")
                        }
                        withStyle(style = SpanStyle(color = ColorTextDark, fontWeight = FontWeight.Bold)) {
                            append(
                                if(viewModel.daysVacation == 0) { "-" }
                                else { "${viewModel.daysVacation} дн." }
                            )
                        }
                    },
                    fontSize = 15.sp,
                    fontFamily = interFontFamily
                )
                Text(
                    text = buildAnnotatedString {
                        withStyle(style = SpanStyle(color = ColorTextLight)){
                            append("Уже запланировано: ")
                        }
                        withStyle(style = SpanStyle(color = ColorTextDark, fontWeight = FontWeight.Bold)){
                            append(
                                if(viewModel.daysVacationPlanned == 0) { "-" }
                                else{ "${viewModel.daysVacationPlanned} дн." }
                            )
                        }
                    },
                    fontSize = 15.sp,
                    fontFamily = interFontFamily
                )
            }

            // Граница
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(2.dp)
                    .background(Color.White)
            )

            //Отпуск за стаж
            Column(
                verticalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                Text(
                    text = "ОТПУСК ЗА СТАЖ",
                    fontSize = 10.sp,
                    fontFamily = interFontFamily,
                    color = ColorTextDark,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = buildAnnotatedString {
                        withStyle(style = SpanStyle(color = ColorTextLight)){
                            append("Можешь запланировать: ")
                        }
                        withStyle(style = SpanStyle(color = ColorTextDark, fontWeight = FontWeight.Bold)){
                            append(
                                if(viewModel.daysVacationsForExperience == 0) { "-" }
                                else{ "${viewModel.daysVacationsForExperience} дн."}
                            )
                        }
                    },
                    fontSize = 15.sp,
                    fontFamily = interFontFamily
                )
                Text(
                    text = buildAnnotatedString {
                        withStyle(style = SpanStyle(color = ColorTextLight)){
                            append("Уже запланировано: ")
                        }
                        withStyle(style = SpanStyle(color = ColorTextDark, fontWeight = FontWeight.Bold)){
                            append(
                                if(viewModel.daysVacationsForExperiencePlanned == 0) { "-" }
                                else {"${viewModel.daysVacationsForExperiencePlanned} дн."}
                            )
                        }
                    },
                    fontSize = 15.sp,
                    fontFamily = interFontFamily
                )
            }
            // Граница
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(2.dp)
                    .background(Color.White)
            )
            //Отгулы
            Column(
                verticalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                Text(
                    text = "ОТГУЛЫ",
                    fontSize = 10.sp,
                    fontWeight = FontWeight.Bold,
                    fontFamily = interFontFamily,
                    color = ColorTextDark
                )
                Text(
                    text = buildAnnotatedString {
                        withStyle(style = SpanStyle(color = ColorTextLight)){
                            append("Текущий баланс: ")
                        }
                        withStyle(style = SpanStyle(color = ColorTextDark, fontWeight = FontWeight.Bold)){
                            append(
                                if(viewModel.daysOff == 0){ "-" }
                                else{ "${viewModel.daysOff} дн."}
                            )
                        }
                    },
                    fontSize = 15.sp,
                    fontFamily = interFontFamily
                )
            }
        }
    }
}