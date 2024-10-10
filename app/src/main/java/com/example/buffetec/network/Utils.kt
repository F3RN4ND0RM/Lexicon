package com.example.buffetec.network

import android.content.SharedPreferences

// Función para guardar el token JWT en SharedPreferences
fun saveTokenLocally(token: String?, sharedPreferences: SharedPreferences) {
    val editor = sharedPreferences.edit()
    editor.putString("jwt_token", token)
    editor.apply()
}
