package com.example.ticketmag.DTOs

data class TokenResponse(
    val token: String? = null,
    val user: UserDTO? = null
)