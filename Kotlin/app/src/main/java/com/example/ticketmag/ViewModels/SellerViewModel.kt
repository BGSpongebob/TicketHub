package com.example.ticketmag.ViewModels

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ticketmag.ApiServices.SellerApiService
import com.example.ticketmag.DTOs.ClientDTO
import com.example.ticketmag.DTOs.EventDTO
import com.example.ticketmag.DTOs.SaleDTO
import kotlinx.coroutines.launch
import retrofit2.HttpException

class SellerViewModel(
    private val sellerApiService: SellerApiService,
    private val sellerId: Long
) : ViewModel() {
    // List states
    var clientsState by mutableStateOf<UiState<List<ClientDTO>>>(UiState.Idle)
        private set
    var eventsState by mutableStateOf<UiState<List<EventDTO>>>(UiState.Idle)
        private set

    // Operation states
    var clientOperationState by mutableStateOf<UiState<ClientDTO>?>(null)
        private set
    var saleOperationState by mutableStateOf<UiState<SaleDTO>?>(null)
        private set

    init {
        fetchInitialData()
    }

    private fun fetchInitialData() {
        fetchClients()
        fetchEvents()
    }

    fun fetchClients() {
        viewModelScope.launch {
            clientsState = UiState.Loading
            try {
                val clients = sellerApiService.getAllClients()
                clientsState = UiState.Success(clients)
            } catch (e: HttpException) {
                clientsState = UiState.Error(e.response()?.errorBody()?.string() ?: "Unknown error fetching clients")
            } catch (e: Exception) {
                clientsState = UiState.Error("Network error: ${e.message}")
            }
        }
    }

    fun createClient(client: ClientDTO) {
        viewModelScope.launch {
            clientOperationState = UiState.Loading
            try {
                val createdClient = sellerApiService.createClient(client)
                clientOperationState = UiState.Success(createdClient)
                fetchClients() // Refresh list
            } catch (e: HttpException) {
                clientOperationState = UiState.Error(e.response()?.errorBody()?.string() ?: "Unknown error creating client")
            } catch (e: Exception) {
                clientOperationState = UiState.Error("Network error: ${e.message}")
            }
        }
    }

    fun createSale(sale: SaleDTO) {
        viewModelScope.launch {
            saleOperationState = UiState.Loading
            try {
                val createdSale = sellerApiService.createSale(sale)
                saleOperationState = UiState.Success(createdSale)
                fetchEvents()
            } catch (e: HttpException) {
                saleOperationState = UiState.Error(e.response()?.errorBody()?.string() ?: "Unknown error creating sale")
            } catch (e: Exception) {
                saleOperationState = UiState.Error("Network error: ${e.message}")
            }
        }
    }

    fun fetchEvents() {
        viewModelScope.launch {
            try {
                val currentEvents = (eventsState as? UiState.Success)?.data ?: emptyList()
                val newEvents = sellerApiService.getEventsBySellerId(sellerId)

                // check if any data has changed
                val hasChanged = currentEvents.size != newEvents.size ||
                        !currentEvents.sortedBy { it.id }.zip(newEvents.sortedBy { it.id })
                            .all { (a, b) -> a == b }

                if (hasChanged) {
                    eventsState = UiState.Success(newEvents)
                }
            } catch (e: HttpException) {
                eventsState = UiState.Error(e.response()?.errorBody()?.string() ?: "Unknown error fetching events")
            } catch (e: Exception) {
                eventsState = UiState.Error("Network error: ${e.message}")
            }
        }
    }


    // Reset methods
    fun resetClientOperationState() {
        clientOperationState = null
    }

    fun resetSaleOperationState() {
        saleOperationState = null
    }

}