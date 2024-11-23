package com.example.flyvactions.Views.ProfileViews

import android.os.Handler
import android.os.Looper
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.example.flyvactions.Models.isInternetConnection
import com.example.flyvactions.R
import com.example.flyvactions.ViewModels.ProfileViewModels.EditProfileViewModel
import com.example.flyvactions.ui.theme.BlueMain
import com.example.flyvactions.ui.theme.ColorBackgroundButton
import com.example.flyvactions.ui.theme.ColorTextDark
import com.example.flyvactions.ui.theme.ColorTextLight
import com.example.flyvactions.ui.theme.interFontFamily
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


/**
 * Окно редактирования пользовательских данных
 */
@Composable
fun EditProfileScreen(navHostController: NavHostController, viewModel : EditProfileViewModel = viewModel()){
    val context = LocalContext.current
    val focusManager = LocalFocusManager.current

    val flagLoading = remember{ mutableStateOf(false) }
    val lastClickTime = remember { mutableLongStateOf(0L) }

    LaunchedEffect(Unit) {
        viewModel.fetchListCity()
    }
    Column(
        modifier = Modifier.fillMaxSize().background(Color.White)
            .padding(start = 25.dp, top = 55.dp, end = 25.dp, bottom = 55.dp)
            .pointerInput(Unit){
                detectTapGestures(
                    onTap = {
                        focusManager.clearFocus()
                    }
                )
            },
        verticalArrangement = Arrangement.spacedBy(65.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ){
        //Иконка и текст
       Column(
           verticalArrangement = Arrangement.spacedBy(50.dp)
       ) {
           //Иконка назад
           Row(
               horizontalArrangement = Arrangement.SpaceBetween,
               verticalAlignment = Alignment.CenterVertically
           ){
               Icon(
                   imageVector = ImageVector.vectorResource(id = R.drawable.back),
                   contentDescription = "BackMain",
                   tint = ColorTextDark,
                   modifier = Modifier
                       .size(30.dp)
                       .clickable(
                           enabled = viewModel.isEnabledBack
                       ) {
                           navHostController.navigate("profileView"){
                               popUpTo("editProfileView"){
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
           }

           Text(
               text = "Редактирование профиля",
               fontSize = 32.sp,
               fontFamily = interFontFamily,
               fontWeight = FontWeight.Medium,
               textAlign = TextAlign.Center,
               color = BlueMain
           )
       }
        //Поля ввода
        Column(
            verticalArrangement = Arrangement.spacedBy(35.dp)
        ) {

            //Номер телефона
            OutlinedTextField(
                value = viewModel.numberPhone,
                onValueChange = {viewModel.numberPhone = it},
                textStyle = TextStyle(
                    fontSize = 16.sp,
                    fontFamily = interFontFamily,
                    color = ColorTextDark
                ),
                label = {
                    Text(
                        text = "Номер",
                        color = ColorTextLight
                    )
                },
                shape = RoundedCornerShape(8.dp),
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = Color.Transparent,
                    unfocusedContainerColor = Color.Transparent,
                    focusedIndicatorColor = BlueMain,
                    unfocusedIndicatorColor = BlueMain
                ),
                //Когда будет срабатывать
                keyboardOptions = KeyboardOptions.Default.copy(
                    imeAction = ImeAction.Next //При нажатии на кнопку дальше
                ),
                //Что будет срабатывать
                keyboardActions = KeyboardActions(
                    onNext = { focusManager.moveFocus(FocusDirection.Down) } //Смена фокуса на нижнее поле
                )
            )

            //Почта
            OutlinedTextField(
                value = viewModel.email,
                onValueChange = {viewModel.email = it},
                textStyle = TextStyle(
                    fontSize = 16.sp,
                    fontFamily = interFontFamily,
                    color = ColorTextDark
                ),
                label = {
                    Text(
                        text = "Email",
                        color = ColorTextLight
                    )
                },
                shape = RoundedCornerShape(8.dp),
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = Color.Transparent,
                    unfocusedContainerColor = Color.Transparent,
                    focusedIndicatorColor = BlueMain,
                    unfocusedIndicatorColor = BlueMain
                ),
                //Когда будет срабатывать
                keyboardOptions = KeyboardOptions.Default.copy(
                    imeAction = ImeAction.Next //При нажатии на кнопку дальше
                ),
                //Что будет срабатывать
                keyboardActions = KeyboardActions(
                    onNext = { focusManager.moveFocus(FocusDirection.Down) } //Смена фокуса на нижнее поле
                )
            )
            //Города
            //Поле ввода
            OutlinedTextField(
                value = viewModel.city,
                onValueChange = {
                    viewModel.city = it
                },
                textStyle = TextStyle(
                    fontSize = 16.sp,
                    fontFamily = interFontFamily,
                    color = ColorTextDark
                ),
                label = {
                    Text(
                        text = "Город",
                        color = ColorTextLight
                    )
                },
                shape = RoundedCornerShape(8.dp),
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = Color.Transparent,
                    unfocusedContainerColor = Color.Transparent,
                    focusedIndicatorColor = BlueMain,
                    unfocusedIndicatorColor = BlueMain
                ),
                keyboardOptions = KeyboardOptions.Default.copy(
                    imeAction = ImeAction.Done
                ),
                keyboardActions = KeyboardActions(
                    onDone = { focusManager.clearFocus() }
                )
            )

        }

        Box(
            modifier = Modifier.padding(top = 10.dp)
        ){
            Button(
                onClick = {
                    if(isInternetConnection(context)){
                        val currentTime = System.currentTimeMillis() //Берёт нынешнее системное время
                        flagLoading.value = true

                        if(currentTime - lastClickTime.longValue < 10000)
                            viewModel.counterQuery+=1
                        else
                            viewModel.counterQuery = 1
                        lastClickTime.longValue = currentTime //При первой итерации равно 0, далее
                        // устанавливается системное время, что было назначено при нажатии на кнопку

                        if(viewModel.counterQuery>=6){
                            Toast.makeText(context, "Слишком много запросов. Подождите одну минуту", Toast.LENGTH_SHORT).show()

                            viewModel.isEnabledButton = false
                            flagLoading.value = false

                            Handler(Looper.getMainLooper()).postDelayed({
                                viewModel.isEnabledButton = true
                            }, 60000)
                        }
                        else{
                            viewModel.updateUserData()
                        }
                    }
                    else{
                        Toast.makeText(context, "Проблемы с интернетом. Восстановите соединение", Toast.LENGTH_SHORT).show()

                        //Блокируем кнопку на минуту
                        viewModel.isEnabledButton = false
                        Handler(Looper.getMainLooper()).postDelayed({
                            viewModel.isEnabledButton = true
                        }, 60000)
                    }
                },
                enabled = viewModel.isEnabledButton &&
                        (viewModel.city.isNotEmpty() || viewModel.numberPhone.isNotEmpty() || viewModel.email.isNotEmpty()),

                colors = ButtonColors(

                    containerColor = BlueMain,
                    contentColor = Color.White,
                    disabledContainerColor = ColorBackgroundButton,
                    disabledContentColor = ColorTextDark
                ),
                modifier = Modifier.height(52.dp).width(215.dp),
                shape = RoundedCornerShape(8.dp)
            ){
                Text(
                    text = "Редактировать", fontSize = 20.sp, fontFamily = interFontFamily)
            }
        }

    }

    //Результат по редактированию
    if(viewModel.flagExceptionUpdate){
        Toast.makeText(context,"Во время обновления данных произошла ошибка", Toast.LENGTH_SHORT).show()
        viewModel.flagExceptionUpdate = false
    }
    if(viewModel.flagSuccessUpdate){
        Toast.makeText(context,"Обновление данных прошло успешно", Toast.LENGTH_SHORT).show()
        viewModel.flagSuccessUpdate = false
    }


}