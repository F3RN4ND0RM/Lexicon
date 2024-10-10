import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import at.favre.lib.crypto.bcrypt.BCrypt
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.buffetec.network.ApiService
import com.example.buffetec.network.LoginRequest
import com.example.buffetec.network.LoginResponse
import com.example.buffetec.network.RetrofitClient
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.navigation.NavHostController
import com.example.buffetec.R
import com.example.buffetec.data.UserDatabase
import com.example.lazycolumnexample.navigation.Screen
import com.example.buffetec.network.saveTokenLocally
import com.example.buffetec.data.UserEntity

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Login(navController: NavHostController, onLogin: (String, String) -> Unit) {

    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()

    var email by remember { mutableStateOf("") }
    var pwd by remember { mutableStateOf("") }

    val db = UserDatabase.getDatabase(context).userDao()
    var passwordVisible by remember { mutableStateOf(false) }
    val scrollState = rememberScrollState()

    // Inicializar Retrofit y SharedPreferences
    val apiService = RetrofitClient.retrofit.create(ApiService::class.java)
    val sharedPreferences = context.getSharedPreferences("AppPrefs", Context.MODE_PRIVATE)

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF6200EA))
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
                .verticalScroll(scrollState)
                .background(Color.White, shape = RoundedCornerShape(40.dp))
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Image(
                painter = painterResource(id = R.drawable.logo),
                contentDescription = "Logo de la empresa",
                modifier = Modifier
                    .size(150.dp)
                    .padding(bottom = 16.dp)
                    .align(Alignment.CenterHorizontally)
            )

            // Correo electrónico
            OutlinedTextField(
                value = email,
                onValueChange = { email = it },
                label = { Text("Correo Electrónico") },
                keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Email),
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Contraseña con iconos de visibilidad
            OutlinedTextField(
                value = pwd,
                onValueChange = { pwd = it },
                label = { Text("Contraseña") },
                modifier = Modifier.fillMaxWidth(),
                visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Password),
                trailingIcon = {
                    IconButton(onClick = { passwordVisible = !passwordVisible }) {
                        Icon(
                            imageVector = if (passwordVisible) Icons.Filled.Visibility else Icons.Filled.VisibilityOff,
                            contentDescription = if (passwordVisible) "Ocultar contraseña" else "Mostrar contraseña"
                        )
                    }
                }
            )

            Spacer(modifier = Modifier.height(24.dp))

            // Botón de inicio de sesión
            Button(
                onClick = {
                    if (email.isEmpty() || pwd.isEmpty()) {
                        Toast.makeText(
                            context,
                            "Por favor llena todos los campos",
                            Toast.LENGTH_LONG
                        ).show()
                    } else {
                        coroutineScope.launch {
                            // Buscar usuario en Room y verificar contraseña
                            val userFromDb = db.getUserByEmail(email)
                            if (userFromDb != null) {
                                val isPasswordCorrect = verifyPassword(pwd, userFromDb.pwd)
                                if (isPasswordCorrect) {
                                    // Hacer login con la API
                                    loginUser(
                                        email, pwd, apiService, sharedPreferences, navController
                                    )
                                } else {
                                    Toast.makeText(
                                        context,
                                        "Contraseña incorrecta",
                                        Toast.LENGTH_LONG
                                    ).show()
                                }
                            } else {
                                Toast.makeText(
                                    context,
                                    "Usuario no encontrado",
                                    Toast.LENGTH_LONG
                                ).show()
                            }
                        }
                    }
                },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(50.dp),
                colors = ButtonDefaults.buttonColors(Color(0xFF6200EA))
            ) {
                Text(text = "Iniciar Sesión", color = Color.White, fontSize = 18.sp)
            }
            Spacer(modifier = Modifier.height(16.dp))

            // Botón para navegar al registro
            Text(
                text = "¿No tienes cuenta? Registrarse",
                color = Color(0xFF6200EA),
                modifier = Modifier.clickable {
                    navController.navigate(Screen.Signup.route)
                }
            )
        }
    }
}

// Función para verificar la contraseña usando bcrypt
fun verifyPassword(plainTextPassword: String, hashedPassword: String): Boolean {
    val result = BCrypt.verifyer().verify(plainTextPassword.toCharArray(), hashedPassword)
    return result.verified
}

// Función para hacer login con la API y manejar la respuesta
// Función para hacer login con la API y manejar la respuesta
fun loginUser(
    email: String,
    password: String,
    apiService: ApiService,
    sharedPreferences: SharedPreferences,
    navController: NavHostController
) {
    val loginRequest = LoginRequest(email = email, password = password)

    apiService.loginUser(loginRequest).enqueue(object : Callback<LoginResponse> {
        override fun onResponse(call: Call<LoginResponse>, response: Response<LoginResponse>) {
            if (response.isSuccessful) {
                val token = response.body()?.token
                saveTokenLocally(token, sharedPreferences)

                // Verificación del token guardado en SharedPreferences

                val savedToken = sharedPreferences.getString("jwt_token", null)
                if (savedToken != null) {
                    Log.d("LoginProcess", "Token JWT guardado: $savedToken")
                    Toast.makeText(navController.context, "Token JWT guardado: $savedToken", Toast.LENGTH_LONG).show()
                } else {
                    Log.d("LoginProcess", "No se pudo guardar el token JWT")
                    Toast.makeText(navController.context, "Error: No se guardó el token JWT", Toast.LENGTH_LONG).show()
                }

                // Navegar al perfil después del login exitoso
                navController.navigate(Screen.Profile.route)
            } else {
                Log.d("LoginProcess", "Error en el inicio de sesión")
                Toast.makeText(navController.context, "Error en el inicio de sesión", Toast.LENGTH_LONG).show()
            }
        }

        override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
            Log.d("LoginProcess", "Error de conexión: ${t.message}")
            Toast.makeText(navController.context, "Error de conexión", Toast.LENGTH_LONG).show()
        }
    })
}

