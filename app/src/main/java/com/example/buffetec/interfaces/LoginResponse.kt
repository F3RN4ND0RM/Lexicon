package com.example.buffetec.interfaces

data class LoginResponse(
    val token : String?,
    val rol : String?
)
data class RegistrarResponse(
    val msg : String?
)
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

data class UpdateRequest(
    val name: String,
    val surname: String,
    val email: String,
    val address: String,
    val city: String,
)

data class updateResponse(
    val msg : String
)