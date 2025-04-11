package com.example.ticketmag.ApiServices

import com.example.ticketmag.DTOs.EventDTO
import com.example.ticketmag.DTOs.SellerQueryDTO
import retrofit2.http.GET

interface AdminApiService {
    @GET("api/events/getAll")
    suspend fun getAllEvents(): List<EventDTO>

    @GET("api/sellers/getAllWithSales")
    suspend fun getAllSellersWithSales(): List<SellerQueryDTO>
}