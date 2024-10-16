import android.app.Application
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.buffetec.Components.ButtonComponent
import com.example.buffetec.Components.InputField
import com.example.buffetec.R
import com.example.buffetec.data.UserDatabase
import com.example.buffetec.data.UserEntity
import com.example.buffetec.interfaces.UsersServices
import com.example.buffetec.ui.theme.lexendFontFamily
import com.example.buffetec.viewmodels.LoginState
import com.example.buffetec.viewmodels.SignUpState
import com.example.buffetec.viewmodels.UsersViewModel
import com.example.lazycolumnexample.navigation.Screen
import kotlinx.coroutines.launch
import java.util.regex.Pattern


@OptIn(ExperimentalMaterial3Api::class)

@Composable
fun Signup(navController: NavHostController) {


    var name by remember { mutableStateOf("") }
    var surname by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var pwd by remember { mutableStateOf("") }
    var address by remember { mutableStateOf("") }
    var gender by remember { mutableStateOf("Male") }
    var phone by remember { mutableStateOf("") }
    var cp by remember { mutableStateOf("") }
    var AUP = true
    var rol = "basic"
    var state = "Nuevo Leon"

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
    var selecteddGender by remember { mutableStateOf(true) }
    val neighborhoods = neighborhoodsByCity[selectedCity] ?: listOf("Ingresa una opción")


    val context = LocalContext.current
    val db = UserDatabase.getDatabase(context).userDao()
    val coroutineScope = rememberCoroutineScope()

    val userViewModel = remember { UsersViewModel(UsersServices.instance, context.applicationContext as Application) }
    val signUpState by userViewModel.signupstate.collectAsState()

    var passwordVisible by remember { mutableStateOf(false) }
    val scrollState = rememberScrollState()

    // Validación de correo electrónico
    val emailPattern = Pattern.compile(
        "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+"
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF622CFF)),
    ){
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
                .verticalScroll(rememberScrollState())
                .background(
                    Color.White,
                    shape = RoundedCornerShape(20.dp)
                ) // Fondo blanco con esquinas redondeadas
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Text(text = "Regresar",
                fontFamily = lexendFontFamily,
                fontWeight = FontWeight.ExtraLight,
                textAlign = TextAlign.Start,
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable(onClick = {
                        navController.navigate(Screen.Login.route)
                    })
            )

            Image(
                painter = painterResource(id = R.drawable.logo),
                contentDescription = "logo",
                modifier = Modifier
                    .fillMaxWidth()
                    .height(70.dp)

            )
            Spacer(modifier = Modifier
                .height(50.dp))


            // Nombre

            InputField(
                label = "Nombre",
                value = name,
                onValueChange = {name = it.capitalize() }, lexendFontFam = lexendFontFamily, visualTrans =  false)


            Spacer(modifier = Modifier.height(16.dp))

            // Apellido


            InputField(
                label = "Apellido",
                value = surname,
                onValueChange = {surname = it.capitalize() }, lexendFontFam = lexendFontFamily, visualTrans =  false)




            Spacer(modifier = Modifier.height(30.dp))


            Column(modifier = Modifier
                .padding(start = 15.dp)
                .fillMaxWidth()){
                Row(verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement =  Arrangement.Start) {
                    Text(text = "Selecciona tu género",
                        fontFamily = lexendFontFamily,
                        fontWeight = FontWeight.ExtraLight,)
                }
                Row(verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement =  Arrangement.End) {
                    Text(text = "Masculino",
                        fontFamily = lexendFontFamily,
                        fontWeight = FontWeight.ExtraLight,
                    )
                    RadioButton(
                        selected = selecteddGender,
                        onClick = {
                            selecteddGender = !selecteddGender
                            gender = "Male" })
                    Spacer(modifier = Modifier.width(10.dp))
                    Text(text = "Femenino",
                        fontFamily = lexendFontFamily,
                        fontWeight = FontWeight.ExtraLight,)
                    RadioButton(selected = !selecteddGender, onClick = {
                        gender = "Femmale"
                        selecteddGender = !selecteddGender
                    })
                }

            }



            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTextField(
                value = email,
                onValueChange = { email = it },
                label = { Text("Correo Electrónico",    fontFamily = lexendFontFamily,fontWeight = FontWeight.ExtraLight,) },
                keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Email),
                isError = !emailPattern.matcher(email).matches(),
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Contraseña con iconos de visibilidad
            OutlinedTextField(
                value = pwd,
                onValueChange = { pwd = it },
                label = { Text("Contraseña", fontFamily = lexendFontFamily,fontWeight = FontWeight.ExtraLight,) },
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


            InputField(
                label = "Dirección",
                value = address,
                onValueChange = {address = it }, lexendFontFam = lexendFontFamily, visualTrans =  false)


            Spacer(modifier = Modifier.height(16.dp))

            // Ciudad
            ExposedDropdownMenuBox(
                expanded = cityExpanded,
                onExpandedChange = { cityExpanded = !cityExpanded }
            ) {
                OutlinedTextField(
                    value = selectedCity,
                    onValueChange = { /* No editable */ },
                    label = { Text("Ciudad", fontFamily = lexendFontFamily,fontWeight = FontWeight.ExtraLight,) },
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
                            text = { Text(text = city, fontFamily = lexendFontFamily,fontWeight = FontWeight.ExtraLight,) },
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
                    label = { Text("Colonia", fontFamily = lexendFontFamily,fontWeight = FontWeight.ExtraLight,) },
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
                            text = { Text(text = neighborhood, fontFamily = lexendFontFamily,fontWeight = FontWeight.ExtraLight,) },
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
                value = cp,
                onValueChange = { cp = it },
                label = { Text("Código Postal", fontFamily = lexendFontFamily,fontWeight = FontWeight.ExtraLight,) },
                keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number),
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(24.dp))

            InputField(
                label = "Teléfono",
                value = phone,
                onValueChange = {phone = it }, lexendFontFam = lexendFontFamily, visualTrans =  false)

            Spacer(modifier = Modifier.height(24.dp))

            // Botón de registro
            ButtonComponent(
                label  = "Registrar",
                onClick = {
                    if (name.isEmpty() || surname.isEmpty() || email.isEmpty() || pwd.isEmpty() ||
                        address.isEmpty() || selectedNeighborhood == "Ingresa una opción" || selectedCity == "Ingresa una opción" || cp.isEmpty()
                    ) {
                        Toast.makeText(
                            context,
                            "Por favor llena todos los campos",
                            Toast.LENGTH_LONG
                        ).show()
                    } else if (!emailPattern.matcher(email).matches()) {
                        Toast.makeText(context, "Correo inválido", Toast.LENGTH_LONG).show()
                    } else {
                        coroutineScope.launch {

                            userViewModel.registrar(name,
                                surname,
                                gender,
                                email,
                                address,
                                selectedNeighborhood,
                                pwd,
                                selectedCity,
                                state,
                                cp,
                                phone,
                                rol,
                                AUP,
                                )

                        }
                    }
                }
            )


            // Navigation effect when login is successful
            LaunchedEffect(signUpState) {
                if (signUpState is SignUpState.Success) {
                    Toast.makeText(context,"Registro completado, inicia sesión",Toast.LENGTH_SHORT).show()

                    navController.navigate(Screen.Login.route) // Replace with your desired route
                }
                if (signUpState is SignUpState.Error) {
                    Toast.makeText(context,"Hubo un error al registrarte",Toast.LENGTH_SHORT).show()

                }
            }


        }
    }

}

