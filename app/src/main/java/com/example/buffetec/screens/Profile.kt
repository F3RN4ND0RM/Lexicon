package com.example.buffetec.screens


import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.buffetec.Components.ButtonComponent


@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun Profile(
    navController: NavHostController,
    modifier: Modifier = Modifier
) {
    var name by remember { mutableStateOf("Hugo") }
    var lastName by remember { mutableStateOf("Lozano") }
    var email by remember { mutableStateOf("hugo.lozano@example.com") }
    var password by remember { mutableStateOf("password123") } // Contraseña real
    var address by remember { mutableStateOf("Direccion x") }
    var city by remember { mutableStateOf("Monterrey") }

    // Estado para mostrar el diálogo de cambiar contraseña
    var showChangePasswordDialog by remember { mutableStateOf(false) }

    // Estado para habilitar la edición del perfil
    var isEditingProfile by remember { mutableStateOf(false) }

    // Scroll state para habilitar el scroll en la columna
    val scrollState = rememberScrollState()

    // Focus manager para controlar el teclado
    val focusManager = LocalFocusManager.current

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(Color(0xFF7A3CFF))
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

            Text(
                text = "Perfil de usuario",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White,
                modifier = Modifier.padding(vertical = 16.dp)
            )

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
                    TextField(
                        value = name,
                        onValueChange = { name = it },
                        modifier = Modifier.fillMaxWidth(),
                        keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Done),
                        keyboardActions = KeyboardActions(onDone = { focusManager.clearFocus() })
                    )
                } else {
                    Text(text = name, fontSize = 16.sp, color = Color.Gray)
                }

                // Apellido
                Text(text = "Apellido:", fontSize = 18.sp, color = Color.Black)
                if (isEditingProfile) {
                    TextField(
                        value = lastName,
                        onValueChange = { lastName = it },
                        modifier = Modifier.fillMaxWidth(),
                        keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Done),
                        keyboardActions = KeyboardActions(onDone = { focusManager.clearFocus() })
                    )
                } else {
                    Text(text = lastName, fontSize = 16.sp, color = Color.Gray)
                }

                // Correo electrónico
                Text(text = "Correo electrónico:", fontSize = 18.sp, color = Color.Black)
                if (isEditingProfile) {
                    TextField(
                        value = email,
                        onValueChange = { email = it },
                        modifier = Modifier.fillMaxWidth(),
                        keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Done),
                        keyboardActions = KeyboardActions(onDone = { focusManager.clearFocus() })
                    )
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
                Button(
                    onClick = { showChangePasswordDialog = true },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF7A3CFF)),
                    shape = RoundedCornerShape(20.dp)
                ) {
                    Text(text = "Cambiar Contraseña", color = Color.White)
                }

                // Diálogo para cambiar la contraseña
                if (showChangePasswordDialog) {
                    ChangePasswordDialog(
                        onDismiss = { showChangePasswordDialog = false },
                        onPasswordChange = { newPassword ->
                            password = newPassword // Actualiza la contraseña real
                            showChangePasswordDialog = false
                        }
                    )
                }

                // Dirección
                Text(text = "Dirección:", fontSize = 18.sp, color = Color.Black)
                if (isEditingProfile) {
                    TextField(
                        value = address,
                        onValueChange = { address = it },
                        modifier = Modifier.fillMaxWidth(),
                        keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Done),
                        keyboardActions = KeyboardActions(onDone = { focusManager.clearFocus() })
                    )
                } else {
                    Text(text = address, fontSize = 16.sp, color = Color.Gray)
                }

                // Ciudad
                Text(text = "Ciudad:", fontSize = 18.sp, color = Color.Black)
                if (isEditingProfile) {
                    TextField(
                        value = city,
                        onValueChange = { city = it },
                        modifier = Modifier.fillMaxWidth(),
                        keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Done),
                        keyboardActions = KeyboardActions(onDone = { focusManager.clearFocus() })
                    )
                } else {
                    Text(text = city, fontSize = 16.sp, color = Color.Gray)
                }

                Spacer(modifier = Modifier.weight(1f))

                // Botones adicionales: Editar Perfil y Cerrar Sesión
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    Button(
                        onClick = { isEditingProfile = !isEditingProfile },
                        modifier = Modifier
                            .weight(1f)
                            .height(50.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF7A3CFF)),
                        shape = RoundedCornerShape(20.dp)
                    ) {
                        Text(
                            text = if (isEditingProfile) "Guardar Cambios" else "Editar Perfil",
                            color = Color.White
                        )
                    }

                    Button(
                        onClick = { /* Lógica para cerrar sesión */ },
                        modifier = Modifier
                            .weight(1f)
                            .height(50.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = Color.Red),
                        shape = RoundedCornerShape(20.dp)
                    ) {
                        Text(text = "Cerrar Sesión", color = Color.White)
                    }
                }
            }
        }
    }
}

@Composable
fun ChangePasswordDialog(onDismiss: () -> Unit, onPasswordChange: (String) -> Unit) {
    var newPassword by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }
    var errorMessage by remember { mutableStateOf("") }

    AlertDialog(
        onDismissRequest = { onDismiss() },
        title = { Text(text = "Cambiar Contraseña") },
        text = {
            Column {
                TextField(
                    value = newPassword,
                    onValueChange = { newPassword = it },
                    label = { Text("Nueva Contraseña") },
                    modifier = Modifier.fillMaxWidth()
                )
                TextField(
                    value = confirmPassword,
                    onValueChange = { confirmPassword = it },
                    label = { Text("Confirmar Contraseña") },
                    modifier = Modifier.fillMaxWidth()
                )
                if (errorMessage.isNotEmpty()) {
                    Text(text = errorMessage, color = Color.Red)
                }
            }
        },
        confirmButton = {
            Button(onClick = {
                if (newPassword == confirmPassword) {
                    onPasswordChange(newPassword) // Se guarda la nueva contraseña
                } else {
                    errorMessage = "Las contraseñas no coinciden"
                }
            }) {
                Text("Guardar")
            }
        },
        dismissButton = {
            Button(onClick = { onDismiss() }) {
                Text("Cancelar")
            }
        }
    )
}
