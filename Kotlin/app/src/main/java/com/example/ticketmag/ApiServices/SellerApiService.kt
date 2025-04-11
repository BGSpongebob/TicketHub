package com.example.ticketmag.ApiServices

import com.example.ticketmag.DTOs.ClientDTO
import com.example.ticketmag.DTOs.EventDTO
import com.example.ticketmag.DTOs.NotificationDTO
import com.example.ticketmag.DTOs.SaleDTO
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface SellerApiService {
    @POST("api/clients/create")
    suspend fun createClient(@Body client: ClientDTO): ClientDTO

    @GET("api/clients/getAll")
    suspend fun getAllClients(): List<ClientDTO>

    @POST("api/sales/create")
    suspend fun createSale(@Body sale: SaleDTO): SaleDTO

    @GET("api/events/getBySellerId/{sellerId}")
    suspend fun getEventsBySellerId(@Path("sellerId") sellerId: Long): List<EventDTO>
}