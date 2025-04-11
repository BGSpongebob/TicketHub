package com.example.ticketmag.UI.Ratings

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.res.stringResource
import com.example.ticketmag.DTOs.SellerDTO
import com.example.ticketmag.R

@Composable
fun RatingDialog(
    seller: SellerDTO,
    existingRating: Int?,
    onConfirm: (Int) -> Unit,
    onDismiss: () -> Unit
) {
    var selectedRating by remember { mutableStateOf(existingRating?.toString() ?: "") }
    var expanded by remember { mutableStateOf(false) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(stringResource(R.string.rating_dialog, seller.name1!!, seller.name2!!)) },
        text = {
            Column {
                Text(stringResource(R.string.rating_placeholder))
                Box {
                    OutlinedTextField(
                        value = selectedRating,
                        onValueChange = {},
                        label = { Text(stringResource(R.string.rating_input)) },
                        trailingIcon = {
                            IconButton(onClick = { expanded = true }) {
                                Icon(Icons.Default.ArrowDropDown, null)
                            }
                        },
                        readOnly = true
                    )
                    DropdownMenu(
                        expanded = expanded,
                        onDismissRequest = { expanded = false }
                    ) {
                        (1..10).forEach { rating ->
                            DropdownMenuItem(
                                text = { Text(rating.toString()) },
                                onClick = {
                                    selectedRating = rating.toString()
                                    expanded = false
                                }
                            )
                        }
                    }
                }
            }
        },
        confirmButton = {
            TextButton(onClick = {
                selectedRating.toIntOrNull()?.let { onConfirm(it) }
            }) {
                Text(stringResource(R.string.confirm_button))
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text(stringResource(R.string.cancel_button))
            }
        }
    )
}