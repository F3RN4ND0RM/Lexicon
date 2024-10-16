package com.example.buffetec.network

import retrofit2.Call

import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

data class User(
    val id: String,
    val name: String,
    val surname: String,
    val rol: String,
    val email: String,
    val address: String,
    val neighborhood: String,
    val city: String,
    val state: String,
    val cp: String,
    val phone: String
)


data class RoleUpdateRequest(
    val id: String,
    val rol: String
)


data class LoginRequest(
    val email: String,
    val password: String
)

data class LoginResponse(
    val message: String,
    val token: String
)

data class RegisterRequest(
    val name: String,
    val surname: String,
    val gender: String,
    val email: String,
    val address: String,
    val neighborhood: String,
    val password: String,
    val city: String,
    val state: String,
    val cp: String,
    val phone: String,
    val rol: String,
    val AUP: Boolean
)

// API Service
interface ApiService {

    // Login de usuario (sin necesidad de token aún)
    @POST("/api/login")
    fun loginUser(@Body loginRequest: LoginRequest): Call<LoginResponse>

    // Método para registro de usuario
    @POST("/api/users")
    fun registerUser(@Body registerRequest: RegisterRequest): Call<Void>

    // Obtener la lista de usuarios (requiere el token en el encabezado de autorización)
    @GET("/api/users")
    fun getUsers(@Header("Authorization") token: String): Call<List<User>>

    // Actualizar el rol de un usuario (requiere el token en el encabezado de autorización)
    @POST("/api/rol")
    fun updateUserRole(@Header("Authorization") token: String, @Body roleUpdateRequest: RoleUpdateRequest): Call<Void>

    // Método para obtener el perfil del usuario usando solo el token
    @GET("/api/usersbyid")  // Ajusta el endpoint según tu API
    fun getUserById(
        @Header("Authorization") token: String  // Paso del token en el header
    ): Call<User>
}

import retrofit2.http.GET
import retrofit2.http.Path

data class ApiResponse(
    val id: String,
    val url: String,
    val created_at: String,
    val output: Output
)

data class Output(
    val quality: Int,
    val documents: List<String>,
    val variables: Map<String, Any>,
    val max_tokens: Int,
    val references: List<Reference>
)

// Nueva data class para las referencias
data class Reference(
    val url: String,
    val title: String,
    val snippet: String
)

interface ApiService {
    @GET("biblioteca/{category}")
    fun searchArticles(@Path("category") category: String): Call<ApiResponse>
}
