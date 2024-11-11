package com.example.flyvactions.Views

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.example.flyvactions.ui.theme.BlueMain
import com.example.flyvactions.ui.theme.ColorBackground

//Кружок загрузки, что накладывается на экран
@Composable
fun LoadingCircle(){
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(ColorBackground.copy(alpha = 0.5f))
            .clickable(enabled = false){},//Отключает клик вообще по всему пространстрву во время загрузки (ибо нехуй кликать, пока идёт загрузка)
        contentAlignment = Alignment.Center
    ){
        CircularProgressIndicator(
            color = BlueMain
        )
    }
}