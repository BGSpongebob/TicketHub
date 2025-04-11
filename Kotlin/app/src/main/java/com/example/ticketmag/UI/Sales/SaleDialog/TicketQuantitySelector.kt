package com.example.ticketmag.UI.Sales.SaleDialog

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun TicketQuantitySelector(
    label: String,
    maxQuantity: Int,
    quantity: Int,
    onQuantityChange: (Int) -> Unit
) {
    val remainingTickets = maxQuantity - quantity // Dynamically calculate remaining tickets

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = "$label\n$remainingTickets left", // Add remaining tickets on a new line
            modifier = Modifier
                .weight(1f)
                .padding(end = 8.dp),
            fontSize = 16.sp // Consistent font size for both lines
        )
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.End
        ) {
            IconButton(onClick = { if (quantity > 0) onQuantityChange(quantity - 1) }) {
                Icon(imageVector = Icons.Default.Remove, contentDescription = "Decrease")
            }
            Text(
                text = "$quantity",
                fontSize = 16.sp,
                modifier = Modifier.padding(horizontal = 8.dp)
            )
            IconButton(onClick = { if (quantity < maxQuantity && quantity < 100) onQuantityChange(quantity + 1) }) {
                Icon(imageVector = Icons.Default.Add, contentDescription = "Increase")
            }
        }
    }
}