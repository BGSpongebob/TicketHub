package com.example.ticketmag.UI.Notifications

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.ticketmag.DTOs.NotificationDTO
import com.example.ticketmag.R

@Composable
fun NotificationDialog(
    notifications: List<NotificationDTO>,
    onMarkAsRead: (Long) -> Unit,
    onDelete: (Long) -> Unit,
    onDismiss: () -> Unit
) {
    // State for deletion confirmation
    var notificationToDelete by remember { mutableStateOf<NotificationDTO?>(null) }

    Dialog(onDismissRequest = onDismiss) {
        Surface(
            shape = MaterialTheme.shapes.medium,
            modifier = Modifier.fillMaxWidth(),
            border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                // Header with title and close button
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(stringResource(R.string.notifications_dialog), fontSize = 20.sp)
                    IconButton(onClick = onDismiss) {
                        Icon(Icons.Default.Close, contentDescription = "Close")
                    }
                }

                // Display notifications or a "no notifications" message
                if (notifications.isEmpty()) {
                    Text(
                        stringResource(R.string.no_notifications),
                        modifier = Modifier.align(Alignment.CenterHorizontally)
                    )
                } else {
                    LazyColumn {
                        items(notifications) { notification ->
                            NotificationCard(
                                notification = notification,
                                onMarkAsRead = { notification.id?.let { onMarkAsRead(it) } },
                                onDelete = { notificationToDelete = notification }
                            )
                        }
                    }
                }
            }
        }
    }

    // Confirmation dialog for deletion
    if (notificationToDelete != null) {
        AlertDialog(
            onDismissRequest = { notificationToDelete = null },
            title = { Text(stringResource(R.string.confirm_deletion_alert)) },
            text = { Text(stringResource(R.string.confirm_deletetion_notification_text)) },
            confirmButton = {
                TextButton(onClick = {
                    notificationToDelete!!.id?.let { onDelete(it) }
                    notificationToDelete = null
                }) {
                    Text(stringResource(R.string.yes))
                }
            },
            dismissButton = {
                TextButton(onClick = { notificationToDelete = null }) {
                    Text(stringResource(R.string.no))
                }
            }
        )
    }
}