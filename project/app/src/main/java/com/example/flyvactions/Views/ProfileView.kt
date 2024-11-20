package com.example.flyvactions.Views

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import com.example.flyvactions.Models.Cache.ProfileCache
import com.example.flyvactions.Models.DataClasses.clearProfileCache
import com.example.flyvactions.R
import com.example.flyvactions.ViewModels.ProfileViewModel
import com.example.flyvactions.ui.theme.BlueMain
import com.example.flyvactions.ui.theme.ColorBackground
import com.example.flyvactions.ui.theme.ColorTextDark
import com.example.flyvactions.ui.theme.ColorTextLight
import com.example.flyvactions.ui.theme.interFontFamily

@Composable
fun ProfileScreen(navHostController: NavHostController, viewModel: ProfileViewModel = viewModel()){
    Column(
        modifier = Modifier.fillMaxSize().background(ColorBackground)
            .padding(start = 25.dp, top = 75.dp, end = 25.dp, bottom = 75.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ){
        //Up-bar
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = ImageVector.vectorResource(id = R.drawable.back),
                contentDescription = "BackMain",
                tint = ColorTextDark,
                modifier = Modifier
                    .size(30.dp)
                    .clickable {
                        navHostController.navigate("mainView"){
                            popUpTo("profileView"){
                                inclusive = true
                            }
                            launchSingleTop = true
                        }
                    }
            )
            Text(
                text = "Выйти",
                fontSize = 14.sp,
                fontFamily = interFontFamily,
                color = ColorTextLight,
                modifier = Modifier.clickable {
                    clearProfileCache()
                    navHostController.navigate("loginView"){
                        popUpTo(0)
                    }
                }
            )
        }

        //Фотка профиля и имя пользователя
        Column(
            verticalArrangement = Arrangement.spacedBy(50.dp)
        ){
            AsyncImage(
                model = viewModel.urlProfileImage,
                contentDescription = "imageProfile",
                modifier = Modifier
                    .size(211.dp)
                    .clip(CircleShape)
                    .border(1.dp ,BlueMain, CircleShape)
            )

            Text(
                text = viewModel.nameUser,
                fontSize = 32.sp,
                fontWeight = FontWeight.Medium,
                fontFamily = interFontFamily,
                color = ColorTextDark
            )
        }
    }
}