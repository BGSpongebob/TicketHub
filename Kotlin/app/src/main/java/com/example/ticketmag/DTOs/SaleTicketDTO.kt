package com.example.ticketmag.DTOs

data class SaleTicketDTO(
    val id: Long? = null,
    val saleId: Long? = null,
    val ticket: TicketDTO? = null,
    val quantity: Int = 0
)
