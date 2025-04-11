package com.example.ticketmag.ApiServices

import com.example.ticketmag.DTOs.EventDTO
import com.example.ticketmag.DTOs.NotificationDTO
import com.example.ticketmag.DTOs.RatingDTO
import com.example.ticketmag.DTOs.SellerDTO
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface OrganizerApiService {
    @POST("api/events/create")
    suspend fun createEvent(@Body event: EventDTO): EventDTO

    @PUT("api/events/update")
    suspend fun updateEvent(@Body event: EventDTO): EventDTO

    @GET("api/events/getByOrganizerId/{organizerId}")
    suspend fun getEventsByOrganizerId(@Path("organizerId") organizerId: Long): List<EventDTO>

    @GET("api/sellers/getAll")
    suspend fun getAllSellers(): List<SellerDTO>

    @POST("api/ratings/create")
    suspend fun createRating(@Body rating: RatingDTO): RatingDTO

    @PUT("api/ratings/update")
    suspend fun updateRating(@Body rating: RatingDTO): RatingDTO

    @GET("api/ratings/getByOrganizer/{organizerId}")
    suspend fun getRatingsByOrganizerId(@Path("organizerId") organizerId: Long): List<RatingDTO>
}