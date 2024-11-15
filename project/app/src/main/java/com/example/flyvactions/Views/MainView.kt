package com.example.flyvactions.Views

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import coil.ImageLoader
import coil.compose.rememberAsyncImagePainter
import coil.decode.SvgDecoder
import com.example.flyvactions.Models.Cache.ProfileCache
import com.example.flyvactions.R
import com.example.flyvactions.ViewModels.LoginViewModel
import com.example.flyvactions.ViewModels.MainViewModel
import com.example.flyvactions.ui.theme.BlueMain
import com.example.flyvactions.ui.theme.ColorUnnamedProofile

@Composable
fun MainScreen(navHostController: NavHostController, viewModel: MainViewModel = viewModel()){
    LaunchedEffect(Unit){
        ProfileCache.profile.userInfo = viewModel.userInfo
    }
    screen()

}

//Проверочно добавил svg
@Preview(showBackground = true)
@Composable
fun screen(){
    Column(modifier = Modifier.fillMaxSize(), verticalArrangement = Arrangement.SpaceAround, horizontalAlignment = Alignment.CenterHorizontally, ) {
        Icon(imageVector = ImageVector.vectorResource(id = R.drawable.burger), contentDescription = "",
            Modifier.size(50.dp), tint = BlueMain
        )
        Icon(imageVector = ImageVector.vectorResource(id = R.drawable.profile), contentDescription = "",
            Modifier.size(150.dp), tint = ColorUnnamedProofile
        )
    }
}