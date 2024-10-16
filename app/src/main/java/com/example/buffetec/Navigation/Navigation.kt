package com.example.lazycolumnexample.navigation

sealed class Screen (val route: String){
    object Login : Screen("Login")
    object Signup: Screen("Signup")
    object  Profile : Screen("Profile")
    object Biblioteca : Screen("Biblioteca")
    object Cases : Screen("Cases")
}
