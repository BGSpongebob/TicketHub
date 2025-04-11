package com.example.ticketmag.ViewModels

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ticketmag.ApiServices.AdminApiService
import com.example.ticketmag.DTOs.EventDTO
import com.example.ticketmag.DTOs.SellerQueryDTO
import kotlinx.coroutines.launch
import retrofit2.HttpException

class AdminViewModel(
    private val adminApiService: AdminApiService
) : ViewModel() {
    var eventsState by mutableStateOf<UiState<List<EventDTO>>>(UiState.Idle)
        private set
    var sellersWithSalesState by mutableStateOf<UiState<List<SellerQueryDTO>>>(UiState.Idle )
        private set

    init {
        fetchInitialData()
    }

    private fun fetchInitialData() {
        fetchAllEvents()
        fetchAllSellersWithSales()
    }

    fun fetchAllEvents() {
        viewModelScope.launch {
            eventsState = UiState.Loading
            try {
                val events = adminApiService.getAllEvents()
                eventsState = UiState.Success(events)
            } catch (e: HttpException) {
                val errorMessage = e.response()?.errorBody()?.string() ?: "Unknown error fetching events"
                eventsState = UiState.Error(errorMessage) // e.g., "Unexpected error while retrieving events: ..."
            } catch (e: Exception) {
                eventsState = UiState.Error("Network error: ${e.message}")
            }
        }
    }

    fun fetchAllSellersWithSales() {
        viewModelScope.launch {
            sellersWithSalesState = UiState.Loading
            try {
                val sellersWithSales = adminApiService.getAllSellersWithSales()
                sellersWithSalesState = UiState.Success(sellersWithSales)
            } catch (e: HttpException) {
                val errorMessage = e.response()?.errorBody()?.string() ?: "Unknown error fetching sellers with sales"
                sellersWithSalesState = UiState.Error(errorMessage) // e.g., "Unexpected error while retrieving sellers with sales: ..."
            } catch (e: Exception) {
                sellersWithSalesState = UiState.Error("Network error: ${e.message}")
            }
        }
    }
}