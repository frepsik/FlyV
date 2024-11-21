package com.example.flyvactions.ViewModels

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.flyvactions.Models.Cache.ProfileCache
import com.example.flyvactions.Models.DataBase.Entities.City
import com.example.flyvactions.Models.DataBase.Queries.Get
import com.example.flyvactions.Models.DataBase.Queries.Update
import kotlinx.coroutines.launch

class EditProfileViewModel : ViewModel() {
    private var get : Get = Get()
    private var update : Update = Update()

    var isEnabledBack by mutableStateOf(true)

    var email by mutableStateOf("")
    var numberPhone by mutableStateOf("")
    var city by mutableStateOf("")

    private var _listCity= mutableListOf<City>()

    var isEnabledButton by mutableStateOf(true)
    var counterQuery : Int = 0

    var flagExceptionUpdate by mutableStateOf(false)
    var flagSuccessUpdate by mutableStateOf(false)


    /**
     * Получение списка городов
     */
    fun fetchListCity(){
        viewModelScope.launch {
            _listCity = get.getCityList()
        }
    }


    /**
     * Обновление пользовательских данных
     */
    fun updateUserData(){
        //Блокируем кнопки на время оперций
        isEnabledButton = false
        isEnabledBack = false

        //Только почта
        if(email.isNotEmpty() && numberPhone.isEmpty() && city.isEmpty()){
            viewModelScope.launch {
                val newRecordEmployee = update.updateUserEmailByEmployeeId(ProfileCache.profile.userInfo!!.id, email)

                if(newRecordEmployee!=null){
                    ProfileCache.profile.email = newRecordEmployee.email!!
                    email = ""
                    flagSuccessUpdate = true
                }
                else{
                    flagExceptionUpdate = true
                }

                isEnabledButton = true
                isEnabledBack = true
            }
        }
        //Только номер телефона
        else if(email.isEmpty() && numberPhone.isNotEmpty() && city.isEmpty()){
            viewModelScope.launch {
                val newRecordEmployee = update.updateUserNumberPhoneByEmployeeId(ProfileCache.profile.userInfo!!.id, numberPhone)

                if(newRecordEmployee!=null){
                    ProfileCache.profile.numberPhone = newRecordEmployee.numberPhone
                    numberPhone = ""
                    flagSuccessUpdate = true
                }
                else{
                    flagExceptionUpdate = true
                }

                isEnabledButton = true
                isEnabledBack = true
            }
        }
        //Только город
        else if(email.isEmpty() && numberPhone.isEmpty() && city.isNotEmpty()){
            viewModelScope.launch {
                //С городом необходимо сначала получить все города, сравнить, введённый с полученными, получить объек и дальше передать id города в метод на обновление
                val cityByName = get.getCityList().firstOrNull() {
                    it.city == city
                }
                if(cityByName == null){
                    flagExceptionUpdate = true
                }
                else{
                    val newRecordEmployee = update.updateUserCityByEmployeeId(ProfileCache.profile.userInfo!!.id, cityByName.id)
                    if(newRecordEmployee!=null){
                        ProfileCache.profile.city = city
                        city = ""
                        flagSuccessUpdate = true
                    }
                    else{
                        flagExceptionUpdate = true
                    }
                }

                isEnabledButton = true
                isEnabledBack = true
            }
        }

        //Почта и телефон
        else if(email.isNotEmpty() && numberPhone.isNotEmpty() && city.isEmpty()){
            isEnabledButton = false
            isEnabledBack = false

            viewModelScope.launch {
                val newRecordEmployeeEmail = update.updateUserEmailByEmployeeId(ProfileCache.profile.userInfo!!.id, email)
                val newRecordEmployeeNumberPhone = update.updateUserNumberPhoneByEmployeeId(ProfileCache.profile.userInfo!!.id, numberPhone)
                if(newRecordEmployeeEmail!=null && newRecordEmployeeNumberPhone!=null){

                    ProfileCache.profile.email = newRecordEmployeeEmail.email!!
                    ProfileCache.profile.numberPhone = newRecordEmployeeNumberPhone.numberPhone
                    email = ""
                    numberPhone = ""
                    flagSuccessUpdate = true
                }
                else{
                    flagExceptionUpdate = true
                }
                isEnabledButton = true
                isEnabledBack = true
            }
        }

        //Почта и город
        else if(email.isNotEmpty() && numberPhone.isEmpty() && city.isNotEmpty()){
            isEnabledButton = false
            isEnabledBack = false

            viewModelScope.launch {

                val cityByName = get.getCityList().firstOrNull() {
                    it.city == city
                }
                if(cityByName == null){
                    flagExceptionUpdate = true
                }
                else{
                    val newRecordEmployeeCity = update.updateUserCityByEmployeeId(ProfileCache.profile.userInfo!!.id, cityByName.id)
                    val newRecordEmployeeEmail = update.updateUserEmailByEmployeeId(ProfileCache.profile.userInfo!!.id, email)

                    if(newRecordEmployeeEmail!=null && newRecordEmployeeCity!=null){

                        ProfileCache.profile.email = newRecordEmployeeEmail.email!!
                        ProfileCache.profile.city = city

                        city = ""
                        email = ""
                        flagSuccessUpdate = true
                    }
                    else{
                        flagExceptionUpdate = true
                    }
                }

                isEnabledButton = true
                isEnabledBack = true
            }
        }

        //Телефон и город
        else if(email.isEmpty() && numberPhone.isNotEmpty() && city.isNotEmpty()){
            isEnabledButton = false
            isEnabledBack = false

            viewModelScope.launch {

                val cityByName = get.getCityList().firstOrNull() {
                    it.city == city
                }
                if(cityByName == null){
                    flagExceptionUpdate = true
                }
                else{
                    val newRecordEmployeeCity = update.updateUserCityByEmployeeId(ProfileCache.profile.userInfo!!.id, cityByName.id)
                    val newRecordEmployeeNumberPhone = update.updateUserNumberPhoneByEmployeeId(ProfileCache.profile.userInfo!!.id, numberPhone)

                    if(newRecordEmployeeNumberPhone!=null && newRecordEmployeeCity!=null){

                        ProfileCache.profile.numberPhone = newRecordEmployeeNumberPhone.numberPhone
                        ProfileCache.profile.city = city

                        numberPhone = ""
                        city = ""
                        flagSuccessUpdate = true
                    }
                    else{
                        flagExceptionUpdate = true
                    }
                }

                isEnabledButton = true
                isEnabledBack = true
            }
        }

        //Телефон, город, почта
        else{
            isEnabledButton = false
            isEnabledBack = false

            viewModelScope.launch {

                val cityByName = get.getCityList().firstOrNull() {
                    it.city == city
                }
                if(cityByName == null){
                    flagExceptionUpdate = true
                }
                else{
                    val newRecordEmployeeCity = update.updateUserCityByEmployeeId(ProfileCache.profile.userInfo!!.id, cityByName.id)
                    val newRecordEmployeeNumberPhone = update.updateUserNumberPhoneByEmployeeId(ProfileCache.profile.userInfo!!.id, numberPhone)
                    val newRecordEmployeeEmail = update.updateUserEmailByEmployeeId(ProfileCache.profile.userInfo!!.id, email)

                    if(newRecordEmployeeNumberPhone!=null && newRecordEmployeeCity!=null && newRecordEmployeeEmail!=null){

                        ProfileCache.profile.numberPhone = newRecordEmployeeNumberPhone.numberPhone
                        ProfileCache.profile.city = city
                        ProfileCache.profile.email = newRecordEmployeeEmail.email!!

                        city = ""
                        numberPhone = ""
                        email = ""
                        flagSuccessUpdate = true
                    }
                    else{
                        flagExceptionUpdate = true
                    }
                }

                isEnabledButton = true
                isEnabledBack = true
            }
        }
    }
}