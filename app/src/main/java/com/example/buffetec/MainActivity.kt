package com.example.buffetec

import Login
import Profile
import Signup
import android.content.Context
import android.os.Bundle
import android.speech.tts.TextToSpeech
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.buffetec.network.ApiService
import com.example.buffetec.network.LoginRequest
import com.example.buffetec.network.RegisterRequest
import com.example.buffetec.network.RetrofitClient
import com.example.buffetec.textToSpeech.TextToSpeechHelper
import com.example.buffetec.ui.theme.BuffetecTheme
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import android.content.SharedPreferences
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.platform.LocalContext
import com.example.buffetec.data.UserDao
import com.example.buffetec.data.UserDatabase
import com.example.buffetec.network.LoginResponse
import com.example.buffetec.network.RegisterResponse
import com.example.lazycolumnexample.navigation.Screen
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import at.favre.lib.crypto.bcrypt.BCrypt

class MainActivity : ComponentActivity() {

    private lateinit var apiService: ApiService
    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var ttsHelper: TextToSpeechHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        // Inicializar Retrofit y SharedPreferences
        apiService = RetrofitClient.retrofit.create(ApiService::class.java)
        sharedPreferences = getSharedPreferences("AppPrefs", Context.MODE_PRIVATE)
        ttsHelper = TextToSpeechHelper(this)

        // Configuración del contenido de la UI
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

        // Obtener el contexto
        val context = LocalContext.current

        // Inicializar SharedPreferences
        val sharedPreferences = context.getSharedPreferences("AppPrefs", Context.MODE_PRIVATE)

        // Obtener el token de SharedPreferences
        val token = sharedPreferences.getString("jwt_token", null)

        // Verificación del token dentro del Composable
        if (token != null) {
            // Si el token existe, navegar al perfil
            LaunchedEffect(Unit) {
                navController.navigate(Screen.Profile.route) {
                    popUpTo(0) // Limpiar el historial de navegación
                }
            }
        }

        // Definir el NavHost con las rutas de la app
        NavHost(navController, startDestination = if (token != null) Screen.Profile.route else Screen.Login.route, modifier = modifier) {
            composable(Screen.Login.route) {
                // Pasar el UserDao, ApiService, y SharedPreferences a la pantalla de Login
                Login(navController, onLogin = { email, password ->
                    // Lógica del login
                    loginUser(email, password) {
                        navController.navigate(Screen.Profile.route) {
                            popUpTo(0) // Limpiar el historial de navegación
                        }
                    }
                })
            }
            composable(Screen.Signup.route) {
                // Pasar los datos necesarios a la pantalla de Signup
                Signup(navController, onSignup = { name, surname, email, password, address, neighborhood, city, state, cp ->
                    registerUser(RegisterRequest(name, surname, email, password, address, neighborhood, city, state, cp))
                }, onSpeak = onSpeak)
            }
            composable(Screen.Profile.route) {
                // Pasar el navController y sharedPreferences para poder cerrar sesión
                Profile(navController, sharedPreferences)
            }
        }
    }


    // Función para registrar un usuario
    private fun registerUser(registerRequest: RegisterRequest) {
        apiService.registerUser(registerRequest).enqueue(object : Callback<RegisterResponse> {
            override fun onResponse(call: Call<RegisterResponse>, response: Response<RegisterResponse>) {
                if (response.isSuccessful) {
                    val token = response.body()?.token
                    saveTokenLocally(token)
                    Toast.makeText(this@MainActivity, "Registro exitoso", Toast.LENGTH_LONG).show()
                } else {
                    Toast.makeText(this@MainActivity, "Error en el registro", Toast.LENGTH_LONG).show()
                }
            }

            override fun onFailure(call: Call<RegisterResponse>, t: Throwable) {
                Toast.makeText(this@MainActivity, "Error de conexión", Toast.LENGTH_LONG).show()
            }
        })
    }

    // Función para hacer login de usuario
    private fun loginUser(email: String, password: String, onSuccess: () -> Unit) {
        apiService.loginUser(LoginRequest(email, password)).enqueue(object : Callback<LoginResponse> {
            override fun onResponse(call: Call<LoginResponse>, response: Response<LoginResponse>) {
                if (response.isSuccessful) {
                    val token = response.body()?.token
                    saveTokenLocally(token)
                    onSuccess() // Navegar al perfil
                } else {
                    runOnUiThread {
                        Toast.makeText(this@MainActivity, "Credenciales incorrectas", Toast.LENGTH_LONG).show()
                    }
                }
            }

            override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                runOnUiThread {
                    Toast.makeText(this@MainActivity, "Error de conexión", Toast.LENGTH_LONG).show()
                }
            }
        })
    }

    // Función para guardar el token JWT localmente
    private fun saveTokenLocally(token: String?) {
        val editor = sharedPreferences.edit()
        editor.putString("jwt_token", token)
        editor.apply()
    }
}
