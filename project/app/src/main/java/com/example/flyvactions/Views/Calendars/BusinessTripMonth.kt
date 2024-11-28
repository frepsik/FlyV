package com.example.flyvactions.Views.Calendars

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.flyvactions.ui.theme.BlueMain
import com.example.flyvactions.ui.theme.ColorBorderData
import com.example.flyvactions.ui.theme.GreenMain
import com.example.flyvactions.ui.theme.interFontFamily
import java.time.LocalDate
import java.time.format.TextStyle
import java.util.Locale

/**
 * Календарь для отображения дней командировки
 */
@Composable
fun CalendarForBusinessTrip(
    beginDate : LocalDate,
    endDate : LocalDate,
    firstAndLastDaysBusinessTrip : List<List<LocalDate>>
){
    val amountDaysInCalendar : Int = (endDate.dayOfYear - beginDate.dayOfYear) + 1
    LazyRow(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(28.dp)
    ) {
        items(count = amountDaysInCalendar){ index ->

            val currentDate = beginDate.plusDays(index.toLong())
            val dayMonth = currentDate.dayOfMonth
            val dayOfWeek = currentDate
                .dayOfWeek
                .getDisplayName(
                    TextStyle.SHORT,
                    Locale("ru")
                ).replaceFirstChar { it.uppercase() }

            var isPlanned = false
            for (it in firstAndLastDaysBusinessTrip){
                // Проверка, запланирована ли дата
                if(currentDate in it[0]..it[1]){
                    isPlanned = true
                    break
                }
            }
            Button(
                onClick = {},
                modifier = Modifier.border(
                    width = if(isPlanned) {0.dp}  else { 1.dp },
                    color =  if(isPlanned) {Color.Transparent}  else { ColorBorderData },
                    shape = RoundedCornerShape(8.dp)
                ).height(74.dp).width(67.dp),
                shape = RoundedCornerShape(8.dp),
                colors = ButtonColors(
                    containerColor = if(isPlanned) { GreenMain} else { Color.White },
                    contentColor = if(isPlanned) { Color.White } else { BlueMain },
                    disabledContainerColor =  if(isPlanned) { GreenMain}  else { Color.White },
                    disabledContentColor = if(isPlanned) { Color.White } else { BlueMain }
                ),
                contentPadding = PaddingValues(0.dp),
                enabled = false
            ){
                //День месяц
                Column(
                    verticalArrangement = Arrangement.spacedBy(1.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(text = "$dayMonth",
                        fontFamily = interFontFamily,
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Medium
                    )
                    Text(text = dayOfWeek,
                        fontFamily = interFontFamily,
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    }
}