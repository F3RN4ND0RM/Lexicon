package com.example.buffetec.network

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import java.util.concurrent.TimeUnit

object RetrofitClient {

    private const val BASE_URL = "https://api-proyecto-w0ws.onrender.com/api/" // URL de tu API

    // Interceptor para el logging
    private val loggingInterceptor = HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BODY
    }

    // Configuración del cliente HTTP con tiempos de espera extendidos
// Increasing the timeout values
    private val httpClient = OkHttpClient.Builder()
        .addInterceptor(loggingInterceptor)
        .connectTimeout(60, TimeUnit.SECONDS)  // Increased from 30 to 60 seconds
        .readTimeout(60, TimeUnit.SECONDS)     // Increased from 30 to 60 seconds
        .writeTimeout(60, TimeUnit.SECONDS)    // Increased from 30 to 60 seconds
        .build()


    val retrofit: Retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(httpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    val apiService: ApiService by lazy {
        retrofit.create(ApiService::class.java)
    }
}
