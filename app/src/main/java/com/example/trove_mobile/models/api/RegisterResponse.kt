// IT21171338 - TENNAKOON T. M. T. C.-  LOGIN RESPONSE

package com.example.trove_mobile.models.api

data class RegisterResponse(
    val token: String,
    val userId: String,
    val message: String
)
