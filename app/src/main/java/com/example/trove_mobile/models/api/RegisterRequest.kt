// IT21171338 - TENNAKOON T. M. T. C.-  LOGIN REQUEST

package com.example.trove_mobile.models.api

data class RegisterRequest(
    val firstname: String,
    val lastname: String,
    val email: String,
    val phone: String,
    val password: String,
    val role: String,
    val status: String
)
