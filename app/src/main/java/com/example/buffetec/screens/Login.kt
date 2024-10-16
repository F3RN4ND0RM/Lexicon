package com.example.buffetec.screens

import android.app.Application
import android.content.Context
import android.util.Log
    import android.widget.Toast
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.snapping.SnapPosition
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.layout.VerticalAlignmentLine
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.buffetec.Components.ButtonComponent
import com.example.buffetec.R
import com.example.buffetec.interfaces.UsersServices
import com.example.buffetec.network.LoginRequest
import com.example.buffetec.network.LoginResponse
import com.example.buffetec.network.RetrofitClient
import com.example.buffetec.ui.theme.lexendFontFamily
import com.example.lazycolumnexample.navigation.Screen
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.buffetec.viewmodels.LoginState
import androidx.compose.runtime.rememberCoroutineScope
import  com.example.buffetec.viewmodels.UsersViewModel
import com.example.buffetec.Components.InputField

import kotlinx.coroutines.launch


@Composable
fun Login(navController: NavHostController){


    val user = remember { mutableStateOf("")}
    val password = remember { mutableStateOf("")}
    val context = LocalContext.current
    val userViewModel = remember { UsersViewModel(UsersServices.instance, context.applicationContext as Application) }
    val coroutineScope = rememberCoroutineScope()
    val loginState by userViewModel.loginState.collectAsState()



    Column(
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight()
            .background(Color(0xFF622CFF))
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(150.dp)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp)
            ) {
                Text(
                    text = "Buen Día",
                    color = Color.White,
                    modifier = Modifier.align(Alignment.Center),
                    fontFamily = lexendFontFamily,
                    style = TextStyle( fontWeight = FontWeight.ExtraLight, fontSize = 50.sp)
                )
            }

        }
        Row(
            modifier = Modifier
                .fillMaxSize()
                .fillMaxWidth()
                .padding(35.dp, 15.dp)

        ) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .height(200.dp)
                    .background(Color.White, shape = RoundedCornerShape(20.dp))
            ){
                Column (
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(20.dp, 80.dp)

                ){

                    Image(
                        painter = painterResource(id = R.drawable.logo),
                        contentDescription = "logo",
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(70.dp)

                    )
                    Spacer(modifier = Modifier
                        .height(50.dp))

                    InputField(label = "Usuario", value = user.value , onValueChange = {user.value = it}, lexendFontFam = lexendFontFamily, visualTrans =  false)
                    Spacer(modifier = Modifier.height(30.dp))
                    InputField(label = "Contraseña", value = password.value , onValueChange = {password.value = it}, lexendFontFam = lexendFontFamily , visualTrans = true)
                    Spacer(modifier = Modifier.height(30.dp))
                    ButtonComponent(label = "Log In", onClick = {
                        coroutineScope.launch {
                            userViewModel.login(user.value, password.value)
                        }
                    })

                    Spacer(modifier = Modifier
                        .height(5.dp))

                    Text(text = "Registrarme",
                        fontFamily = lexendFontFamily,
                        fontWeight = FontWeight.ExtraLight,
                        textAlign = TextAlign.Center,
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable(onClick = {
                                navController.navigate(Screen.Signup.route)
                            })
                    )

                    Spacer(modifier = Modifier
                        .height(5.dp))
                    Text(text = "Entrar como Invitado",
                        fontFamily = lexendFontFamily,
                        fontWeight = FontWeight.ExtraLight,
                        textAlign = TextAlign.Center,
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable(onClick = {
                                navController.navigate(Screen.Biblioteca.route)
                            })
                    )


                    // Navigation effect when login is successful
                    LaunchedEffect(loginState) {
                        if (loginState is LoginState.Success) {
                            navController.navigate(Screen.Profile.route) // Replace with your desired route
                        }
                        if (loginState is LoginState.Error) {
                            Toast.makeText(context,"Usuario o contraseña incorrecto",Toast.LENGTH_SHORT).show()

                        }
                    }
                }
            }
        }


    }


}

