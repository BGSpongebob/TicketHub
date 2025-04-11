package com.example.ticketmag.ViewModels

import android.content.Context
import android.media.MediaPlayer
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ticketmag.ApiServices.NotificationApiService
import com.example.ticketmag.DTOs.NotificationDTO
import com.example.ticketmag.R
import kotlinx.coroutines.launch
import retrofit2.HttpException

class NotificationViewModel(
    private val notificationApiService: NotificationApiService,
    private val userId: Long,
    private val context: Context // Add context to initialize MediaPlayer
) : ViewModel() {
    var notificationsState by mutableStateOf<UiState<List<NotificationDTO>>>(UiState.Idle)
        private set

    var notificationOperationState by mutableStateOf<UiState<Unit>?>(null)
        private set

    var unreadCount by mutableIntStateOf(0)
        private set

    private val mediaPlayer: MediaPlayer = MediaPlayer.create(context, R.raw.notif)

    init {
        fetchInitialData()
    }

    override fun onCleared() {
        super.onCleared()
        mediaPlayer.release() // Release MediaPlayer resources when ViewModel is cleared
    }

    private fun fetchInitialData() {
        fetchNotifications()
    }

    fun fetchNotifications() {
        viewModelScope.launch {
            try {
                val currentNotifications = (notificationsState as? UiState.Success)?.data ?: emptyList()
                val newNotifications = notificationApiService.getNotificationsByUserId(userId)
                val newUnreadCount = newNotifications.count { !it.isRead!! }

                if (newNotifications != currentNotifications || newUnreadCount != unreadCount) {
                    // Check if there are new unread notifications
                    val hasNewNotifications = newNotifications.any { notification ->
                        !notification.isRead!! && !currentNotifications.any { it.id == notification.id }
                    }

                    if (hasNewNotifications && !mediaPlayer.isPlaying) {
                        mediaPlayer.start() // Play sound when new unread notifications are detected
                    }

                    notificationsState = UiState.Success(newNotifications)
                    unreadCount = newUnreadCount
                }
            } catch (e: HttpException) {
                notificationsState = UiState.Error(e.response()?.errorBody()?.string() ?: "Unknown error fetching notifications")
            } catch (e: Exception) {
                notificationsState = UiState.Error("Network error: ${e.message}")
            }
        }
    }

    fun markNotificationRead(notificationId: Long) {
        viewModelScope.launch {
            notificationOperationState = UiState.Loading
            try {
                notificationApiService.markNotificationRead(notificationId)
                notificationOperationState = UiState.Success(Unit)
                fetchNotifications()
            } catch (e: HttpException) {
                notificationOperationState = UiState.Error(e.response()?.errorBody()?.string() ?: "Unknown error marking notification")
            } catch (e: Exception) {
                notificationOperationState = UiState.Error("Network error: ${e.message}")
            }
        }
    }

    fun deleteNotification(notificationId: Long) {
        viewModelScope.launch {
            notificationOperationState = UiState.Loading
            try {
                notificationApiService.deleteNotification(notificationId)
                notificationOperationState = UiState.Success(Unit)
                fetchNotifications()
            } catch (e: HttpException) {
                notificationOperationState = UiState.Error(e.response()?.errorBody()?.string() ?: "Unknown error deleting notification")
            } catch (e: Exception) {
                notificationOperationState = UiState.Error("Network error: ${e.message}")
            }
        }
    }
}