package com.example.flyvactions

import androidx.compose.material3.DrawerValue
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertIsNotDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTextInput
import androidx.navigation.compose.rememberNavController
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.flyvactions.Models.Cache.ProfileCache
import com.example.flyvactions.Views.LoginScreen
import com.example.flyvactions.Views.MainScreen
import com.example.flyvactions.Views.ProfileViews.EditProfileScreen
import com.example.flyvactions.Views.ProfileViews.ProfileScreen

import org.junit.Test
import org.junit.runner.RunWith

import org.junit.Assert.*
import org.junit.Rule
import java.time.LocalDate

/**
 * Instrumented test, which will execute on an Android device.
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
@RunWith(AndroidJUnit4::class)
class ExampleInstrumentedTest {

    @get:Rule
    val composeTestRule = createComposeRule()


    /**
     * Проверка отображения экрана профиля, и элементов на нём
     */
    @Test
    fun profileViewIsDisplayed(){
        //Устанавливаем экран, что будет тестироваться
        composeTestRule.setContent {
           ProfileScreen(rememberNavController())
        }

        composeTestRule.onNodeWithContentDescription("BackMain").assertIsDisplayed()
        composeTestRule.onNodeWithText("Выйти").assertIsDisplayed()
        composeTestRule.onNodeWithContentDescription("imageProfile").assertIsDisplayed()
        composeTestRule.onNodeWithText("Мобильный телефон").assertIsDisplayed()
        composeTestRule.onNodeWithText("Email").assertIsDisplayed()
        composeTestRule.onNodeWithText("Город проживания").assertIsDisplayed()
        composeTestRule.onNodeWithText("Редактировать профиль").assertIsDisplayed()
        composeTestRule.onNodeWithText("Баланс отдыха").assertIsDisplayed()
        composeTestRule.onNodeWithContentDescription("balanceRest").assertIsDisplayed()
    }


    /**
     * Проверка, на отображение экрана редактирования
     */
    @Test
    fun editProfileViewIsDisplayed(){
        //Устанавливаем экран, что будет тестироваться
        composeTestRule.setContent {
            EditProfileScreen(rememberNavController())
        }

        composeTestRule.onNodeWithText("Редактирование профиля")
        composeTestRule.onNodeWithText("Номер")
        composeTestRule.onNodeWithText("Email")
        composeTestRule.onNodeWithText("Город")
        composeTestRule.onNodeWithText("Редактировать")

    }


    /**
     * Проверка на отображения баланса отдыха, после нажатия пользователя на кнопку
     */
    @Test
    fun balanceHolidayCardViewIsDisplayedAfterClickUser(){
        ProfileCache.profile.hireDate = LocalDate.now()

        //Устанавливаем экран, что будет тестироваться
        composeTestRule.setContent {
            ProfileScreen(rememberNavController())
        }

        composeTestRule.onNodeWithText("Баланс отдыха").performClick()

        composeTestRule.onNodeWithText("ЕЖЕГОДНЫЙ ОТПУСК").assertIsDisplayed()
    }


    /**
     * Проверка того, что вводимы пользователем текст в поле "Логин" считывается и отображается (окно авторизации)
     */
    @Test
    fun insertTextInOutlinedTextFieldEmailIsDisplayedInLoginView(){

        //Устанавливаем экран, что будет тестироваться
        composeTestRule.setContent {
            LoginScreen(rememberNavController())
        }

        composeTestRule.onNodeWithText("Логин").performTextInput("nevajno@mail.ru")

        composeTestRule.onNodeWithText("nevajno@mail.ru").assertIsDisplayed()
    }

    /**
     * Проверка того, что валидация данных вводимых пользователем работает и пользователь не перейдёт на другое окно
     */
    @Test
    fun emailAndPasswordValidationIsWorks(){

        //Устанавливаем экран, что будет тестироваться
        composeTestRule.setContent {
            LoginScreen(rememberNavController())
        }

        composeTestRule.onNodeWithText("Логин").performTextInput("nevajno@mail.ru")
        composeTestRule.onNodeWithText("Пароль").performTextInput("nevajno")

        composeTestRule.onNodeWithText("Войти").performClick()

        composeTestRule.onNodeWithContentDescription("burger").assertIsNotDisplayed()
        composeTestRule.onNodeWithContentDescription("imageProfile").assertIsNotDisplayed()
        composeTestRule.onNodeWithText("Текущая неделя").assertIsNotDisplayed()
    }
}