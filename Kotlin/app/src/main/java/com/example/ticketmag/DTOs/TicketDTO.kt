package com.example.ticketmag.DTOs

data class TicketDTO(
    val id: Long? = null,
    val price: Double = 0.0,
    val ttypeId: Long? = null,
    val eventId: Long? = null
)