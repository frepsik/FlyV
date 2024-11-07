package com.example.flyvactions.Views

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.flyvactions.ViewModels.LoginViewModel
import com.example.flyvactions.ui.theme.BlueMain

//Окно LoginView
@Composable
fun LoginScreen(navController: NavController, viewModel: LoginViewModel = viewModel()) {
    Column(modifier = Modifier.fillMaxSize().background(color = BlueMain)){

    }
}