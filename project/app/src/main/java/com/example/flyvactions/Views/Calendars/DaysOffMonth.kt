package com.example.flyvactions.Views.Calendars

import android.util.Log
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
import com.example.flyvactions.ui.theme.ColorTextLight
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
 * Календарь для оформления отгула
 */
@Composable
fun CalendarForDaysOff(
    beginDate : LocalDate,
    endDate : LocalDate,
    selectedDate : MutableState<LocalDate?>,
    daysOff : List<LocalDate>,
    dateSelectedCallback : (LocalDate?) -> Unit
){
    val amountDaysInCalendar : Int = (endDate.dayOfYear - beginDate.dayOfYear) + 1
    val isEnabledButton = remember { mutableStateOf(true) }
    val context = LocalContext.current

    Column(
        verticalArrangement = Arrangement.spacedBy(20.dp)
    ){
        Text(
            text = if(selectedDate.value == null) "Выберите дату:" else "",
            fontFamily = interFontFamily,
            fontSize = 16.sp,
            color = ColorTextLight
        )
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

                //Эта прекрасная переменная для того, чтобы если выбранная дата совпадала с currentDate,
                // то отрисовывалась оконтовка синяя, в общем индексы шляпа
                // (а эта переменная каждый раз отрабатывает, когда я нажимаю на кнопку, ведь экран перерисовывется)
                val isSelected = when (currentDate) {
                    selectedDate.value -> true
                    else -> false
                }

                //Устанавливаем уже запланированные отгулы (если такие есть)
                var isPlanned = false
                for(it in daysOff){
                    if(currentDate == it){
                        isPlanned = true
                        break
                    }
                }

                Button(
                    onClick = {
                        //Интернет
                        if(isInternetConnection(context)){
                            if(isPlanned){
                                Toast.makeText(context, "Дата уже запланирована, выберите другую", Toast.LENGTH_SHORT).show()
                            }
                            else{
                                if(selectedDate.value == null){
                                    selectedDate.value = currentDate
                                    dateSelectedCallback(currentDate)
                                    Log.d("SelectedDate", "$currentDate")
                                }
                                else if(selectedDate.value == currentDate){
                                    selectedDate.value = null
                                    dateSelectedCallback(selectedDate.value)
                                    Log.d("SelectedDate", "${selectedDate.value}")
                                }
                                else{
                                    selectedDate.value = currentDate
                                    dateSelectedCallback(currentDate)
                                    Log.d("SelectedDate", "$currentDate")
                                }

                            }
                            //Ограничение, чтобы бесперерывно на кнопку не жали, потому что логика работы нажатия сломается и количество запросов больно сильно повысится
                            isEnabledButton.value = false
                            CoroutineScope(Dispatchers.Main).launch {
                                delay(300L) // Задержка в миллисекундах
                                isEnabledButton.value = true
                            }
                        }
                        else{
                            Toast.makeText(context, "Проблемы с интернетом. Восстановите соединение", Toast.LENGTH_SHORT).show()
                        }
                    },
                    modifier = Modifier
                        .border(
                            width = if (isPlanned) {
                                0.dp
                            } else if (isSelected) {
                                1.dp
                            } else {
                                1.dp
                            },
                            color = if (isPlanned) {
                                Color.Transparent
                            } else if (isSelected) {
                                BlueMain
                            } else {
                                ColorBorderData
                            },
                            shape = RoundedCornerShape(8.dp)
                        )
                        .height(74.dp)
                        .width(67.dp),
                    shape = RoundedCornerShape(8.dp),
                    colors = ButtonColors(
                        containerColor = if(isPlanned) { GreenMain} else if(isSelected) { Color.White } else { Color.White },
                        contentColor = if(isPlanned) { Color.White } else if(isSelected) { BlueMain } else { BlueMain },
                        disabledContainerColor =  if(isPlanned) { GreenMain} else if(isSelected) { Color.White } else { Color.White },
                        disabledContentColor = if(isPlanned) { Color.White } else if(isSelected) { BlueMain } else { BlueMain }
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
}