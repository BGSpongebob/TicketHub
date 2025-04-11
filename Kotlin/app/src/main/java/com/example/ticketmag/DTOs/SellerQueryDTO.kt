package com.example.ticketmag.DTOs

data class SellerQueryDTO(
    val seller: SellerDTO? = null,
    val sales: List<SaleDTO>? = null
)
