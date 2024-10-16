
package com.example.buffetec.screens


import android.app.Application
import android.util.Log
import android.widget.Toast
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.example.buffetec.Components.ButtonComponent
import com.example.buffetec.Components.InputField
import com.example.buffetec.interfaces.UsersServices
import com.example.buffetec.ui.theme.lexendFontFamily
import com.example.buffetec.viewmodels.GetUserByIdState
import com.example.buffetec.viewmodels.LoginState
import com.example.buffetec.viewmodels.SignUpState
import com.example.buffetec.viewmodels.UpdateState
import com.example.buffetec.viewmodels.UsersViewModel
import com.example.lazycolumnexample.navigation.Screen


@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun Profile(
    navController: NavHostController,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val userViewModel = remember { UsersViewModel(UsersServices.instance, context.applicationContext as Application) }
    val coroutineScope = rememberCoroutineScope()
    val getUserByIdState by userViewModel.usersbyidState.collectAsState()
    var name by remember { mutableStateOf("") }
    var lastName by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") } // Contraseña real
    var address by remember { mutableStateOf("") }
    var city by remember { mutableStateOf("") }
    var showChangePasswordDialog by remember { mutableStateOf(false) }
    var isEditingProfile by remember { mutableStateOf(false) }
    val scrollState = rememberScrollState()
    val focusManager = LocalFocusManager.current
    val updatestate by userViewModel.updatestate.collectAsState()



    LaunchedEffect(Unit) {
        userViewModel.getusuariobyid()
    }


    // Manejar el estado de carga
    when (getUserByIdState) {
        is GetUserByIdState.Loading -> {
            // Mostrar un indicador de carga si es necesario
        }
        is GetUserByIdState.Success -> {
            val userResponse = (getUserByIdState as GetUserByIdState.Success).response
            name = userResponse.name
            lastName = userResponse.surname
            email = userResponse.email
            address = userResponse.address
            city = userResponse.city
            password = "*********" // Ocultar la contraseña en la UI
        }
        is GetUserByIdState.Error -> {
            Toast.makeText(context, "Ha ocurrido un error", Toast.LENGTH_SHORT).show()
        }
        else -> {
            // Manejar cualquier otro estado inesperado, si es necesario
        }
    }

    LaunchedEffect(updatestate) {
        if (updatestate is UpdateState.Success) {
            Toast.makeText(context,"Actualizacion exitosa",Toast.LENGTH_SHORT).show()
            userViewModel.getusuariobyid()

        }
        if (updatestate is UpdateState.Error) {
            Toast.makeText(context,"Hubo un error al guardar los datos",Toast.LENGTH_SHORT).show()

        }
    }


    Box(
        modifier = modifier
            .fillMaxSize()
            .clickable { focusManager.clearFocus() }, // Para quitar el enfoque al hacer clic fuera
        contentAlignment = Alignment.TopCenter
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
                .verticalScroll(scrollState),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(text = "Regresar",
                fontFamily = lexendFontFamily,
                fontWeight = FontWeight.ExtraLight,
                textAlign = TextAlign.Start,
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable(onClick = {
                        navController.navigate(Screen.Biblioteca.route)
                    })
            )


            Text(
                text = "Perfil de usuario",
                color = Color.Black,
                modifier = Modifier.align(Alignment.CenterHorizontally),
                fontFamily = lexendFontFamily,
                style = TextStyle( fontWeight = FontWeight.ExtraLight, fontSize = 30.sp)
            )

            Spacer(modifier = Modifier.height(20.dp))

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(
                        color = Color.White,
                        shape = RoundedCornerShape(25.dp)
                    )
                    .padding(16.dp),
                horizontalAlignment = Alignment.Start,
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // Nombre
                Text(text = "Nombre:", fontSize = 18.sp, color = Color.Black)
                if (isEditingProfile) {
                    InputField(label = "Nombre",
                        value = name ,
                        onValueChange = {name = it},
                        lexendFontFam = lexendFontFamily, visualTrans =  false)

                } else {
                    Text(text = name, fontSize = 16.sp, color = Color.Gray)
                }

                // Apellido
                Text(text = "Apellido:", fontSize = 18.sp, color = Color.Black)
                if (isEditingProfile) {
                    InputField(label = "Apellidos",
                        value = lastName ,
                        onValueChange = {lastName = it},
                        lexendFontFam = lexendFontFamily, visualTrans =  false)
                } else {
                    Text(text = lastName, fontSize = 16.sp, color = Color.Gray)
                }

                // Correo electrónico
                Text(text = "Correo electrónico:", fontSize = 18.sp, color = Color.Black)
                if (isEditingProfile) {
                    InputField(label = "Email",
                        value = email ,
                        onValueChange = {email = it},
                        lexendFontFam = lexendFontFamily, visualTrans =  false)
                } else {
                    Text(text = email, fontSize = 16.sp, color = Color.Gray)
                }

                // Mostrar Contraseña (oculta con asteriscos)
                Text(text = "Contraseña:", fontSize = 18.sp, color = Color.Black)
                Text(
                    text = "*".repeat(password.length), // Muestra los asteriscos
                    fontSize = 16.sp,
                    color = Color.Gray
                )

                // Botón para cambiar la contraseña
                ButtonComponent(
                    label = "Cambiar Contraseña",
                    onClick = { showChangePasswordDialog = true },
                )


                // Dirección
                Text(text = "Dirección:", fontSize = 18.sp, color = Color.Black)
                if (isEditingProfile) {
                    InputField(label = "Dirección",
                        value = address ,
                        onValueChange = {address = it},
                        lexendFontFam = lexendFontFamily, visualTrans =  false)
                } else {
                    Text(text = address, fontSize = 16.sp, color = Color.Gray)
                }

                // Ciudad
                Text(text = "Ciudad:", fontSize = 18.sp, color = Color.Black)
                if (isEditingProfile) {
                    InputField(label = "Ciudad",
                        value = city ,
                        onValueChange = {city = it},
                        lexendFontFam = lexendFontFamily, visualTrans =  false)
                } else {
                    Text(text = city, fontSize = 16.sp, color = Color.Gray)
                }

                Spacer(modifier = Modifier.weight(1f))


                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    ButtonComponent(
                        label =  if (isEditingProfile) "Guardar Cambios" else "Editar Perfil",
                        onClick = {
                            if (isEditingProfile) {
                                guardarCambios(
                                    userViewModel,
                                    name,
                                    lastName,
                                    email,
                                    address,
                                    city
                                )
                                // Now toggle after saving
                                isEditingProfile = !isEditingProfile
                            } else {
                                isEditingProfile = !isEditingProfile // Start editing
                            }
                        }
                    )

                }


            }
        }
    }
}



fun guardarCambios(
    userViewModel: UsersViewModel,
    name: String,
    surname: String,
    email: String,
    address: String,
    city : String,
){

   var address2 = address + "3"
    userViewModel.actualizar(name, surname, email, address2 ,  city)
    Log.d("resp" , "entro")
}

