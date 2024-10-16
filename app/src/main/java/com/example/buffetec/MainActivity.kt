package com.example.buffetec


import Biblioteca

import CaseDetail
import Cases
import Signup

import android.os.Bundle
import android.speech.tts.TextToSpeech
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable

import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.buffetec.textToSpeech.TextToSpeechHelper
import com.example.buffetec.ui.theme.BuffetecTheme

import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.buffetec.screens.Admin
import com.example.buffetec.screens.Login
import com.example.buffetec.screens.MainPage
import com.example.buffetec.screens.Profile
import com.example.buffetec.ui.theme.BuffetecTheme
import com.example.lazycolumnexample.navigation.Screen

class MainActivity : ComponentActivity() {

    private lateinit var ttsHelper: TextToSpeechHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        ttsHelper = TextToSpeechHelper(this)

        setContent {
            BuffetecTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    MainNavigation(modifier = Modifier.padding(innerPadding)) { text, language ->
                        ttsHelper.setLanguage(language.toString())
                        ttsHelper.speak(text)
                    }
                }
            }
        }
    }

    override fun onDestroy() {
        ttsHelper.shutdown()
        super.onDestroy()
    }

    @Composable
    fun MainNavigation(modifier: Modifier = Modifier, onSpeak: (String, Any?) -> Unit) {
        val navController = rememberNavController()


        NavHost(navController, startDestination = "biblioteca", modifier = modifier) {
            composable("biblioteca") {
                Biblioteca(navController)
            }

    NavHost(navController, startDestination = Screen.Login.route, modifier = modifier) {

        composable(Screen.Login.route) {
            Login(navController)
        }
        composable(Screen.Signup.route) {
            Signup(navController)
        }
        composable(Screen.Profile.route){
            Profile(navController)
        }

        composable(Screen.Admin.route){
            Admin(navController)
        }

        composable(Screen.Cases.route) {
            Cases(navController)

        }

        composable(Screen.MainPage.route) {
            MainPage(navController)
        }

        composable("case_detail/{caseId}") { backStackEntry ->
            val caseId = backStackEntry.arguments?.getString("caseId")
            if (caseId != null) {
                CaseDetail(caseId, navController)
            }
        }

    }

}


}

