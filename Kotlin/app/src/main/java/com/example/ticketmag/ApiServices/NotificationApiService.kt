package com.example.ticketmag.ApiServices

import com.example.ticketmag.DTOs.NotificationDTO
import retrofit2.Response
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.PUT
import retrofit2.http.Path

interface NotificationApiService {
    @GET("api/notifications/getByUserId/{userId}")
    suspend fun getNotificationsByUserId(@Path("userId") userId: Long): List<NotificationDTO>

    @PUT("api/notifications/read/{id}")
    suspend fun markNotificationRead(@Path("id") id: Long)

    @DELETE("api/notifications/delete/{id}")
    suspend fun deleteNotification(@Path("id") notificationId: Long): Response<Unit?>
}