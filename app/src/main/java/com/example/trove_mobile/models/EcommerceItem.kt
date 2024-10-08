package com.example.trove_mobile.models

import java.io.Serializable

data class EcommerceItem(
    val id: String,
    val productName: String,
    val description: String,
    val productPrice: Double,
    val stock: Int,
    val imageUrl: String
) : Serializable
