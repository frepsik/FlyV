package com.example.flyvactions.Views

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.flyvactions.ViewModels.LoginViewModel
import com.example.flyvactions.ui.theme.BlueMain
import com.example.flyvactions.ui.theme.interFontFamily


//Окно LoginView
@Composable
fun LoginScreen(navController: NavController, viewModel: LoginViewModel = viewModel()) {

    Column(modifier = Modifier.fillMaxSize().background(color = BlueMain),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally){
        Text(
            text = "dsadjsajkdsajkdskadYAYYA",
            fontSize = 64.sp,
            fontFamily = interFontFamily,
            color = Color.White
        )
    }
}