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
import com.example.buffetec.network.RegisterRequest
import com.example.buffetec.network.RetrofitClient
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import android.content.Context
import android.content.SharedPreferences
import androidx.compose.foundation.Image
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
import com.example.buffetec.data.UserEntity
import com.example.buffetec.network.RegisterResponse
import com.example.lazycolumnexample.navigation.Screen
import java.util.regex.Pattern
import com.example.buffetec.network.saveTokenLocally


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Signup(navController: NavHostController, onSignup: (String, String, String, String, String, String, String, String, String) -> Unit, onSpeak: (String, String) -> Unit) {

    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()

    var name by remember { mutableStateOf("") }
    var surname by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var pwd by remember { mutableStateOf("") }
    var address by remember { mutableStateOf("") }
    var cp by remember { mutableStateOf("") }

    // Ciudades y colonias específicas
    val cities = listOf("Monterrey", "San Pedro Garza García", "Apodaca", "San Nicolás de los Garza", "Guadalupe", "Escobedo", "Santa Catarina")
    val neighborhoodsByCity = mapOf(
        "Monterrey" to listOf("Tecnológico", "Mitras", "San Jerónimo", "Cumbres", "Centro"),
        "San Pedro Garza García" to listOf("Valle Oriente", "Del Valle", "San Agustín", "Chipinque", "Fuentes del Valle"),
        "Apodaca" to listOf("Cumbres", "Linda Vista", "La Fé", "Santa Rosa", "Pueblo Nuevo"),
        "San Nicolás de los Garza" to listOf("Anáhuac", "Residencial Las Puentes", "La Estancia", "Los Álamos", "Hacienda Los Morales"),
        "Guadalupe" to listOf("Contry", "Las Quintas", "Valle de Guadalupe", "Jardines de Linda Vista", "Camino Real"),
        "Escobedo" to listOf("Privadas de Anáhuac", "Las Torres", "San Miguel Residencial", "Villas de Anáhuac", "Monterreal"),
        "Santa Catarina" to listOf("La Fama", "El Lechugal", "Residencial Santa Catarina", "Infonavit La Huasteca", "Puerta Mitras")
    )

    // Control de selección de ciudad y colonia
    var selectedCity by remember { mutableStateOf("Ingresa una opción") }
    var selectedNeighborhood by remember { mutableStateOf("Ingresa una opción") }
    var cityExpanded by remember { mutableStateOf(false) }
    var neighborhoodExpanded by remember { mutableStateOf(false) }

    val neighborhoods = neighborhoodsByCity[selectedCity] ?: listOf("Ingresa una opción")
    val emailPattern = Pattern.compile(
        "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+")

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
                .verticalScroll(rememberScrollState())
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

            // Nombre
            OutlinedTextField(
                value = name,
                onValueChange = { name = it.capitalize() },
                label = { Text("Nombre") },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Apellido
            OutlinedTextField(
                value = surname,
                onValueChange = { surname = it.capitalize() },
                label = { Text("Apellido") },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Correo Electrónico con validación
            OutlinedTextField(
                value = email,
                onValueChange = { email = it },
                label = { Text("Correo Electrónico") },
                keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Email),
                isError = !emailPattern.matcher(email).matches(),
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

            Spacer(modifier = Modifier.height(16.dp))

            // Dirección
            OutlinedTextField(
                value = address,
                onValueChange = { address = it.capitalize() },
                label = { Text("Dirección") },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Ciudad
            ExposedDropdownMenuBox(
                expanded = cityExpanded,
                onExpandedChange = { cityExpanded = !cityExpanded }
            ) {
                OutlinedTextField(
                    value = selectedCity,
                    onValueChange = { /* No editable */ },
                    label = { Text("Ciudad") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .menuAnchor(),
                    readOnly = true,
                    trailingIcon = {
                        ExposedDropdownMenuDefaults.TrailingIcon(expanded = cityExpanded)
                    }
                )

                ExposedDropdownMenu(
                    expanded = cityExpanded,
                    onDismissRequest = { cityExpanded = false }
                ) {
                    cities.forEachIndexed { index, city ->
                        DropdownMenuItem(
                            text = { Text(text = city) },
                            onClick = {
                                if (selectedCity != cities[index]) {
                                    // Resetear la colonia si se cambia la ciudad
                                    selectedNeighborhood = "Ingresa una opción"
                                }
                                selectedCity = cities[index]
                                cityExpanded = false
                            },
                            contentPadding = ExposedDropdownMenuDefaults.ItemContentPadding
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Colonias
            ExposedDropdownMenuBox(
                expanded = neighborhoodExpanded,
                onExpandedChange = { neighborhoodExpanded = !neighborhoodExpanded }
            ) {
                OutlinedTextField(
                    value = selectedNeighborhood,
                    onValueChange = { /* No editable */ },
                    label = { Text("Colonia") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .menuAnchor(),
                    readOnly = true,
                    trailingIcon = {
                        ExposedDropdownMenuDefaults.TrailingIcon(expanded = neighborhoodExpanded)
                    }
                )

                ExposedDropdownMenu(
                    expanded = neighborhoodExpanded,
                    onDismissRequest = { neighborhoodExpanded = false }
                ) {
                    neighborhoods.forEachIndexed { index, neighborhood ->
                        DropdownMenuItem(
                            text = { Text(text = neighborhood) },
                            onClick = {
                                selectedNeighborhood = neighborhoods[index]
                                neighborhoodExpanded = false
                            },
                            contentPadding = ExposedDropdownMenuDefaults.ItemContentPadding
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTextField(
                value = cp, onValueChange = { cp = it }
                , label = { Text("Código Postal") },
                        modifier = Modifier
                        .fillMaxWidth()
            )


            Spacer(modifier = Modifier.height(16.dp))

            // Botón para hablar los datos en voz alta
            Button(
                onClick = {
                    val dataToSpeak = "Nombre: $name, Apellido: $surname, Correo: $email, Dirección: $address, Ciudad: $selectedCity, Colonia: $selectedNeighborhood, Código Postal: $cp"
                    onSpeak(dataToSpeak, "es")
                },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(50.dp),
                colors = ButtonDefaults.buttonColors(Color(0xFF6200EA))
            ) {
                Text(text = "Datos en voz alta", color = Color.White)
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Botón de registro
            Button(
                onClick = {
                    if (name.isEmpty() || surname.isEmpty() || email.isEmpty() || pwd.isEmpty() ||
                        address.isEmpty() || selectedNeighborhood == "Ingresa una opción" || selectedCity == "Ingresa una opción" || cp.isEmpty()) {

                        Toast.makeText(
                            context,
                            "Por favor llena todos los campos",
                            Toast.LENGTH_LONG
                        ).show()
                        }
                    else if (!emailPattern.matcher(email).matches()) {
                        Toast.makeText(context, "Correo inválido", Toast.LENGTH_LONG).show()

                    }else {
                    coroutineScope.launch {
                        registerUser(
                            name, surname, email, pwd, address, selectedNeighborhood, selectedCity, "Nuevo León", cp,
                            apiService, sharedPreferences, navController
                        )
                        val encryptedPassword = encryptPassword(pwd)

                        val user = UserEntity(
                            name = name,
                            surname = surname,
                            email = email,
                            pwd = encryptedPassword,
                            address = address,
                            neighborhood = selectedNeighborhood,
                            city = selectedCity,
                            state = "Nuevo León",
                            cp = cp
                        )
                        db.insertUser(user)
                        Toast.makeText(
                            context,
                            "Usuario registrado localmente",
                            Toast.LENGTH_LONG
                        ).show()
                    }
                }},
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(50.dp),
                colors = ButtonDefaults.buttonColors(Color(0xFF6200EA))
            ) {
                Text(text = "Registrar", color = Color.White, fontSize = 18.sp)
            }

        }
    }
}



// Función para registrar un usuario y manejar la respuesta de la API
fun registerUser(
    name: String,
    surname: String,
    email: String,
    password: String,
    address: String,
    neighborhood: String,
    city: String,
    state: String,
    cp: String,
    apiService: ApiService,
    sharedPreferences: SharedPreferences,
    navController: NavHostController
) {
    val registerRequest = RegisterRequest(name, surname, email, password, address, neighborhood, city, state, cp)

    apiService.registerUser(registerRequest).enqueue(object : Callback<RegisterResponse> {
        override fun onResponse(call: Call<RegisterResponse>, response: Response<RegisterResponse>) {
            if (response.isSuccessful) {
                val token = response.body()?.token
                saveTokenLocally(token, sharedPreferences)
                // Navegar al perfil después de un registro exitoso
                navController.navigate(Screen.Profile.route)
            } else {
                // Mostrar error si el registro falla
                Toast.makeText(navController.context, "Error en el registro", Toast.LENGTH_LONG).show()
            }
        }

        override fun onFailure(call: Call<RegisterResponse>, t: Throwable) {
            Toast.makeText(navController.context, "Error de conexión", Toast.LENGTH_LONG).show()
        }
    })
}



// Función para encriptar la contraseña usando bcrypt
fun encryptPassword(plainTextPassword: String): String {
    return BCrypt.withDefaults().hashToString(12, plainTextPassword.toCharArray())
}
