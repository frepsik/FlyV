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
 * Календарь для оформления отпуска
 */
@Composable
fun CalendarForVacation(
    beginDate : LocalDate,
    endDate : LocalDate,
    firstAndLastDaysVacation : List<List<LocalDate>>,
    resetState: MutableState<Boolean>,
    datesSelectedCallback : (LocalDate?, LocalDate?) -> Unit
){
    val firstSelectedDayInCalendar = remember { mutableStateOf<LocalDate?>(null) }
    val secondSelectedDayInCalendar = remember { mutableStateOf<LocalDate?>(null) }

    val toggledButtonIndexFirst = remember { mutableIntStateOf(-1) }
    val toggledButtonIndexSecond = remember { mutableIntStateOf(-1) }
    val amountDaysInCalendar : Int = (endDate.dayOfYear - beginDate.dayOfYear) + 1
    val toggleButtonFirst = remember { mutableStateOf(false) }
    val toggleButtonSecond = remember { mutableStateOf(false) }
    val lastButtonIndexFirst = remember { mutableIntStateOf(-1) }
    val lastButtonIndexSecond = remember { mutableIntStateOf(-1) }
    val isEnabledButton = remember { mutableStateOf(true) }
    val context = LocalContext.current

    //Сбрасываем данные (ещё можно переписать это всё дело в теории и сделать прям по датам, закидывать их, но я чёто не хочу и времени не много, но на заметочку, что так можно сделать)
    if(resetState.value){
        firstSelectedDayInCalendar.value = null
        secondSelectedDayInCalendar.value = null

        toggledButtonIndexFirst.intValue = -1
        toggledButtonIndexSecond.intValue = -1
        toggleButtonFirst.value = false
        toggleButtonSecond.value = false
        lastButtonIndexFirst.intValue = -1
        lastButtonIndexSecond.intValue = -1
        resetState.value = false
    }
    Column(
        verticalArrangement = Arrangement.spacedBy(20.dp)
    ){
        Text(
            text = if(secondSelectedDayInCalendar.value == null || firstSelectedDayInCalendar.value == null) "Выберите диапазон:" else "",
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
                    firstSelectedDayInCalendar.value -> true
                    secondSelectedDayInCalendar.value -> true
                    else -> false
                }


                var isPlanned = false
                for (it in firstAndLastDaysVacation){
                    // Проверка, запланирована ли дата
                    if(currentDate in it[0]..it[1]){
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
                                if(firstSelectedDayInCalendar.value == null){
                                    toggledButtonIndexFirst.intValue = index
                                    lastButtonIndexFirst.intValue = index
                                    toggleButtonFirst.value = true
                                    firstSelectedDayInCalendar.value = currentDate

                                    Log.d("InstallFirstDate","Success")

                                    datesSelectedCallback(null, null)
                                }
                                //При условии, что первый день выбран, второй день не выбран, и выбираемое второе значение больше даты первого выбранного
                                else if(firstSelectedDayInCalendar.value != null && secondSelectedDayInCalendar.value == null && currentDate > firstSelectedDayInCalendar.value){
                                    toggledButtonIndexSecond.intValue = index
                                    lastButtonIndexSecond.intValue = index
                                    toggleButtonSecond.value = true
                                    secondSelectedDayInCalendar.value = currentDate

                                    //Присвавием в другие переменные для безопасности (не по ссылке), чтобы если вдруг значения где то там обновятся по ссылке
                                    val firstDateVacation = firstSelectedDayInCalendar.value!!
                                    val lastDateVacation = secondSelectedDayInCalendar.value!!

                                    Log.d("InstallSecondDate","Success")

                                    datesSelectedCallback(firstDateVacation, lastDateVacation)
                                }
                                //Могу отменить первую кнопку
                                else if(firstSelectedDayInCalendar.value != null && lastButtonIndexFirst.intValue==index && secondSelectedDayInCalendar.value==null){
                                    firstSelectedDayInCalendar.value = null
                                    toggledButtonIndexFirst.intValue = -1
                                    toggleButtonFirst.value = false

                                    Log.d("DeleteFirstDate","Success")

                                    datesSelectedCallback(null, null)
                                }
                                //Могу отменить вторую кнопку
                                else if(secondSelectedDayInCalendar.value != null && lastButtonIndexSecond.intValue==index){
                                    secondSelectedDayInCalendar.value = null
                                    toggledButtonIndexSecond.intValue = -1
                                    toggleButtonSecond.value = false

                                    Log.d("DeleteSecondDate","Success")

                                    datesSelectedCallback(null, null)
                                }
                                //Сбрасываю выбранные значения и начинаю с нового выбранного, если пользователь выбирает любую другую дату кроме последней
                                else{
                                    toggledButtonIndexFirst.intValue = index
                                    lastButtonIndexFirst.intValue = index
                                    toggleButtonFirst.value = true
                                    firstSelectedDayInCalendar.value = currentDate

                                    secondSelectedDayInCalendar.value = null
                                    toggledButtonIndexSecond.intValue = -1
                                    toggleButtonSecond.value = false

                                    Log.d("ResetDates","Success")

                                    datesSelectedCallback(null, null)
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
                    modifier = Modifier.border(
                        width = if(isPlanned) {0.dp} else if(isSelected) { 1.dp } else { 1.dp },
                        color =  if(isPlanned) {Color.Transparent} else if(isSelected) { BlueMain } else { ColorBorderData },
                        shape = RoundedCornerShape(8.dp)
                    ).height(74.dp).width(67.dp),
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