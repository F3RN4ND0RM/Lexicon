package com.example.buffetec.screens

import Cases
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Book
import androidx.compose.material.icons.filled.Cases
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
import androidx.compose.ui.text.font.FontWeight
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
    val navController = rememberNavController()  // Creamos el NavController
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed) // Control del Drawer
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
                    title = { Text("Buffetec") },
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
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Bienvenido a Buffetec",
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF0D47A1),
            modifier = Modifier.padding(bottom = 16.dp)
        )
        Text(
            text = "Obtén asesoría legal personalizada y consulta tus casos.",
            fontSize = 16.sp,
            color = Color.Gray,
            modifier = Modifier.padding(bottom = 32.dp)
        )
        Button(
            onClick = {  },
            shape = RoundedCornerShape(8.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color(0xFF1976D2)
            ),
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 32.dp)
        ) {
            Text(text = "Pedir Asesoría Legal", color = Color.White)
        }
    }
}

sealed class NavItem(val route: String, val title: String, val icon: ImageVector) {
    object Home : NavItem("home", "Home", Icons.Default.Home)
    object Casos : NavItem("cases", "Casos", Icons.Default.Cases)
    object Perfil : NavItem("profile", "Perfil", Icons.Default.Person)
    object Biblioteca : NavItem("biblioteca", "Biblioteca", Icons.Default.Book)

}