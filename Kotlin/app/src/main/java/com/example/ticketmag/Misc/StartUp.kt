package com.example.ticketmag.Misc

import android.content.Context
import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.ticketmag.ApiServices.ApiClient
import com.example.ticketmag.UI.Screens.AdminScreen
import com.example.ticketmag.UI.Screens.HomeScreen
import com.example.ticketmag.UI.Screens.Login.LoginScreen
import com.example.ticketmag.ViewModels.AdminViewModel
import com.example.ticketmag.ViewModels.AuthViewModel
import com.example.ticketmag.ViewModels.NotificationViewModel
import com.example.ticketmag.ViewModels.OrganizerViewModel
import com.example.ticketmag.ViewModels.SellerViewModel

class StartUp {
    private lateinit var apiClient: ApiClient
    private val authViewModel = AuthViewModel(ApiClient({ null }).authApi) // Temporary client for auth
    private var organizerViewModel: OrganizerViewModel? = null
    private var sellerViewModel: SellerViewModel? = null
    private var notificationViewModel: NotificationViewModel? = null
    private var adminViewModel: AdminViewModel? = null

    @Composable
    fun Start(context: Context) {
        val navController = rememberNavController()
        NavHost(navController = navController, startDestination = "login") {
            composable("login") {
                LoginScreen(
                    authViewModel = authViewModel,
                    onLoginSuccess = {
                        // Initialize API client after login
                        apiClient = ApiClient(
                            { authViewModel.currentToken },
                            authViewModel::onTokenExpired
                        )

                        // Determine role and initialize the appropriate ViewModel
                        when (val userRoleId = authViewModel.currentUser?.roleId) {
                            1L -> { // Organizer
                                organizerViewModel = OrganizerViewModel(
                                    apiClient.organizerApi,
                                    authViewModel.currentRoleUserId ?: 0L
                                )

                                notificationViewModel = NotificationViewModel(
                                    apiClient.notificationApi,
                                    authViewModel.currentUser?.id ?: 0L,
                                    context = context
                                )

                                // Navigate to role-based screen
                                navController.navigate("homeScreen") {
                                    popUpTo("login") { inclusive = true } // Remove login from back stack
                                }
                            }
                            2L -> { // Seller
                                sellerViewModel = SellerViewModel(
                                    apiClient.sellerApi,
                                    authViewModel.currentRoleUserId ?: 0L
                                )

                                notificationViewModel = NotificationViewModel(
                                    apiClient.notificationApi,
                                    authViewModel.currentUser?.id ?: 0L,
                                    context = context
                                )

                                // Navigate to role-based screen
                                navController.navigate("homeScreen") {
                                    popUpTo("login") { inclusive = true } // Remove login from back stack
                                }
                            }
                            3L -> { // Admin
                                adminViewModel = AdminViewModel(apiClient.adminApi)
                                navController.navigate("adminScreen") {
                                    popUpTo("login") { inclusive = true }
                                }
                            }
                            else -> println("Unknown role ID: $userRoleId")
                        }
                    }
                )
            }
            composable("homeScreen") {
                HomeScreen(
                    authViewModel = authViewModel,
                    organizerViewModel = organizerViewModel,
                    sellerViewModel = sellerViewModel,
                    notificationViewModel = notificationViewModel,
                    onLogout = {
                        // Clear user and token
                        authViewModel.logout()
                        organizerViewModel = null
                        sellerViewModel = null
                        notificationViewModel = null
                        adminViewModel = null
                        navController.navigate("login") {
                            popUpTo("homeScreen") { inclusive = true }
                        }
                    }
                )
            }
            composable("adminScreen") {
                adminViewModel?.let { viewModel ->
                    AdminScreen(
                        authViewModel = authViewModel,
                        adminViewModel = viewModel,
                        onLogout = {
                            authViewModel.logout()
                            organizerViewModel = null
                            sellerViewModel = null
                            notificationViewModel = null
                            adminViewModel = null
                            navController.navigate("login") {
                                popUpTo("adminScreen") { inclusive = true }
                            }
                        }
                    )
                }
            }
        }
    }
}