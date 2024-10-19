package com.example.buffetec.screens

import android.app.Application
import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.buffetec.Components.ButtonComponent
import com.example.buffetec.interfaces.UsersResponse
import com.example.buffetec.interfaces.UsersServices
import com.example.buffetec.interfaces.updateRolrequest
import com.example.buffetec.interfaces.usersALL
import com.example.buffetec.ui.theme.lexendFontFamily
import com.example.buffetec.viewmodels.AllUsersState
import com.example.buffetec.viewmodels.GetUserByIdState
import com.example.buffetec.viewmodels.RolUserState
import com.example.buffetec.viewmodels.UpdateState
import com.example.buffetec.viewmodels.UsersViewModel

// Modelo de ejemplo para un usuario

@Composable
fun Admin(
    navController: NavHostController,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val userViewModel = remember { UsersViewModel(UsersServices.instance, context.applicationContext as Application) }
    val coroutineScope = rememberCoroutineScope()
    val userState by userViewModel.allusersState.collectAsState()
    var users by remember { mutableStateOf(emptyList<usersALL>()) }
    val userrolstate by userViewModel.userrolstate.collectAsState()


    LaunchedEffect(Unit) {
        userViewModel.allUsers()
    }

    when (userState) {
        is AllUsersState.Loading -> {
            // Mostrar un indicador de carga
            CircularProgressIndicator(modifier = Modifier.padding(16.dp)) // Añadir padding si es necesario
        }
        is AllUsersState.Success -> {
            // Guardar la lista de usuarios en el estado local
            users = (userState as AllUsersState.Success).response
        }
        is AllUsersState.Error -> {
            Toast.makeText(context, "An error occurred: ${(userState as AllUsersState.Error).message}", Toast.LENGTH_SHORT).show()
        }
        else -> {
            // Manejar otros estados si es necesario
        }
    }

    LaunchedEffect(userrolstate) {
        if (userrolstate is RolUserState.Success) {
            Toast.makeText(context,"Actualizacion exitosa",Toast.LENGTH_SHORT).show()
            userViewModel.allUsers()
        }
        if (userrolstate is RolUserState.Error) {
            Toast.makeText(context,"Hubo un error al guardar los datos",Toast.LENGTH_SHORT).show()
        }
    }

    // Solo mostrar LazyColumn si hay usuarios
    if (users.isNotEmpty()) {
        LazyColumn {
            items(users) { user ->
                UserCard(user, userViewModel) // Usar un composable separado para mostrar la información del usuario
            }
        }
    } else if (userState is AllUsersState.Success) {
        // Mostrar un mensaje si no hay usuarios
        Text(text = "No hay usuarios disponibles", modifier = Modifier.padding(16.dp))
    }
}

// Composable para mostrar la información de un usuario
@Composable
fun UserCard(user: usersALL, userViewModel :UsersViewModel) {
    Card(modifier = Modifier
        .background(Color.White)
        .padding(8.dp)
        .fillMaxWidth()
    ,                     border = BorderStroke(1.dp, Color(0xFFD7DBDD)),
        colors = CardDefaults.cardColors(containerColor = Color.White),
    ) {
        Column(
            modifier = Modifier.padding(30.dp)) {
            Text(text = " ${user.name} ${user.surname}",
                style = TextStyle( fontWeight = FontWeight.ExtraLight, fontSize = 20.sp))

            Spacer(modifier = Modifier.height(20.dp))

            if (user.rol == "admin"){
                ButtonComponent(
                    onClick = {guardarCambios(user.id.toString(), "basic", userViewModel)},
                    label = "Quitar Privilegios" )
            }else{
                ButtonComponent(
                    onClick = {guardarCambios(user.id.toString(), "admin", userViewModel)},
                    label = "Dar Privilegios" )
            }

            // Agrega más campos según sea necesario
        }
    }
}

fun guardarCambios(
    id : String,
    rol : String,
    userViewModel : UsersViewModel
){

    userViewModel.updateRol(id, rol)
    Log.d("resp" , "entro")
}

