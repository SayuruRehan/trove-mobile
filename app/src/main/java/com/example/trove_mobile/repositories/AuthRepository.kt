// IT21171338 - TENNAKOON T. M. T. C.-  AUTH REPOSITORY

package com.example.trove_mobile.repositories

import com.example.trove_mobile.models.api.LoginRequest
import com.example.trove_mobile.models.api.LoginResponse
import com.example.trove_mobile.network.ApiClient
import com.example.trove_mobile.network.ApiService
import retrofit2.Callback

class AuthRepository {
    private val apiService: ApiService = ApiClient.create(ApiService::class.java)

    fun login(request: LoginRequest, callback: Callback<LoginResponse>) {
        val call = apiService.login(request)
        call.enqueue(callback)
    }
}
