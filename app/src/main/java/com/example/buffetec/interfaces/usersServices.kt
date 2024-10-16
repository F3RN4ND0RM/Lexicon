package com.example.buffetec.interfaces

import com.example.buffetec.network.RetrofitClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import com.example.buffetec.interfaces.LoginResponse
import com.example.buffetec.network.LoginRequest
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.http.Body
import retrofit2.http.POST

interface UsersServices {
    companion object {
        private val loggingInterceptor = HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }

        private val client = OkHttpClient.Builder()
            .addInterceptor(loggingInterceptor)
            .build()

        val instance: UsersServices = Retrofit.Builder()
            .baseUrl("https://api-proyecto-w0ws.onrender.com/api/")
            .addConverterFactory(GsonConverterFactory.create())
            .client(client)
            .build()
            .create(UsersServices::class.java)
    }

    @POST("login")
    suspend fun login(@Body request: LoginRequest): LoginResponse
}