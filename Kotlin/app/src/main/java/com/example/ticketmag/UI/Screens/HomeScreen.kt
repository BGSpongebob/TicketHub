package com.example.ticketmag.UI.Screens

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ExitToApp
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ticketmag.DTOs.EventDTO
import com.example.ticketmag.DTOs.NotificationDTO
import com.example.ticketmag.UI.Events.EventDetailsDialog
import com.example.ticketmag.UI.Events.EventDialog.EventDialog
import com.example.ticketmag.UI.Events.EventList
import com.example.ticketmag.UI.Notifications.NotificationDialog
import com.example.ticketmag.UI.Sales.SaleDialog.SaleDialog
import com.example.ticketmag.Misc.Validator
import com.example.ticketmag.R
import com.example.ticketmag.UI.FeedbackAlert
import com.example.ticketmag.ViewModels.OrganizerViewModel
import com.example.ticketmag.ViewModels.SellerViewModel
import com.example.ticketmag.ViewModels.UiState
import com.example.ticketmag.ViewModels.AuthViewModel
import com.example.ticketmag.ViewModels.NotificationViewModel
import kotlinx.coroutines.delay

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    authViewModel: AuthViewModel,
    organizerViewModel: OrganizerViewModel?,
    sellerViewModel: SellerViewModel?,
    notificationViewModel: NotificationViewModel?,
    onLogout: () -> Unit
) {

    var showEventDetailsDialog by remember { mutableStateOf<EventDTO?>(null) }
    var showEventDialog by remember { mutableStateOf<Pair<EventDTO?, Boolean>?>(null) }
    var showSaleDialog by remember { mutableStateOf<EventDTO?>(null) }
    var showNotificationDialog by remember { mutableStateOf(false) }

    var showFeedbackAlert by remember { mutableStateOf(false) }
    var feedbackMessage by remember { mutableStateOf("") }
    var isError by remember { mutableStateOf(false) }

    var lastEventOperation by remember { mutableStateOf<String?>(null) }

    var isPerformingAction by remember { mutableStateOf(false) }

    val validator = Validator()

    var searchQuery by remember { mutableStateOf("") }


    //events

    LaunchedEffect(Unit) {
        organizerViewModel?.fetchEvents()
        sellerViewModel?.fetchEvents()
    }

    val eventsState = organizerViewModel?.eventsState ?: sellerViewModel?.eventsState
    LaunchedEffect(eventsState) {
        when (eventsState) {
            is UiState.Error -> {
                feedbackMessage = eventsState.message
                isError = true
                showFeedbackAlert = true
            }
            else -> {}
        }
    }

    // Filter events based on search query
    val filteredEvents = when (val state = organizerViewModel?.eventsState ?: sellerViewModel?.eventsState) {
        is UiState.Success -> state.data.filter {
            it.title?.contains(searchQuery, ignoreCase = true) == true
        }
        else -> emptyList()
    }

    //notifs

    LaunchedEffect(Unit) {
        notificationViewModel?.fetchNotifications()
    }

    val notificationsState = notificationViewModel?.notificationsState
    LaunchedEffect(notificationsState) {
        when (notificationsState) {
            is UiState.Error -> {
                feedbackMessage = notificationsState.message
                isError = true
                showFeedbackAlert = true
            }
            else -> {}
        }
    }

    val unreadCount = notificationViewModel?.unreadCount ?: 0

    //alerts

    LaunchedEffect(organizerViewModel?.eventOperationState) {
        organizerViewModel?.eventOperationState?.let { state ->
            when (state) {
                is UiState.Success -> {
                    feedbackMessage = when (lastEventOperation) {
                        "update" -> "Event successfully updated"
                        "create" -> "Event successfully created"
                        else -> "Event operation completed" // Fallback, should not happen
                    }
                    isError = false
                    showFeedbackAlert = true
                    showEventDialog = null // Close the dialog
                    lastEventOperation = null // Reset operation tracking
                }
                is UiState.Error -> {
                    feedbackMessage = state.message
                    isError = true
                    showFeedbackAlert = true
                    lastEventOperation = null // Reset operation tracking
                }
                else -> {}
            }
            organizerViewModel.resetEventOperationState() // Reset after handling
        }
    }

    LaunchedEffect(sellerViewModel?.clientOperationState) {
        sellerViewModel?.clientOperationState?.let { state ->
            when (state) {
                is UiState.Success -> {
                    feedbackMessage = "Client successfully Registered"
                    isError = false
                    showFeedbackAlert = true
                }
                is UiState.Error -> {
                    feedbackMessage = state.message
                    isError = true
                    showFeedbackAlert = true
                    sellerViewModel.resetClientOperationState()
                }
                else -> {}
            }
        }
    }

    LaunchedEffect(sellerViewModel?.saleOperationState) {
        sellerViewModel?.saleOperationState?.let { state ->
            when (state) {
                is UiState.Success -> {
                    feedbackMessage = "Sale successfully created"
                    isError = false
                    showFeedbackAlert = true
                }
                is UiState.Error -> {
                    feedbackMessage = state.message
                    isError = true
                    showFeedbackAlert = true
                }
                else -> {}
            }
            sellerViewModel.resetSaleOperationState()
        }
    }

    LaunchedEffect(isPerformingAction) {
        if (!isPerformingAction) {
            while (true) {
                delay(5_000) // 30 seconds
                notificationViewModel?.fetchNotifications()
                organizerViewModel?.fetchEvents()
                sellerViewModel?.fetchEvents()
            }
        }
    }


    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        if (organizerViewModel != null) {
                            Text(authViewModel.currentUser?.organizer?.name1 + " " + authViewModel.currentUser?.organizer?.name2, fontSize = 20.sp, modifier = Modifier.weight(0.8f))
                        }
                        if (sellerViewModel != null) {
                            Text(authViewModel.currentUser?.seller?.name1 + " " + authViewModel.currentUser?.seller?.name2, fontSize = 20.sp, modifier = Modifier.weight(0.8f))
                            Spacer(modifier = Modifier.width(8.dp))
                            Icon(Icons.Default.Star, "Rating")
                            Text("${authViewModel.currentUser?.seller?.currentRating}", fontSize = 20.sp)
                        }
                    }
                },
                actions = {
                    if (notificationViewModel != null) {
                        BadgedBox(
                            modifier = Modifier.offset(x = 6.dp),
                            badge = {
                                if (unreadCount > 0) {
                                    Badge(
                                        modifier = Modifier
                                            .offset(x = (-12).dp, y = 4.dp) // Move badge closer to bell
                                    ) {
                                        Text(unreadCount.toString())
                                    }
                                }
                            }
                        ) {
                            IconButton(onClick = { showNotificationDialog = true }) {
                                Icon(
                                    imageVector = Icons.Default.Notifications,
                                    contentDescription = "Notifications",
                                    tint = MaterialTheme.colorScheme.onPrimary
                                )
                            }
                        }
                    }
                    IconButton(
                        onClick = onLogout
                    ) {
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
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            var searchWeight = 1f
            if (organizerViewModel != null) {
                searchWeight = 0.8f
            }
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                OutlinedTextField(
                    value = searchQuery,
                    onValueChange = { searchQuery = it },
                    label = { Text(stringResource(R.string.search_events)) },
                    modifier = Modifier
                        .padding(0.dp, 0.dp, 0.dp, 8.dp)
                        .weight(searchWeight)
                )

                if (organizerViewModel != null) {
                    IconButton(
                        onClick = { showEventDialog = Pair(null, false) },
                    ) {
                        Icon(
                            imageVector = Icons.Default.Add,
                            contentDescription = "Create Event",
                            tint = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.size(32.dp)
                        )
                    }
                }

            }
            Spacer(modifier = Modifier.height(8.dp))
            when (eventsState) {
                is UiState.Loading -> {
                    CircularProgressIndicator(modifier = Modifier.align(Alignment.CenterHorizontally))
                }
                is UiState.Success -> {
                    if (eventsState.data.isEmpty()) {
                        Text(
                            text = stringResource(R.string.no_events_found),
                            fontSize = 16.sp,
                            modifier = Modifier.align(Alignment.CenterHorizontally)
                        )
                    } else {
                        EventList(
                            events = filteredEvents,
                            onShowEventDetails = { event -> showEventDetailsDialog = event },
                            isOrganizer = organizerViewModel != null,
                            onEditEvent = { event -> showEventDialog = Pair(event, true) },
                            onCreateSale = { event -> showSaleDialog = event }
                        )
                    }
                }
                else -> {
                    Text(
                        text = stringResource(R.string.no_events_found),
                        fontSize = 16.sp,
                        modifier = Modifier.align(Alignment.CenterHorizontally)
                    )
                }
            }

            showEventDetailsDialog?.let { event ->
                isPerformingAction = true
                EventDetailsDialog(
                    event = event,
                    organizerViewModel = organizerViewModel,
                    authViewModel = authViewModel,
                    onDismiss = {
                        showEventDetailsDialog = null
                        isPerformingAction = false
                    }
                )
            }

            showEventDialog?.let { (event, isEditMode) ->
                isPerformingAction = true
                if (organizerViewModel != null) {
                    EventDialog(
                        authViewModel = authViewModel,
                        organizerViewModel = organizerViewModel,
                        onDismiss = {
                            showEventDialog = null
                            isPerformingAction = false
                        },
                        onSave = { updatedEvent ->
                            if (isEditMode) {
                                lastEventOperation = "update"
                                organizerViewModel.updateEvent(updatedEvent)
                            } else {
                                lastEventOperation = "create"
                                organizerViewModel.createEvent(updatedEvent)
                            }
                        },
                        validator = validator,
                        isEditMode = isEditMode,
                        eventToEdit = event
                    )
                }
            }

            showSaleDialog?.let { event ->
                isPerformingAction = true
                SaleDialog(
                    event = event,
                    sellerViewModel = sellerViewModel!!,
                    authViewModel = authViewModel,
                    onDismiss = {
                        showSaleDialog = null
                        isPerformingAction = false
                    },
                    validator = validator
                )
            }

            var notificationList: List<NotificationDTO> = emptyList()
            if (showNotificationDialog && notificationViewModel != null) {
                isPerformingAction = true
                when (val state = notificationViewModel.notificationsState) {
                    is UiState.Success -> {
                        notificationList = state.data.sortedByDescending { it.createdAt }
                    }
                    else -> {}
                }
                NotificationDialog(
                    notifications = notificationList,
                    onMarkAsRead = { id -> notificationViewModel.markNotificationRead(id) },
                    onDelete = { id -> notificationViewModel.deleteNotification(id) },
                    onDismiss = {
                        showNotificationDialog = false
                        isPerformingAction = false
                    }
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