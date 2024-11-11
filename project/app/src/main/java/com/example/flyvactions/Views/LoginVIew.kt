package com.example.flyvactions.Views

import android.os.Handler
import android.os.Looper
import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.flyvactions.Models.BaseState
import com.example.flyvactions.ViewModels.LoginViewModel
import com.example.flyvactions.ui.theme.BlueMain
import com.example.flyvactions.ui.theme.ColorTextDark
import com.example.flyvactions.ui.theme.ColorTextLight
import com.example.flyvactions.ui.theme.interFontFamily
import kotlinx.coroutines.delay


//Окно LoginView
@Composable
fun LoginScreen(navController: NavController, viewModel: LoginViewModel = viewModel()) {
    val focusManager = LocalFocusManager.current
    val flagLoading = remember{ mutableStateOf(false) }
    val context = LocalContext.current

    val lastClickTime = remember { mutableLongStateOf(0L) }

    Column(modifier = Modifier.fillMaxSize().padding(bottom = 40.dp)
        //Снятие фокуса с элемента
        .pointerInput(Unit){
            detectTapGestures(onTap = {
                focusManager.clearFocus()
            })
        },

        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally){

        Box(modifier = Modifier.height(216.dp).width(234.dp),
            contentAlignment = Alignment.Center) {
            Text(
                text = "FlyV",
                fontSize = 68.sp,
                fontFamily = interFontFamily,
                color = BlueMain
            )
        }

        Spacer(modifier = Modifier.height(15.dp))

        Column(verticalArrangement = Arrangement.spacedBy(35.dp)) {
            OutlinedTextField(
                value = viewModel.emailUser,
                onValueChange = {viewModel.emailUser = it},
                textStyle = TextStyle(
                    fontSize = 16.sp,
                    fontFamily = interFontFamily,
                    color = ColorTextDark
                ),
                label = {
                    Text(
                        text = "Логин",
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

            OutlinedTextField(
                value = viewModel.passwordUser,
                onValueChange = {viewModel.passwordUser = it},
                textStyle = TextStyle(
                    fontSize = 16.sp,
                    fontFamily = interFontFamily,
                    color = ColorTextDark
                ),
                label = {
                    Text(
                        text = "Пароль",
                        color = ColorTextLight
                    )
                },
                visualTransformation = PasswordVisualTransformation(),
                shape = RoundedCornerShape(8.dp),
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = Color.Transparent,
                    unfocusedContainerColor = Color.Transparent,
                    focusedIndicatorColor = BlueMain,
                    unfocusedIndicatorColor = BlueMain
                ),
                //Когда будет срабатывать
                keyboardOptions = KeyboardOptions.Default.copy(
                    imeAction = ImeAction.Done //При нажатии на кнопку готово
                ),
                //Что будет срабатывать
                keyboardActions = KeyboardActions(
                    onDone = { focusManager.clearFocus() } //Очистка фокуса
                )
            )
        }

        Spacer(modifier = Modifier.height(120.dp))

        Button(
            onClick = {
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
                    viewModel.flagQuery = false

                    Handler(Looper.getMainLooper()).postDelayed({
                        viewModel.flagQuery = true
                    }, 60000)
                }
                else
                    viewModel.onSignInEmailPassword()

            },
            enabled = viewModel.flagQuery,
            colors = ButtonColors(
                containerColor = BlueMain,
                contentColor = Color.White,
                disabledContainerColor = BlueMain,
                disabledContentColor = Color.White
            ),
            modifier = Modifier.height(52.dp).width(190.dp),
            shape = RoundedCornerShape(8.dp)
        ){
            Text(
                text = "Войти", fontSize = 20.sp, fontFamily = interFontFamily)
        }
    }
    //Костыль для того, чтобы отображать загрузку и осуществлять переход на другое окно
    if(flagLoading.value){
        when(viewModel.userState.value){
            is BaseState.Loading -> { LoadingCircle() }
            is BaseState.Success -> {
                navController.navigate("mainView"){
                    popUpTo("loginView"){
                        inclusive = true
                    }
                }
                flagLoading.value = false
            }
            is BaseState.Error -> {
                Toast.makeText(context, "Неверный логин или пароль, повторите попытку", Toast.LENGTH_SHORT).show()
                flagLoading.value = false
            }
        }
    }
}
