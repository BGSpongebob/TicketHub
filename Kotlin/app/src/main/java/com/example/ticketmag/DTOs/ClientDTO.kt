package com.example.ticketmag.DTOs

import kotlinx.serialization.Serializable

data class ClientDTO(
    val id: Long? = null,
    val name1: String? = null,
    val name2: String? = null,
    val phone: String? = null
)