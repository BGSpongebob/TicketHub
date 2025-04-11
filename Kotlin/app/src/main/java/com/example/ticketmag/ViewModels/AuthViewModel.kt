package com.example.ticketmag.ViewModels

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ticketmag.ApiServices.AuthApiService
import com.example.ticketmag.DTOs.TokenResponse
import com.example.ticketmag.DTOs.UserDTO
import kotlinx.coroutines.launch
import retrofit2.HttpException

class AuthViewModel(
    private val authApiService: AuthApiService
) : ViewModel() {
    var authState by mutableStateOf<UiState<TokenResponse>>(UiState.Idle)
        private set

    private var token: String? by mutableStateOf(null)
    val currentToken: String? get() = token

    private var user: UserDTO? by mutableStateOf(null)
    val currentUser: UserDTO? get() = user

    private var roleUserId: Long? by mutableStateOf(null)
    val currentRoleUserId: Long? get() = roleUserId

    fun register(userDTO: UserDTO) {
        viewModelScope.launch {
            authState = UiState.Loading
            try {
                val registeredUser = authApiService.registerUser(userDTO)
                authState = UiState.Success(TokenResponse(token = null, user = registeredUser))

                registeredUser.username?.let { registeredUser.password?.let { it1 ->
                    login(it,
                        it1
                    )
                } } // Automatically log in after registration
            } catch (e: HttpException) {
                val errorMessage = e.response()?.errorBody()?.string() ?: "Unknown registration error"
                authState = UiState.Error(errorMessage) // e.g., "Duplicate error while registering user: ..."
            } catch (e: Exception) {
                authState = UiState.Error("Network error: ${e.message}")
            }
        }
    }

    fun login(username: String, password: String) {
        viewModelScope.launch {
            authState = UiState.Loading
            try {
                val loginDTO = UserDTO(username = username, password = password)
                val response = authApiService.loginUser(loginDTO)
                token = response.token // Store token for API calls
                user = response.user //Store user

                // Set roleUserId based on roleId
                response.user?.let { user ->
                    roleUserId = when (user.roleId) {
                        1L -> { // Organizer
                            user.organizer?.id ?: throw IllegalStateException("Organizer ID missing")
                        }

                        2L -> { // Seller
                            user.seller?.id ?: throw IllegalStateException("Seller ID missing")
                        }

                        3L -> { // Admin
                            -1L
                        }

                        else -> {
                            throw IllegalStateException("Unknown role ID: ${user.roleId}")
                        }
                    }
                } ?: throw IllegalStateException("User data missing in login response")

                authState = UiState.Success(response)

            } catch (e: HttpException) {
                val errorMessage = e.response()?.errorBody()?.string() ?: "Unknown login error"
                authState = UiState.Error(errorMessage) // e.g., "User failed to log in because of invalid credentials."
            } catch (e: Exception) {
                authState = UiState.Error("Network error: ${e.message}")
            }
        }
    }

    fun logout() {
        token = null // Clear the token
        user = null // clear user
        roleUserId = null // clear organizer/seller ID
        authState = UiState.Idle
    }

    fun onTokenExpired() {
        logout()
        authState = UiState.Error("Your session has expired. Please relog.") // Trigger UI to show message
    }

    fun resetAuthState() {
        authState = UiState.Idle
    }
}