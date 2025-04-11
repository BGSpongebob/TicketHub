package com.example.ticketmag.DTOs

import kotlinx.serialization.Serializable
import java.time.LocalDateTime

data class BasicEventDTO(
    val id: Long? = null,
    val title: String? = null,
    val description: String? = null,
    val etypeId: Long? = null,
    val eventDateTime: LocalDateTime? = null
)