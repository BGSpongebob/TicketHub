package com.example.ticketmag.UI.Screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ExitToApp
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.ticketmag.DTOs.EventDTO
import com.example.ticketmag.DTOs.SellerQueryDTO
import com.example.ticketmag.R
import com.example.ticketmag.UI.Events.EventDetailsDialog
import com.example.ticketmag.UI.Events.EventList
import com.example.ticketmag.UI.FeedbackAlert
import com.example.ticketmag.UI.Sales.SellerSalesDialog
import com.example.ticketmag.UI.Sellers.SellerCard
import com.example.ticketmag.ViewModels.AdminViewModel
import com.example.ticketmag.ViewModels.AuthViewModel
import com.example.ticketmag.ViewModels.UiState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AdminScreen(
    authViewModel: AuthViewModel,
    adminViewModel: AdminViewModel,
    onLogout: () -> Unit
) {
    // State for selected tab ("events" or "sales")
    var selectedTab by remember { mutableStateOf("events") }
    // State for search query
    var searchQuery by remember { mutableStateOf("") }
    // State for showing event details dialog
    var showEventDetails by remember { mutableStateOf<EventDTO?>(null) }
    // State for showing seller sales dialog
    var showSellerSales by remember { mutableStateOf<SellerQueryDTO?>(null) }

    var showFeedbackAlert by remember { mutableStateOf(false) }
    var feedbackMessage by remember { mutableStateOf("") }
    var isError by remember { mutableStateOf(false) }

    // Fetch data when the screen is first composed
    LaunchedEffect(Unit) {
        adminViewModel.fetchAllEvents()
        adminViewModel.fetchAllSellersWithSales()
    }

    // Filter events based on search query
    val filteredEvents = when (val state = adminViewModel.eventsState) {
        is UiState.Success -> state.data.filter {
            it.title?.startsWith(searchQuery, ignoreCase = true) == true
        }
        else -> emptyList()
    }

    // Filter sellers based on search query
    val filteredSellers = when (val state = adminViewModel.sellersWithSalesState) {
        is UiState.Success -> state.data.filter {
            it.seller?.name1?.startsWith(searchQuery, ignoreCase = true) == true ||
                    it.seller?.name2?.startsWith(searchQuery, ignoreCase = true) == true
        }
        else -> emptyList()
    }

    LaunchedEffect(adminViewModel.eventsState) {
        adminViewModel.eventsState.let { state ->
            when (state) {
                is UiState.Error -> {
                    feedbackMessage = state.message
                    isError = true
                    showFeedbackAlert = true
                }
                else -> {}
            }
        }
    }

    LaunchedEffect(adminViewModel.sellersWithSalesState) {
        adminViewModel.sellersWithSalesState.let { state ->
            when (state) {
                is UiState.Error -> {
                    feedbackMessage = state.message
                    isError = true
                    showFeedbackAlert = true
                }
                else -> {}
            }
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(stringResource(R.string.admin_dashboard)) },
                actions = {
                    IconButton(onClick = onLogout) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ExitToApp,
                            contentDescription = "Logout",
                            tint = MaterialTheme.colorScheme.onPrimary,
                            modifier = Modifier.size(32.dp)
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    titleContentColor = MaterialTheme.colorScheme.onPrimary
                )
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Search Bar
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                // Search bar that expands to fill available space
                OutlinedTextField(
                    value = searchQuery,
                    onValueChange = { searchQuery = it },
                    label = { Text(stringResource(R.string.search)) },
                    modifier = Modifier.weight(0.8f)
                )
                // Refresh icon button
                IconButton(
                    onClick = {
                        adminViewModel.fetchAllEvents()
                        adminViewModel.fetchAllSellersWithSales()
                    }
                ) {
                    Icon(
                        imageVector = Icons.Filled.Refresh,
                        contentDescription = "Refresh data",
                        modifier = Modifier
                            .size(24.dp)
                            .offset(y = 4.dp)
                    )
                }
            }

            // Toggle Button Group using TabRow
            TabRow(
                selectedTabIndex = if (selectedTab == "events") 0 else 1,
                modifier = Modifier.fillMaxWidth()
            ) {
                Tab(
                    selected = selectedTab == "events",
                    onClick = { selectedTab = "events" },
                    text = { Text(stringResource(R.string.events_tab)) }
                )
                Tab(
                    selected = selectedTab == "sales",
                    onClick = { selectedTab = "sales" },
                    text = { Text(stringResource(R.string.sales_tab)) }
                )
            }

            // LazyColumn based on selected tab
            when (selectedTab) {
                "events" -> {
                    when (adminViewModel.eventsState) {
                        is UiState.Loading -> CircularProgressIndicator(modifier = Modifier.align(
                            Alignment.CenterHorizontally))
                        is UiState.Success -> {
                            if (filteredEvents.isEmpty()) {
                                Text(stringResource(R.string.no_events_found), modifier = Modifier.align(Alignment.CenterHorizontally))
                            } else {
                                EventList(
                                    events = filteredEvents,
                                    onShowEventDetails = { showEventDetails = it },
                                    isOrganizer = false,
                                    onEditEvent = {}, // Not used for admin
                                    onCreateSale = {}, // Not used for admin
                                    showActionButton = false // Hide action buttons for admin
                                )
                            }
                        }
                        else -> {}
                    }
                }
                "sales" -> {
                    when (adminViewModel.sellersWithSalesState) {
                        is UiState.Loading -> CircularProgressIndicator(modifier = Modifier.align(
                            Alignment.CenterHorizontally))
                        is UiState.Success -> {
                            if (filteredSellers.isEmpty()) {
                                Text(stringResource(R.string.no_sellers_found), modifier = Modifier.align(Alignment.CenterHorizontally))
                            } else {
                                LazyColumn(
                                    verticalArrangement = Arrangement.spacedBy(8.dp),
                                ) {
                                    items(filteredSellers) { sellerQuery ->
                                        SellerCard(
                                            sellerQuery = sellerQuery,
                                            onClick = { showSellerSales = sellerQuery }
                                        )
                                    }
                                }
                            }
                        }
                        else -> {}
                    }
                }
            }

            // Event Details Dialog
            showEventDetails?.let { event ->
                EventDetailsDialog(
                    event = event,
                    organizerViewModel = null, // No organizer features for admin
                    authViewModel = authViewModel,
                    onDismiss = { showEventDetails = null }
                )
            }

            // Seller Sales Dialog
            showSellerSales?.let { sellerQuery ->
                SellerSalesDialog(
                    sellerQuery = sellerQuery,
                    onDismiss = { showSellerSales = null }
                )
            }

            if (showFeedbackAlert) {
                FeedbackAlert(
                    message = feedbackMessage,
                    isError = isError,
                    onDismiss = { showFeedbackAlert = false }
                )
            }
        }
    }
}