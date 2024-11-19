package com.example.flyvactions.Views

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
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
import com.example.flyvactions.Models.Cache.ProfileCache
import com.example.flyvactions.R
import com.example.flyvactions.ViewModels.MainViewModel
import com.example.flyvactions.Views.Calendars.CalendarWeekOrMonth
import com.example.flyvactions.Views.SupportingMainViews.AbsencesEmployeesLazyColumn
import com.example.flyvactions.ui.theme.BlueMain
import com.example.flyvactions.ui.theme.ColorTextDark
import com.example.flyvactions.ui.theme.ColorTextLight
import com.example.flyvactions.ui.theme.ColorUnnamedProfile
import com.example.flyvactions.ui.theme.interFontFamily

/**
 * Главная мобильного приложения
 */
@Composable
fun MainScreen(navHostController: NavHostController, viewModel: MainViewModel = viewModel()){
    LaunchedEffect(Unit){
        ProfileCache.profile.userInfo = viewModel.userInfo
        viewModel.isVacationSoon()
    }

    Column(modifier = Modifier.fillMaxSize().background(color = Color.White).padding(start = 25.dp, top = 70.dp, end = 25.dp, bottom = 70.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

       Column(
           verticalArrangement = Arrangement.spacedBy(60.dp)
       ) {
           //Верхний bar с burger и профилем
           Row(modifier = Modifier.fillMaxWidth(),
               horizontalArrangement = Arrangement.SpaceBetween,
               verticalAlignment = Alignment.CenterVertically
           ) {
               Icon(imageVector = ImageVector.vectorResource(id = R.drawable.burger), contentDescription = "",
                   Modifier.size(23.dp), tint = BlueMain)
               Icon(imageVector = ImageVector.vectorResource(id = R.drawable.profile), contentDescription = "",
                   Modifier.size(65.dp), tint = ColorUnnamedProfile)
           }

           //Текущая неделя, месяц, дни
           Column(
               verticalArrangement = Arrangement.spacedBy(10.dp)
           ) {
               Text(text = "Текущая неделя",
                   fontFamily = interFontFamily,
                   fontSize = 32.sp,
                   color = ColorTextDark,
                   fontWeight = FontWeight.Medium,
                   textAlign = TextAlign.Left
               )
               Text(text = "${viewModel.month}, ${viewModel.dayBeginWeek}-${viewModel.dayEndWeek}",
                   fontFamily = interFontFamily,
                   fontSize = 20.sp,
                   color = ColorTextLight,
                   fontWeight = FontWeight.Medium,
                   textAlign = TextAlign.Left,
                   modifier = Modifier.padding(start = 2.dp)
               )
           }

           //Календарь на неделю
           CalendarWeekOrMonth(viewModel.dateBeginWeek, viewModel.dateEndWeek){
               day ->
               if(viewModel.selectedDate == null) {
                   viewModel.selectedDate = day
                   viewModel.fetchEmployeesByDateAbsence(day)

                   if(viewModel.isVacation){
                       viewModel.flagVacationSoon = false
                   }
                   if(viewModel.isVacationEnd){
                       viewModel.flagEndVacation = false
                   }
               }
               else if (viewModel.selectedDate == day){
                   viewModel.selectedDate = null
                   viewModel.clearListAEC()

                   if(viewModel.isVacation){
                       viewModel.flagVacationSoon = true
                   }
                   if(viewModel.isVacationEnd){
                       viewModel.flagEndVacation = true
                   }
               }
               else{
                   viewModel.selectedDate = day
                   viewModel.clearListAEC()
                   viewModel.fetchEmployeesByDateAbsence(day)

                   if(viewModel.isVacation){
                       viewModel.flagVacationSoon = false
                   }
                   if(viewModel.isVacationEnd){
                       viewModel.flagEndVacation = false
                   }
               }
           }

           if(!viewModel.flagAdditionalText){
               if(viewModel.flagLazyColumn){
                   Log.d("listAEC", "${viewModel.listAEC}")
                   //Функция вывода отсутствующих пользователей
                   AbsencesEmployeesLazyColumn(viewModel.listAEC)
               }
               else{
                   //Сообщение о скором отпуске пользователю
                   if(viewModel.flagVacationSoon){
                       Column(
                           horizontalAlignment = Alignment.Start,
                           verticalArrangement = Arrangement.spacedBy(15.dp),
                           modifier = Modifier.padding(top=75.dp)
                       ) {
                           //1 граница
                           Box(
                               modifier = Modifier
                                   .fillMaxWidth()
                                   .height(1.dp)
                                   .background(BlueMain)
                           )

                           //сообщение пользователю
                           Text(text = buildAnnotatedString {
                               withStyle(style = SpanStyle(color = ColorTextDark)) {
                                   append("Через ${viewModel.messageSoonVacation} у вас ")
                               }
                               withStyle(style = SpanStyle(color = BlueMain)) {
                                   append("запланирован отпуск")
                               } },
                               fontSize = 18.sp,
                               fontWeight = FontWeight.Medium,
                               fontFamily = interFontFamily,
                               textAlign = TextAlign.Start
                           )

                           //2 граница
                           Box(
                               modifier = Modifier
                                   .fillMaxWidth()
                                   .height(1.dp)
                                   .background(BlueMain)
                           )
                       }
                   }
                   //Сообщение о конце отпуска
                   if(viewModel.flagEndVacation){
                       Column(
                           horizontalAlignment = Alignment.Start,
                           verticalArrangement = Arrangement.spacedBy(15.dp),
                           modifier = Modifier.padding(top=75.dp)
                       ) {
                           //1 граница
                           Box(
                               modifier = Modifier
                                   .fillMaxWidth()
                                   .height(1.dp)
                                   .background(BlueMain)
                           )

                           //сообщение пользователю
                           Text(text = buildAnnotatedString {
                               withStyle(style = SpanStyle(color = ColorTextDark)) {
                                   append("Сегодя ")
                               }
                               withStyle(style = SpanStyle(color = BlueMain)) {
                                   append("нужно отдыхать. ")
                               }
                               withStyle(style = SpanStyle(color = ColorTextDark)) {
                                   append("Конец только - ${viewModel.messageEndVacation}")}

                               },
                               fontSize = 18.sp,
                               fontWeight = FontWeight.Medium,
                               fontFamily = interFontFamily,
                               textAlign = TextAlign.Start
                           )

                           //2 граница
                           Box(
                               modifier = Modifier
                                   .fillMaxWidth()
                                   .height(1.dp)
                                   .background(BlueMain)
                           )
                       }
                   }
               }

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

