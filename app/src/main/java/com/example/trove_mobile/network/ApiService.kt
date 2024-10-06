package com.example.trove_mobile.network


import com.example.trove_mobile.models.api.LoginRequest
import com.example.trove_mobile.models.api.LoginResponse
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

interface ApiService {
    @POST("auth/login")
    fun login(@Body request: LoginRequest): Call<LoginResponse>
}
