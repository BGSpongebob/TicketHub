package com.example.ticketmag.UI.Events.EventDialog

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.ticketmag.DTOs.EventType

@Composable
fun EventTypeSpinner(
    selectedId: Long, // The currently selected event type ID
    onIdChange: (Long) -> Unit, // Callback to update the selected ID
    label: @Composable () -> Unit,
) {
    var expanded by remember { mutableStateOf(false) }
    val icon = if (expanded) Icons.Filled.KeyboardArrowUp else Icons.Filled.KeyboardArrowDown

    // Find the name corresponding to the selected ID, default to a placeholder if not found
    val selectedEventType = EventType.entries.firstOrNull { it.id == selectedId } ?: EventType.CONCERT

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { expanded = !expanded }
            .padding(16.dp),
        contentAlignment = Alignment.CenterStart
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column {
                label() // Displays the label (e.g., "Event Type")
                Text(selectedEventType.name, style = MaterialTheme.typography.bodyMedium) // Displays the selected event name
            }
            IconButton(onClick = { expanded = !expanded }) {
                Icon(imageVector = icon, contentDescription = null) // Up/down arrow
            }
        }
        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
            modifier = Modifier.fillMaxWidth()
        ) {
            EventType.entries.forEach { eventType ->
                DropdownMenuItem(
                    text = {
                        Text(eventType.name)
                    },
                    onClick = {
                        onIdChange(eventType.id) // Update the selected ID
                        expanded = false // Close the dropdown
                    }
                )
            }
        }
    }
}