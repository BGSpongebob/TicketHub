package com.example.ticketmag.UI.Events.EventDialog

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import com.example.ticketmag.R

@Composable
fun SeatTypeInput(
    label: String,
    enabled: Boolean,
    onEnabledChange: (Boolean) -> Unit,
    seats: String,
    onSeatsChange: (String) -> Unit,
    price: String,
    onPriceChange: (String) -> Unit,
    isValidSeatCount: (String) -> Boolean,
    isValidPrice: (String) -> Boolean,
    isEditable: Boolean
) {
    Column(modifier = Modifier.fillMaxWidth()) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Checkbox(
                checked = enabled,
                onCheckedChange = onEnabledChange,
                enabled = isEditable
            )
            Text(label)
        }
        if (enabled) {
            OutlinedTextField(
                value = seats,
                onValueChange = onSeatsChange,
                label = { Text(stringResource(R.string.seat_count_input)) },
                placeholder = { Text(stringResource(R.string.seat_count_placeholder)) },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                trailingIcon = {
                    if (seats.isNotEmpty() && !isValidSeatCount(seats)) {
                        Icon(
                            imageVector = Icons.Default.Close,
                            contentDescription = "Invalid seat count",
                            tint = MaterialTheme.colorScheme.error
                        )
                    }
                },
                modifier = Modifier.fillMaxWidth(),
                enabled = isEditable
            )
            OutlinedTextField(
                value = price,
                onValueChange = onPriceChange,
                label = { Text(stringResource(R.string.price_input)) },
                placeholder = { Text(stringResource(R.string.price_placeholder)) },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                trailingIcon = {
                    if (price.isNotEmpty() && !isValidPrice(price)) {
                        Icon(
                            imageVector = Icons.Default.Close,
                            contentDescription = "Invalid price",
                            tint = MaterialTheme.colorScheme.error
                        )
                    }
                },
                modifier = Modifier.fillMaxWidth(),
                enabled = isEditable
            )
        }
    }
}