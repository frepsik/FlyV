package com.example.flyvactions.Views.SupportingMainViews

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.flyvactions.Models.DataClasses.AbsenceEmployeeCalendar
import com.example.flyvactions.ui.theme.BlueMain
import com.example.flyvactions.ui.theme.ColorBorderData
import com.example.flyvactions.ui.theme.ColorTextDark
import com.example.flyvactions.ui.theme.interFontFamily

/**
 * LazyColumn, что выводит определённым образом список отсуттвующих пользователей
 */
@Composable
fun AbsencesEmployeesLazyColumn(absencesEmployees : MutableList<AbsenceEmployeeCalendar>){

    LazyColumn(
        verticalArrangement = Arrangement.spacedBy(28.dp),
        horizontalAlignment = Alignment.Start
    ) {
        items(absencesEmployees){
            item ->
            Column(
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Row(
                    modifier = Modifier.padding(start = 3.dp),
                    horizontalArrangement = Arrangement.spacedBy(15.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {

                    //Фото профиля
                    AsyncImage(
                        model = item.urlPhotoProfile,
                        contentDescription = "iconProfile",
                        modifier = Modifier
                            .size(55.dp)
                            .clip(CircleShape)
                            .border(1.dp, BlueMain, CircleShape)

                    )

                    //Разделение
                    Box(
                        modifier = Modifier
                            .height(55.dp)
                            .width(6.dp)
                            .clip(RoundedCornerShape(2.dp))
                            .background(BlueMain)
                    )

                    //ФИО - причина
                    Column(
                        horizontalAlignment = Alignment.Start,
                        verticalArrangement = Arrangement.Center
                    ){
                        Text(
                            text = item.fullName,
                            fontFamily = interFontFamily,
                            color = ColorTextDark,
                            textAlign = TextAlign.Start,
                            fontWeight = FontWeight.Medium
                        )
                        Text(text = buildAnnotatedString {
                            withStyle(style = SpanStyle(color = ColorTextDark)) {
                                append("Причина отсутствия - ")
                            }
                            withStyle(style = SpanStyle(color = BlueMain)) {
                                append(item.reasonAbsence.lowercase())
                            }},
                            fontFamily = interFontFamily,
                            textAlign = TextAlign.Start,
                            fontWeight = FontWeight.Medium
                        )
                    }
                }
                //Нижняя граница
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(1.dp)
                        .background(ColorBorderData)
                )
            }
        }
    }
}