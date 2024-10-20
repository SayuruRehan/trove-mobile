// IT21171338 - TENNAKOON T. M. T. C.-  API CLIENT

package com.example.trove_mobile.network


import com.example.trove_mobile.models.EcommerceItem
import com.example.trove_mobile.models.api.LoginRequest
import com.example.trove_mobile.models.api.LoginResponse
import com.example.trove_mobile.models.api.RegisterRequest
import com.example.trove_mobile.models.api.RegisterResponse
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST

interface ApiService {
    //Auth
    @POST("auth/login")
    fun login(@Body request: LoginRequest): Call<LoginResponse>

    @POST("auth/register")
    fun signup(@Body request: RegisterRequest): Call<RegisterResponse>

    //Products
    @GET("products")
    fun getProducts(@Header("Authorization") token: String): Call<List<EcommerceItem>>
}
