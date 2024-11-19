package com.example.flyvactions.Views.SupportingMainViews

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.example.flyvactions.Models.DataClasses.AbsenceEmployeeCalendar

/**
 * LazyColumn, что выводит определённым образом список отсуттвующих пользователей
 */
@Composable
fun AbsencesEmployeesLazyColumn(absencesEmployees : MutableList<AbsenceEmployeeCalendar>){

    LazyColumn(
        verticalArrangement = Arrangement.spacedBy(28.dp),
        horizontalAlignment = Alignment.Start
    ) {
        items(absencesEmployees){
                item ->
            Row(horizontalArrangement = Arrangement.SpaceAround) {
                AsyncImage(
                    model = item.urlPhotoProfile,
                    contentDescription = "iconProfile",
                    modifier = Modifier.size(55.dp)
                )
                Text(text = item.fullName)
                Text(text = item.reasonAbsence)
            }
        }
    }
}