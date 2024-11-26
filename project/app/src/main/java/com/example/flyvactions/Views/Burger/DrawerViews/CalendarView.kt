package com.example.flyvactions.Views.Burger.DrawerViews

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.example.flyvactions.R
import com.example.flyvactions.ViewModels.DrawerViewModels.CalendarViewModel
import com.example.flyvactions.Views.Calendars.CalendarWeekOrMonth
import com.example.flyvactions.Views.SupportingMainViews.AbsencesEmployeesLazyColumn
import com.example.flyvactions.ui.theme.BlueMain
import com.example.flyvactions.ui.theme.ColorBorderData
import com.example.flyvactions.ui.theme.ColorTextDark
import com.example.flyvactions.ui.theme.ColorTextLight
import com.example.flyvactions.ui.theme.interFontFamily

/**
 * Окно, где можно посмотреть в календаре от текущего месяца по последующие причины отсутствия сотрудников компании
 */
@Composable
fun CalendarScreen(navHostController: NavHostController, viewModel: CalendarViewModel = viewModel()){
    val context = LocalContext.current
    Column(
        modifier = Modifier
            .background(Color.White)
            .fillMaxSize()
            .padding(start = 25.dp, top = 70.dp, end = 25.dp, bottom = 70.dp)
    ) {
        Column(
            verticalArrangement = Arrangement.spacedBy(60.dp)
        ){
            //Иконка назад
            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ){
                Icon(
                    imageVector = ImageVector.vectorResource(id = R.drawable.back),
                    contentDescription = "BackMain",
                    tint = ColorTextDark,
                    modifier = Modifier
                        .size(30.dp)
                        .clickable {
                            navHostController.navigate("mainView"){
                                popUpTo(0){
                                    inclusive = true
                                }
                                launchSingleTop = true
                            }

                        }
                )
            }
            //Текущий месяц, год, кнопки смены и определения сегодняшнего дня
            Column(
                verticalArrangement = Arrangement.spacedBy(30.dp)
            ) {
                Text(text = viewModel.monthAndYear,
                    fontFamily = interFontFamily,
                    fontSize = 32.sp,
                    color = ColorTextDark,
                    fontWeight = FontWeight.Medium,
                    textAlign = TextAlign.Left
                )
                Row(
                    modifier = Modifier.width(235.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween

                ) {
                    //Назад
                    Button(
                        onClick = {
                            viewModel.prevMonth()
                        },
                        modifier = Modifier.border(
                            width =  1.dp,
                            color = ColorBorderData,
                            shape = RoundedCornerShape(8.dp)
                        ).height(41.dp).width(44.dp),
                        shape = RoundedCornerShape(8.dp),
                        colors = ButtonColors(
                            containerColor =  Color.White,
                            contentColor = Color.Transparent,
                            disabledContainerColor = Color.White ,
                            disabledContentColor = Color.Transparent
                        ),
                        contentPadding = PaddingValues(0.dp)
                    ) {
                        Icon(
                            imageVector = ImageVector.vectorResource(id = R.drawable.prev),
                            contentDescription = "prev",
                            tint = ColorTextLight
                        )
                    }

                    //Вперёд
                    Button(
                        onClick = {
                            viewModel.nextMonth()
                        },
                        modifier = Modifier.border(
                            width =  1.dp,
                            color = ColorBorderData,
                            shape = RoundedCornerShape(8.dp)
                        ).height(41.dp).width(44.dp),
                        shape = RoundedCornerShape(8.dp),
                        colors = ButtonColors(
                            containerColor =  Color.White,
                            contentColor = Color.Transparent,
                            disabledContainerColor = Color.White ,
                            disabledContentColor = Color.Transparent
                        ),
                        contentPadding = PaddingValues(0.dp)
                    ) {
                        Icon(
                            imageVector = ImageVector.vectorResource(id = R.drawable.next),
                            contentDescription = "prev",
                            tint = ColorTextLight
                        )
                    }

                    Box(
                        modifier = Modifier.border(
                                width =  1.dp,
                                color = BlueMain,
                                shape = RoundedCornerShape(8.dp)
                            )
                            .height(41.dp)
                            .width(100.dp)
                    ){
                        Text(
                            text = viewModel.currentDateOutput,
                            fontFamily = interFontFamily,
                            fontSize = 22.sp,
                            textAlign = TextAlign.Center,
                            color = ColorTextLight,
                            modifier = Modifier.align(Alignment.Center)
                        )
                    }

                }
            }

            //Календарь на месяц
            CalendarWeekOrMonth(viewModel.beginDayMonth, viewModel.endDayMonth, viewModel.selectedDate){
                    day ->
                Log.d("DateCallback", "${viewModel.selectedDate}")
                if(viewModel.selectedDate.value==null){
                    viewModel.clearListAEC()
                    viewModel.isData = false
                }
                else{
                    viewModel.clearListAEC()
                    viewModel.fetchEmployeesByDateAbsence()
                }
            }

            //Проверяем, нужно ли вообще хоть что то выводить и была ли выбрана дата
            if(viewModel.isData){
                if(viewModel.flagLazyColumn){
                    Log.d("listAEC Srabotal", "${viewModel.listAEC}")
                    //Функция вывода отсутствующих пользователей
                    AbsencesEmployeesLazyColumn(viewModel.listAEC)
                }
                else{
                    Text(text = buildAnnotatedString {
                        withStyle(style = SpanStyle(color = ColorTextDark)) {
                            append("Все сотрудники ")
                        }
                        withStyle(style = SpanStyle(color = BlueMain)) {
                            append("на рабочем месте")
                        } },
                        fontSize = 20.sp,
                        fontFamily = interFontFamily,
                        fontWeight = FontWeight.Medium,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.padding(top=77.dp)
                    )
                }
            }
        }
    }
}