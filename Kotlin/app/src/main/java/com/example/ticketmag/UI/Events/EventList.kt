package com.example.ticketmag.UI.Events

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.ticketmag.DTOs.EventDTO

@Composable
fun EventList(
    events: List<EventDTO>,
    onShowEventDetails: (EventDTO) -> Unit,
    isOrganizer: Boolean,
    onEditEvent: (EventDTO) -> Unit,
    onCreateSale: (EventDTO) -> Unit,
    showActionButton: Boolean = true
) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(events) { event ->
            EventCard(
                event = event,
                isOrganizer = isOrganizer,
                onEditEvent = { onEditEvent(event) },
                onCreateSale = { onCreateSale(event) },
                onShowDetails = { onShowEventDetails(event) },
                showActionButton = showActionButton
            )
        }
    }
}