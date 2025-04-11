package com.example.ticketmag.DTOs
import java.time.LocalDate

data class SaleDTO(
    val id: Long? = null,
    val saleDate: LocalDate? = null,
    val seller: Long? = null,
    val client: ClientDTO? = null,
    val event: BasicEventDTO? = null,
    val salesTickets: List<SaleTicketDTO>? = null
)
