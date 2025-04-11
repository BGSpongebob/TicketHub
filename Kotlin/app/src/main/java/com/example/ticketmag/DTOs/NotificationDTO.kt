package com.example.ticketmag.DTOs
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

data class NotificationDTO(
    val id: Long? = null,
    val title: String? = null,
    val text: String? = null,
    val isRead: Boolean? = null,
    val createdAt: LocalDateTime? = null,
    val userId: Long? = null
){
    fun getFormattedDateTime(): String {
        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")
        return createdAt?.format(formatter) ?: "blank datetime"
    }
}