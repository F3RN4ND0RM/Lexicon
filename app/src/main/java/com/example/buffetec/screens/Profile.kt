import android.provider.ContactsContract.Profile
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavHostController
import com.example.buffetec.model.User
import com.example.lazycolumnexample.navigation.Screen
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch


@Composable
fun Profile(
    navController: NavHostController,
    modifier: Modifier = Modifier // Añadir el parámetro modifier aquí
) {
    var name by remember { mutableStateOf("Hugo") }
    var lastName by remember { mutableStateOf("Lozano") }
    var email by remember { mutableStateOf("hugo.lozano@example.com") }
    var password by remember { mutableStateOf("password123") }
    var address by remember { mutableStateOf("Direccion x") }
    var city by remember { mutableStateOf("Monterrey") }

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(Color(0xFF7A3CFF)), // Fondo morado
        contentAlignment = Alignment.TopCenter
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Texto de "Perfil de usuario" en la parte superior
            Text(
                text = "Perfil de usuario",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White,
                modifier = Modifier.padding(vertical = 16.dp)
            )

            // Caja de información del perfil
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(
                        color = Color.White,
                        shape = RoundedCornerShape(25.dp)
                    )
                    .padding(16.dp),
                horizontalAlignment = Alignment.Start, // Alinear texto a la izquierda
                verticalArrangement = Arrangement.spacedBy(16.dp) // Espacio entre elementos
            ) {
                // Nombre
                Text(text = "Nombre:", fontSize = 18.sp, color = Color.Black)
                Text(text = name, fontSize = 16.sp, color = Color.Gray)

                // Apellido
                Text(text = "Apellido:", fontSize = 18.sp, color = Color.Black)
                Text(text = lastName, fontSize = 16.sp, color = Color.Gray)

                // Correo electrónico
                Text(text = "Correo electrónico:", fontSize = 18.sp, color = Color.Black)
                Text(text = email, fontSize = 16.sp, color = Color.Gray)

                // Contraseña
                Text(text = "Contraseña:", fontSize = 18.sp, color = Color.Black)
                Text(text = "********", fontSize = 16.sp, color = Color.Gray) // Mostrar como oculto

                // Botón para cambiar la contraseña
                Button(
                    onClick = { /* Lógica para cambiar la contraseña */ },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF7A3CFF)),
                    shape = RoundedCornerShape(20.dp)
                ) {
                    Text(text = "Cambiar Contraseña", color = Color.White)
                }

                // Dirección
                Text(text = "Dirección:", fontSize = 18.sp, color = Color.Black)
                Text(text = address, fontSize = 16.sp, color = Color.Gray)

                // Ciudad
                Text(text = "Ciudad:", fontSize = 18.sp, color = Color.Black)
                Text(text = city, fontSize = 16.sp, color = Color.Gray)

                Spacer(modifier = Modifier.weight(1f)) // Empuja los botones hacia abajo

                // Botones adicionales: Editar Perfil y Cerrar Sesión
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(16.dp) // Espacio entre botones
                ) {
                    Button(
                        onClick = { /* Lógica para editar perfil */ },
                        modifier = Modifier
                            .weight(1f)
                            .height(50.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF7A3CFF)),
                        shape = RoundedCornerShape(20.dp)
                    ) {
                        Text(text = "Editar Perfil", color = Color.White)
                    }

                    Button(
                        onClick = { navController.navigate(Screen.Login.route)},
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