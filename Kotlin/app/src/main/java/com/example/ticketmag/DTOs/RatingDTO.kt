package com.example.ticketmag.DTOs

data class RatingDTO(
    val id: Long? = null,
    val rating: Int = 0,
    val sellerId: Long? = null,
    val organizerId: Long? = null
)
