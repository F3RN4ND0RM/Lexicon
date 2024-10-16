package com.example.buffetec.screens

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
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.buffetec.Components.ButtonComponent
import com.example.buffetec.ui.theme.lexendFontFamily

// Modelo de ejemplo para un usuario
data class User(
    val id: String,
    val name: String,
    val isAdmin: Boolean
)

@Composable
fun Admin(
    navController: NavHostController,
    modifier: Modifier = Modifier
) {
    // Simulación de la lista de usuarios
    var users by remember { mutableStateOf(listOf<User>()) }

    LaunchedEffect(Unit) {
        users = listOf(
            User("1", "Hugo Lozano", false),
            User("2", "Fernando Ramos", false),
            User("3", "Eugenio Rodriguez", true) // Este usuario ya es admin
        )
    }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight()
            .padding(35.dp, 15.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Admin Panel",
            color = Color.Black,
            fontFamily = lexendFontFamily,
            style = TextStyle(
                fontWeight = FontWeight.ExtraLight,
                fontSize = 50.sp
            ),
            modifier = Modifier.padding(bottom = 16.dp)
        )

        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.White, shape = RoundedCornerShape(20.dp))
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp, 20.dp)
            ) {
                LazyColumn(
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    // Mostrar la lista de usuarios
                    items(users) { user ->
                        UserItem(
                            user = user,
                            onGrantAdmin = { updatedUser ->
                                users = users.map { if (it.id == updatedUser.id) updatedUser else it }
                            }
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun UserItem(
    user: User,
    onGrantAdmin: (User) -> Unit,
    modifier: Modifier = Modifier
) {
    var showDialog by remember { mutableStateOf(false) }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(10.dp)
            .background(Color(0xFFF1F1F1), shape = RoundedCornerShape(15.dp))
            .padding(16.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text(
                    text = user.name,
                    style = TextStyle(
                        fontFamily = lexendFontFamily,
                        fontWeight = FontWeight.Medium,
                        fontSize = 18.sp
                    )
                )
                Text(
                    text = if (user.isAdmin) "Admin" else "User",
                    style = TextStyle(
                        fontFamily = lexendFontFamily,
                        fontWeight = FontWeight.Light,
                        fontSize = 16.sp
                    )
                )
            }

            if (!user.isAdmin) {
                ButtonComponent(
                    label = "Grant Admin Access",
                    onClick = { showDialog = true }
                )

                if (showDialog) {
                    GrantAdminDialog(
                        onDismiss = { showDialog = false },
                        onConfirm = { accepted ->
                            if (accepted) {
                                onGrantAdmin(user.copy(isAdmin = true)) // Actualizar el estado de admin
                            }
                            showDialog = false
                        }
                    )
                }
            } else {
                Text(
                    text = "Already Admin",
                    color = Color.Gray,
                    style = TextStyle(
                        fontFamily = lexendFontFamily,
                        fontWeight = FontWeight.Light,
                        fontSize = 16.sp
                    )
                )
            }
        }
    }
}

@Composable
fun GrantAdminDialog(
    onDismiss: () -> Unit,
    onConfirm: (Boolean) -> Unit
) {
    var text by remember { mutableStateOf("") }
    var errorMessage by remember { mutableStateOf("") }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(text = "Confirm Admin Access") },
        text = {
            Column {
                Text("Para conceder acceso de administrador, por favor escribe 'ACEPTO'.")
                TextField(
                    value = text,
                    onValueChange = { text = it },
                    label = { Text("Escribe 'ACEPTO'") },
                    modifier = Modifier.fillMaxWidth()
                )
                if (errorMessage.isNotEmpty()) {
                    Text(text = errorMessage, color = MaterialTheme.colorScheme.error)
                }
            }
        },
        confirmButton = {
            Button(onClick = {
                if (text == "ACEPTO") {
                    onConfirm(true)
                } else {
                    errorMessage = "Texto incorrecto. Escribe 'ACEPTO' para continuar."
                    onConfirm(false)
                }
            }) {
                Text("Confirmar")
            }
        },
        dismissButton = {
            Button(onClick = onDismiss) {
                Text("Cancelar")
            }
        }
    )
}