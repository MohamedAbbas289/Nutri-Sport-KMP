package com.nutrisport.shared.domain

import kotlinx.serialization.Serializable

@Serializable
data class Customer(
    val id: String,
    val firstName: String,
    val lastName: String,
    val email: String,
    val city: String? = null,
    val postalCode: Int? = null,
    val address: String? = null,
    val phoneNumber: PhoneNumber? = null,
    val cartItem: List<CartItem> = emptyList()
)

@Serializable
data class PhoneNumber(
    val countryCode: Int,
    val number: String
)
