package com.example.flyvactions.Views.Burger.DrawerViews

import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
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
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.example.flyvactions.Models.isInternetConnection
import com.example.flyvactions.R
import com.example.flyvactions.ViewModels.DrawerViewModels.VacationVIewModel
import com.example.flyvactions.Views.Calendars.CalendarForVacation
import com.example.flyvactions.Views.Calendars.CalendarWeekOrMonth
import com.example.flyvactions.Views.ProfileViews.BalanceHolidayCardScreen
import com.example.flyvactions.Views.SupportingMainViews.AbsencesEmployeesLazyColumn
import com.example.flyvactions.ui.theme.BlueMain
import com.example.flyvactions.ui.theme.ColorBackgroundButton
import com.example.flyvactions.ui.theme.ColorBorderData
import com.example.flyvactions.ui.theme.ColorTextDark
import com.example.flyvactions.ui.theme.ColorTextLight
import com.example.flyvactions.ui.theme.interFontFamily
import java.time.LocalDate

/**
 * Окно где пользователь может оформить отпуск
 */
@Composable
fun VacationScreen(navHostController: NavHostController, viewModel: VacationVIewModel = viewModel()){
    val context = LocalContext.current
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
            modifier = Modifier
                .background(Color.White)
                .fillMaxSize()
                .padding(start = 25.dp, top = 70.dp, end = 25.dp, bottom = 70.dp),
        ) {
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
            Spacer(modifier = Modifier.height(60.dp))
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
                    modifier = Modifier.width(280.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween

                ) {
                    //Назад
                    Button(
                        onClick = {
                            viewModel.prevMonth()
                            viewModel.selectedDate.value = null
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
                            viewModel.selectedDate.value = null
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
                            fontSize = 16.sp,
                            color = BlueMain,
                            fontFamily = interFontFamily,
                            fontWeight = FontWeight.Medium
                        )
                        Icon(
                            imageVector = ImageVector.vectorResource(id = R.drawable.balance),
                            contentDescription = "balanceRest",
                            modifier = Modifier.size(15.dp),
                            tint = BlueMain
                        )
                    }
                }
            }
            Spacer(modifier = Modifier.height(40.dp))
            Column(
                verticalArrangement = Arrangement.spacedBy(20.dp)
            ) {
                //Здесь будет календарь
                CalendarForVacation(viewModel.beginDayMonth, viewModel.endDayMonth, LocalDate.of(2024, 11, 25),  LocalDate.of(2024, 11, 30),){
                        firstDayVacation, lastDayVacation ->
                    viewModel.isEnabledPlanned = !(firstDayVacation == null || lastDayVacation == null)
                    Log.d("RangeDates" ,"${firstDayVacation} - ${lastDayVacation}")
                }
            }
            Spacer(modifier = Modifier.height(190.dp))
            Box(
                modifier = Modifier.fillMaxWidth(),
                contentAlignment = Alignment.Center
            ){
                Button(
                    onClick = {

                    },
                    enabled = viewModel.isEnabledPlanned,
                    shape = RoundedCornerShape(8.dp),
                    colors = ButtonColors(
                        containerColor = BlueMain,
                        contentColor = Color.White,
                        disabledContainerColor = ColorBackgroundButton,
                        disabledContentColor = ColorTextDark
                    ),
                    modifier = Modifier.height(52.dp).width(215.dp)
                ) {
                    Text(
                        text = "Запланировать",
                        fontSize = 20.sp,
                        fontFamily = interFontFamily
                    )
                }
            }
        }
        Box(
            modifier = Modifier
                .zIndex(1f)
                .align(Alignment.Center)
                .padding(top=20.dp)
        ){
            if(viewModel.isShowCardBalanceHoliday){
                BalanceHolidayCardScreen()
            }
        }
    }

}