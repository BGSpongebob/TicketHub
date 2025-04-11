package com.example.ticketmag.DTOs

enum class EventType(val id: Long) {
    CONCERT(1L),
    CONFERENCE(2L),
    THEATER(3L),
    SPORTS(4L),
    OTHER(5L);

    companion object {
        fun fromId(id: Long?): String {
            return entries.find { it.id == id }?.name ?: "UNKNOWN"
        }
    }
}