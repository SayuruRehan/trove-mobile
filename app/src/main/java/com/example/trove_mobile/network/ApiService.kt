// IT21171338 - TENNAKOON T. M. T. C.-  API CLIENT

package com.example.trove_mobile.network


import com.example.trove_mobile.models.EcommerceItem
import com.example.trove_mobile.models.api.LoginRequest
import com.example.trove_mobile.models.api.LoginResponse
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST

interface ApiService {
    @POST("auth/login")
    fun login(@Body request: LoginRequest): Call<LoginResponse>

    // Define a GET request to fetch products
    @GET("products")
    fun getProducts(@Header("Authorization") token: String): Call<List<EcommerceItem>>
}
