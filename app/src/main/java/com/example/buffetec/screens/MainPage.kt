package com.example.buffetec.screens

import CaseDetail
import Cases
import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AssignmentInd
import androidx.compose.material.icons.filled.Book
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainPage(navController: NavHostController) {
    val navController = rememberNavController()
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            DrawerContent(
                navController = navController,
                closeDrawer = { scope.launch { drawerState.close() } }
            )
        },
                scrimColor = Color.White 

    ) {
        Scaffold(
            topBar = {
                TopAppBar(
                    title = { Text("Bufetec") },
                    navigationIcon = {
                        IconButton(onClick = { scope.launch { drawerState.open() } }) {
                            Icon(Icons.Default.Menu, contentDescription = "Abrir Drawer")  // Cambiamos al ícono de menú
                        }
                    }
                )
            }
        ) { paddingValues ->
            // Configuración del NavHost para manejar las rutas de navegación
            NavHost(
                navController = navController,
                startDestination = NavItem.Home.route,  // Pantalla inicial
                modifier = Modifier.padding(paddingValues)
            ) {
                composable(NavItem.Casos.route) {
                    Cases(navController)  // Llamada a la pantalla de Casos
                }
                composable(NavItem.Perfil.route) {
                    Profile(navController)  // Llamada a la pantalla de Perfil
                }
                composable(NavItem.Home.route) {
                    Home()  // Llamada a la pantalla de Citas
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
}

@Composable
fun DrawerContent(navController: NavHostController, closeDrawer: () -> Unit) {
    val items = listOf(NavItem.Home, NavItem.Casos, NavItem.Biblioteca, NavItem.Perfil)

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.Start
    ) {
        Text(
            text = "Opciones",
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(bottom = 16.dp)
        )
        items.forEach { item ->
            NavigationDrawerItem(
                label = { Text(text = item.title) },
                selected = false,
                onClick = {
                    navController.navigate(item.route)
                    closeDrawer()
                },
                icon = {
                    Icon(imageVector = item.icon, contentDescription = item.title)
                }
            )
            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}

@Composable
fun Home() {
    val scrollState = rememberScrollState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(scrollState)
            .padding(16.dp),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Welcome Header
        Text(
            text = "Bienvenido a Bufetec",
            fontSize = 28.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF1976D2),
            modifier = Modifier.padding(bottom = 8.dp)
        )
        Text(
            text = "Centro de Asistencia Jurídica Gratuita del Tecnológico de Monterrey",
            fontSize = 18.sp,
            textAlign = TextAlign.Center,
            color = Color.Gray,
            modifier = Modifier.padding(bottom = 32.dp)
        )

        // Services Section
        Text(
            text = "Nuestros Servicios",
            fontSize = 22.sp,
            fontWeight = FontWeight.Bold,
            color = Color.Black,
            modifier = Modifier.padding(bottom = 16.dp)
        )
        ServicesSection()

        Spacer(modifier = Modifier.height(24.dp))

        // Testimonials Section
        Text(
            text = "Testimonios",
            fontSize = 22.sp,
            fontWeight = FontWeight.Bold,
            color = Color.Black,
            modifier = Modifier.padding(bottom = 16.dp)
        )
        TestimonialsSection()

        Spacer(modifier = Modifier.height(32.dp))

        // Call to Action Button
        Button(
            onClick = { /* Action */ },
            shape = RoundedCornerShape(8.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color(0xFF1976D2)
            ),
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 32.dp)
                .background(Color.White, shape = RoundedCornerShape(8.dp))
        ) {
            Text(text = "Solicitar Asesoría Legal", color = Color.White)
        }
    }
}

@Composable
fun ServicesSection() {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(16.dp),
        modifier = Modifier
            .padding(horizontal = 16.dp)
            .fillMaxWidth()
    ) {
        ServiceItem(
            "Consulta de Casos",
            "Accede y consulta el estado de tus casos en cualquier momento.",
            Icons.Default.AssignmentInd
        )
        ServiceItem(
            "Asesoría Personalizada",
            "Nuestros abogados están aquí para brindarte asesoría legal personalizada.",
            Icons.Default.Person
        )
        ServiceItem(
            "Biblioteca Legal",
            "Obtén ayuda con toda la información legal actualizada al día.",
            Icons.Default.Book
        )
    }
}

fun sendSms(context: Context) {
    val smsIntent = Intent(Intent.ACTION_SENDTO).apply {
        data = Uri.parse("smsto:81 8328 4344")
        putExtra("sms_body", "👋 ¡Hola! Me gustaría recibir asesoría personalizada sobre mi caso. 📜💼 ¿Podrían ayudarme, por favor?")
    }

    // Intentar iniciar la actividad
    try {
        context.startActivity(smsIntent)
    } catch (e: Exception) {
        // Manejo de errores
        Toast.makeText(context, "No se encontró ninguna aplicación de mensajería.", Toast.LENGTH_SHORT).show()
    }
}

@Composable
fun ServiceItem(title: String, description: String, icon: ImageVector) {
    val context = LocalContext.current

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .fillMaxWidth()
            .background(Color(0xFFF0F0F0), shape = RoundedCornerShape(8.dp))
            .padding(16.dp)
            .clickable {
                if (title == "Asesoría Personalizada") {
                    sendSms(context)
                }
            }
    ) {
        Icon(icon, contentDescription = title, modifier = Modifier.size(40.dp), tint = Color(0xFF1976D2))
        Text(text = title, fontSize = 20.sp, fontWeight = FontWeight.Bold, color = Color.Black)
        Text(text = description, fontSize = 14.sp, color = Color.Gray, modifier = Modifier.padding(8.dp))
    }
}

@SuppressLint("QueryPermissionsNeeded")
fun sendEmail(context: Context) {
    val emailIntent = Intent(Intent.ACTION_SENDTO).apply {
        data = Uri.parse("mailto:") // Solo se permiten aplicaciones de correo
        putExtra(Intent.EXTRA_EMAIL, arrayOf("correo@example.com")) // Cambia esto por el correo deseado
        putExtra(Intent.EXTRA_SUBJECT, "Asesoría Personalizada")
        putExtra(Intent.EXTRA_TEXT, "Por favor, incluya aquí su consulta.")
    }
    if (emailIntent.resolveActivity(context.packageManager) != null) {
        context.startActivity(emailIntent)
    }
}

@Composable
fun TestimonialsSection() {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(16.dp),
        modifier = Modifier.padding(horizontal = 16.dp)
    ) {
        TestimonialItem(
            author = "Juan Pérez",
            content = "Buffetec me ayudó a resolver un conflicto legal rápidamente. Excelente atención y profesionalismo."
        )
        TestimonialItem(
            author = "María González",
            content = "El equipo de abogados fue muy claro y me ofrecieron soluciones efectivas. Recomiendo ampliamente el servicio."
        )
        TestimonialItem(
            author = "Carlos Ramírez",
            content = "Gracias a Buffetec, pude resolver mi situación legal con facilidad. Siempre estuvieron dispuestos a ayudar."
        )
    }
}

@Composable
fun TestimonialItem(author: String, content: String) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color(0xFFF5F5F5), shape = RoundedCornerShape(8.dp))
            .padding(16.dp)
    ) {
        Text(text = content, fontSize = 16.sp, color = Color.Gray)
        Spacer(modifier = Modifier.height(8.dp))
        Text(text = "- $author", fontSize = 14.sp, fontWeight = FontWeight.Bold, color = Color(0xFF1976D2))
    }
}
sealed class NavItem(val route: String, val title: String, val icon: ImageVector) {
    object Home : NavItem("home", "Home", Icons.Default.Home)
    object Casos : NavItem("cases", "Casos", Icons.Default.AssignmentInd)
    object Perfil : NavItem("profile", "Perfil", Icons.Default.Person)
    object Biblioteca : NavItem("biblioteca", "Biblioteca", Icons.Default.Book)
}