package com.example.ticketmag.ViewModels

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ticketmag.ApiServices.OrganizerApiService
import com.example.ticketmag.DTOs.EventDTO
import com.example.ticketmag.DTOs.RatingDTO
import com.example.ticketmag.DTOs.SellerDTO
import kotlinx.coroutines.launch
import retrofit2.HttpException

class OrganizerViewModel(
    private val organizerApiService: OrganizerApiService,
    private val organizerId: Long
) : ViewModel() {
    // List states
    var eventsState by mutableStateOf<UiState<List<EventDTO>>>(UiState.Idle)
        private set
    var sellersState by mutableStateOf<UiState<List<SellerDTO>>>(UiState.Idle)
        private set
    var ratingsState by mutableStateOf<UiState<List<RatingDTO>>>(UiState.Idle)
        private set

    // Operation states
    var eventOperationState by mutableStateOf<UiState<EventDTO>?>(null)
        private set
    var ratingOperationState by mutableStateOf<UiState<RatingDTO>?>(null)
        private set

    init {
        fetchInitialData()
    }

    private fun fetchInitialData() {
        fetchEvents()
        fetchSellers()
        fetchRatings()
    }

    fun fetchEvents() {
        viewModelScope.launch {
            try {
                val currentEvents = (eventsState as? UiState.Success)?.data ?: emptyList()
                val newEvents = organizerApiService.getEventsByOrganizerId(organizerId)

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

    fun createEvent(event: EventDTO) {
        viewModelScope.launch {
            eventOperationState = UiState.Loading
            try {
                val createdEvent = organizerApiService.createEvent(event)
                eventOperationState = UiState.Success(createdEvent)
                fetchEvents() // Refresh list in background
            } catch (e: HttpException) {
                eventOperationState = UiState.Error(e.response()?.errorBody()?.string() ?: "Unknown error creating event")

                println(eventOperationState)
            } catch (e: Exception) {
                eventOperationState = UiState.Error("Network error: ${e.message}")
            }
        }
    }

    fun updateEvent(event: EventDTO) {
        viewModelScope.launch {
            eventOperationState = UiState.Loading
            try {
                val updatedEvent = organizerApiService.updateEvent(event)
                eventOperationState = UiState.Success(updatedEvent)
                fetchEvents() // Refresh list
            } catch (e: HttpException) {
                eventOperationState = UiState.Error(e.response()?.errorBody()?.string() ?: "Unknown error updating event")
            } catch (e: Exception) {
                eventOperationState = UiState.Error("Network error: ${e.message}")
            }
        }
    }

    fun fetchSellers() {
        viewModelScope.launch {
            sellersState = UiState.Loading
            try {
                val sellers = organizerApiService.getAllSellers()
                sellersState = UiState.Success(sellers)
            } catch (e: HttpException) {
                sellersState = UiState.Error(e.response()?.errorBody()?.string() ?: "Unknown error fetching sellers")
            } catch (e: Exception) {
                sellersState = UiState.Error("Network error: ${e.message}")
            }
        }
    }

    fun fetchRatings() {
        viewModelScope.launch {
            ratingsState = UiState.Loading
            try {
                val ratings = organizerApiService.getRatingsByOrganizerId(organizerId)
                ratingsState = UiState.Success(ratings)
            } catch (e: HttpException) {
                ratingsState = UiState.Error(e.response()?.errorBody()?.string() ?: "Unknown error fetching ratings")
            } catch (e: Exception) {
                ratingsState = UiState.Error("Network error: ${e.message}")
            }
        }
    }

    fun createRating(rating: RatingDTO) {
        viewModelScope.launch {
            ratingOperationState = UiState.Loading
            try {
                val createdRating = organizerApiService.createRating(rating)
                ratingOperationState = UiState.Success(createdRating)
                fetchRatings() // Refresh list
            } catch (e: HttpException) {
                ratingOperationState = UiState.Error(e.response()?.errorBody()?.string() ?: "Unknown error creating rating")
            } catch (e: Exception) {
                ratingOperationState = UiState.Error("Network error: ${e.message}")
            }
        }
    }

    fun updateRating(rating: RatingDTO) {
        viewModelScope.launch {
            ratingOperationState = UiState.Loading
            try {
                val updatedRating = organizerApiService.updateRating(rating)
                ratingOperationState = UiState.Success(updatedRating)
                fetchRatings() // Refresh list
            } catch (e: HttpException) {
                ratingOperationState = UiState.Error(e.response()?.errorBody()?.string() ?: "Unknown error updating rating")
            } catch (e: Exception) {
                ratingOperationState = UiState.Error("Network error: ${e.message}")
            }
        }
    }

    // Reset methods
    fun resetEventOperationState() {
        eventOperationState = null
    }

    fun resetRatingOperationState() {
        ratingOperationState = null
    }
}