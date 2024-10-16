package com.example.buffetec.network

import retrofit2.Call
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
