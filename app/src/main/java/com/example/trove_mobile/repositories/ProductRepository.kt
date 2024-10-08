package com.example.trove_mobile.repositories

import com.example.trove_mobile.models.EcommerceItem
import com.example.trove_mobile.network.ApiClient
import com.example.trove_mobile.network.ApiService
import retrofit2.Callback

class ProductRepository {
    private val apiService: ApiService = ApiClient.create(ApiService::class.java)

    fun getProducts(token: String, callback: Callback<List<EcommerceItem>>) {
        val call = apiService.getProducts("Bearer $token")
        call.enqueue(callback)
    }
}
