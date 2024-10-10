package com.example.buffetec.network

import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

// Define el cuerpo para el registro y login
data class RegisterRequest(
    val name: String,
    val surname: String,
    val email: String,
    val pwd: String,
    val address: String,
    val neighborhood: String,
    val city: String,
    val state: String,
    val cp: String
)

data class LoginRequest(val email: String, val password: String)

// Define las respuestas
data class RegisterResponse(val message: String, val token: String)
data class LoginResponse(val message: String, val token: String)

// Define las solicitudes HTTP que hará la app
interface ApiService {
    @POST("/register")
    fun registerUser(@Body request: RegisterRequest): Call<RegisterResponse>

    @POST("/login")
    fun loginUser(@Body request: LoginRequest): Call<LoginResponse>
}
