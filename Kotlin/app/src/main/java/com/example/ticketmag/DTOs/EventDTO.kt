package com.example.ticketmag.DTOs

import kotlinx.serialization.Serializable
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

data class EventDTO(
    val id: Long? = null,
    val title: String? = null,
    val description: String? = null,
    val basicSeats: Int = 0,
    val premiumSeats: Int = 0,
    val vipSeats: Int = 0,
    val avBasicSeats: Int = 0,
    val avPremiumSeats: Int = 0,
    val avVipSeats: Int = 0,
    val eventDateTime: LocalDateTime? = null,
    val etypeId: Long? = null,
    val organizer: OrganizerDTO? = null,
    val sellers: List<SellerDTO>? = null,
    val tickets: List<TicketDTO>? = null
) {

    private fun parseEventDateTime(): LocalDateTime? =
        eventDateTime?.let { LocalDateTime.parse(it.toString(), DateTimeFormatter.ISO_LOCAL_DATE_TIME) }

    fun getFormattedDate(): String =
        parseEventDateTime()?.format(DateTimeFormatter.ISO_LOCAL_DATE) ?: "No Date"

    fun getFormattedTime(): String =
        parseEventDateTime()?.format(DateTimeFormatter.ofPattern("HH:mm")) ?: "No Time"
}