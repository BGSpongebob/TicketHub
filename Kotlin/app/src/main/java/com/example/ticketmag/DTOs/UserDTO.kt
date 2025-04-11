package com.example.ticketmag.DTOs

data class UserDTO(
    val id: Long? = null,
    val username: String? = null,
    val password: String? = null,
    val roleId: Long? = null,
    val organizer: OrganizerDTO? = null,
    val seller: SellerDTO? = null
)