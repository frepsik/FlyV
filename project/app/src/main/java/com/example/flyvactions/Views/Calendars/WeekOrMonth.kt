package com.example.flyvactions.Views.Calendars

import android.widget.Toast
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
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.flyvactions.Models.isInternetConnection
import com.example.flyvactions.ui.theme.BlueMain
import com.example.flyvactions.ui.theme.ColorBorderData
import com.example.flyvactions.ui.theme.GreenMain
import com.example.flyvactions.ui.theme.interFontFamily
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.format.TextStyle
import java.util.Locale


/**
 * Календарь на месяц или неделю
 * (в рамках контекста моего программного продукта, можно установить определённый диапазон, который захочется)
 */
@Composable
fun CalendarWeekOrMonth(
    beginDate : LocalDate,
    endDate : LocalDate,
    selectedDate : MutableState<LocalDate?>,
    dateSelectedCallback : (LocalDate) -> Unit
){
    val selectedDateInFunc = remember { mutableStateOf<LocalDate?>(null) }
    val amountDaysInCalendar : Int = (endDate.dayOfYear - beginDate.dayOfYear) + 1
    val isEnabledButton = remember { mutableStateOf(true) }
    val context = LocalContext.current


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

            val isSelected = when(currentDate){
                selectedDateInFunc.value -> true
                else -> false
            }

            Button(
                onClick = {
                    //Интернет
                    if(isInternetConnection(context)){
                        if(selectedDateInFunc.value != currentDate ){
                            selectedDate.value = currentDate
                            selectedDateInFunc.value = currentDate
                            dateSelectedCallback(currentDate)
                        }
                        else{
                            selectedDate.value = null
                            selectedDateInFunc.value = null
                            dateSelectedCallback(currentDate)
                        }

                        //Ограничение, чтобы бесперерывно на кнопку не жали, потому что логика работы нажатия сломается и количество запросов больно сильно повысится
                        isEnabledButton.value = false
                        CoroutineScope(Dispatchers.Main).launch {
                            delay(500L) // Задержка в миллисекундах
                            isEnabledButton.value = true
                        }
                    }
                    else{
                        Toast.makeText(context, "Проблемы с интернетом. Восстановите соединение", Toast.LENGTH_SHORT).show()
                    }
                },
                modifier = Modifier.border(
                    width = if(isSelected) { 0.dp } else { 1.dp },
                    color =  if(isSelected) { Color.Transparent } else { ColorBorderData },
                    shape = RoundedCornerShape(8.dp)
                ).height(74.dp).width(67.dp),
                shape = RoundedCornerShape(8.dp),
                colors = ButtonColors(
                    containerColor = if(isSelected) { GreenMain } else { Color.White },
                    contentColor = if(isSelected) { Color.White } else { BlueMain },
                    disabledContainerColor = if(isSelected) { GreenMain } else { Color.White },
                    disabledContentColor = if(isSelected) { Color.White } else { BlueMain }
                ),
                contentPadding = PaddingValues(0.dp),
                enabled = isEnabledButton.value
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