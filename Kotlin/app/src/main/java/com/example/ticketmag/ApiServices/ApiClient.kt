package com.example.ticketmag.ApiServices

import android.os.Build
import com.example.ticketmag.DTOs.Adapters.LocalDateAdapter
import com.example.ticketmag.DTOs.Adapters.LocalDateTimeAdapter
import com.google.gson.GsonBuilder
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.time.LocalDate
import java.time.LocalDateTime

class ApiClient(
    private val tokenProvider: () -> String?,
    private val onTokenExpired: (() -> Unit)? = null // Callback for token expiration
    ) {

    val EMULATOR_URL = "http://10.0.2.2:8080/"
    val HOME_URL = "http://192.168.1.2:8080/"
    val REAL_URL = "https://lioness-knowing-distinctly.ngrok-free.app/"

    private fun getTestUrl(): String {
        return if (Build.PRODUCT?.contains("sdk") == true) { // Emulator check
            EMULATOR_URL
        } else {
            HOME_URL
        }
    }

    private val okHttpClient = OkHttpClient.Builder()
        .addInterceptor { chain: Interceptor.Chain ->
            val original = chain.request()
            val token = tokenProvider() ?: ""
            val request = original.newBuilder()
                .header("Authorization", "Bearer $token")
                .build()
            val response = chain.proceed(request)
            if (response.code() == 401) {
                onTokenExpired?.invoke() // Notify that token expired
            }
            response // Return response without retrying
        }
        .build()



    private val gson = GsonBuilder()
        .registerTypeAdapter(LocalDateTime::class.java, LocalDateTimeAdapter())
        .registerTypeAdapter(LocalDate::class.java, LocalDateAdapter())
        .create()

    private val retrofit = Retrofit.Builder()
        .baseUrl(getTestUrl()) //input url
        .client(okHttpClient)
        .addConverterFactory(GsonConverterFactory.create(gson)) // Pass custom Gson instance
        .build()

    val authApi: AuthApiService = retrofit.create(AuthApiService::class.java)
    val sellerApi: SellerApiService = retrofit.create(SellerApiService::class.java)
    val organizerApi: OrganizerApiService = retrofit.create(OrganizerApiService::class.java)
    val notificationApi: NotificationApiService = retrofit.create(NotificationApiService::class.java)
    val adminApi: AdminApiService = retrofit.create(AdminApiService::class.java)
}
