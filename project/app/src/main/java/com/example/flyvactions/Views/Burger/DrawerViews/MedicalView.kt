package com.example.flyvactions.Views.Burger.DrawerViews

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.example.flyvactions.Models.isInternetConnection
import com.example.flyvactions.R
import com.example.flyvactions.ViewModels.DrawerViewModels.MedicalViewModel
import com.example.flyvactions.ui.theme.BlueMain
import com.example.flyvactions.ui.theme.ColorBackgroundButton
import com.example.flyvactions.ui.theme.ColorTextDark
import com.example.flyvactions.ui.theme.ColorTextLight
import com.example.flyvactions.ui.theme.interFontFamily

/**
 * Окно где пользователь может оформить больничный
 */
@Composable
fun MedicalScreen(navHostController: NavHostController, viewModel: MedicalViewModel = viewModel()){
    val context = LocalContext.current
    LaunchedEffect(Unit) {
        viewModel.fetchMedicalPeriod()
    }
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
                        navHostController.navigate("mainView") {
                            popUpTo(0) {
                                inclusive = true
                            }
                            launchSingleTop = true
                        }

                    }
            )
        }

        Spacer(modifier = Modifier.height(115.dp))

        Box(
            modifier = Modifier.fillMaxWidth(),
            contentAlignment = Alignment.Center
        ){
            Text(
                text = "${viewModel.amountDays}\nдень",
                fontFamily = interFontFamily,
                fontSize = 64.sp,
                color = BlueMain,
                textAlign = TextAlign.Center
            )
        }

        Spacer(modifier = Modifier.height(160.dp))
        Column(
            verticalArrangement = Arrangement.spacedBy(40.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.fillMaxWidth()
        ){
            Button(
                onClick = {
                    if(!isInternetConnection(context)){
                        Toast.makeText(context, "Проблемы с интернетом. Восстановите соединение", Toast.LENGTH_SHORT).show()
                    }
                    else{
                        if(!viewModel.isOpen){
                            //Открываем больничный
                            viewModel.medicalRegistration()
                        }
                        else{
                            //Закрываем больничный
                            viewModel.medicalDelete()
                        }
                    }
                },
                shape = RoundedCornerShape(8.dp),
                colors = ButtonColors(
                    containerColor = BlueMain,
                    contentColor = Color.White,
                    disabledContainerColor = ColorBackgroundButton,
                    disabledContentColor = ColorTextDark
                ),
                modifier = Modifier
                    .height(52.dp)
                    .width(215.dp)
            ) {
                Text(
                    text = if(!viewModel.isOpen) { "Открыть" } else { "Закрыть" },
                    fontSize = 20.sp,
                    fontFamily = interFontFamily
                )
            }
            Text(
                text = if(!viewModel.isOpen){ "Больничный\nещё не открыт"} else { "Больничный\nоткрыт" },
                fontSize = 18.sp,
                color = ColorTextLight,
                textAlign = TextAlign.Center
            )
        }

        if(viewModel.isShowHint){
            Toast.makeText(context, viewModel.hint, Toast.LENGTH_SHORT).show()
            viewModel.isShowHint = false
        }
    }
}