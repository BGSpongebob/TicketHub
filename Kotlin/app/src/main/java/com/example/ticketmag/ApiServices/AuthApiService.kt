package com.example.ticketmag.ApiServices

import com.example.ticketmag.DTOs.TokenResponse
import com.example.ticketmag.DTOs.UserDTO
import retrofit2.http.Body
import retrofit2.http.POST

interface AuthApiService {
    @POST("api/users/register")
    suspend fun registerUser(@Body user: UserDTO): UserDTO

    @POST("api/users/login")
    suspend fun loginUser(@Body login: UserDTO): TokenResponse
}