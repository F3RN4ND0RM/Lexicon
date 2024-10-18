package com.example.buffetec.interfaces

import com.example.buffetec.network.RetrofitClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import com.example.buffetec.interfaces.LoginResponse
import com.example.buffetec.network.LoginRequest
import com.example.buffetec.network.RegisterRequest
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.PUT

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

    @POST("users")
    suspend fun registrar(@Body request: RegisterRequest): RegistrarResponse

    @GET("usersbyid")
    suspend fun userbyid(@Header("authToken") authToken: String) : User

    @GET("users")
    suspend fun allUsers(@Header("authToken") authToken: String) : List<usersALL>

    @PUT("updateuser")
    suspend fun updateuser(@Header("authToken") authToken: String, @Body request: UpdateRequest) : updateResponse

    @PUT("rol")
    suspend fun updaterol(@Header("authToken") authToken: String, @Body request: updateRolrequest) : updateResponse


}