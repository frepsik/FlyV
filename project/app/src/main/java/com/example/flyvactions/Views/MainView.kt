package com.example.flyvactions.Views

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.example.flyvactions.Models.Cache.ProfileCache
import com.example.flyvactions.ViewModels.LoginViewModel
import com.example.flyvactions.ViewModels.MainViewModel

@Composable
fun MainScreen(navHostController: NavHostController, viewModel: MainViewModel = viewModel()){
    LaunchedEffect(Unit){
        ProfileCache.profile.userInfo = viewModel.userInfo
    }
}