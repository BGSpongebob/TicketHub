package com.example.ticketmag.UI.Sales.SaleDialog

import android.annotation.SuppressLint
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.ticketmag.DTOs.ClientDTO
import com.example.ticketmag.DTOs.EventDTO
import com.example.ticketmag.DTOs.SaleDTO
import com.example.ticketmag.DTOs.SaleTicketDTO
import com.example.ticketmag.UI.Clients.ClientDialog
import com.example.ticketmag.Misc.Validator
import com.example.ticketmag.R
import com.example.ticketmag.ViewModels.AuthViewModel
import com.example.ticketmag.ViewModels.SellerViewModel
import com.example.ticketmag.ViewModels.UiState
import java.time.LocalDate

@SuppressLint("DefaultLocale")
@Composable
fun SaleDialog(
    event: EventDTO,
    sellerViewModel: SellerViewModel,
    authViewModel: AuthViewModel,
    onDismiss: () -> Unit,
    validator: Validator
) {
    var basicQuantity by remember { mutableIntStateOf(0) }
    var premiumQuantity by remember { mutableIntStateOf(0) }
    var vipQuantity by remember { mutableIntStateOf(0) }
    var selectedClient by remember { mutableStateOf<ClientDTO?>(null) }
    var showClientSelection by remember { mutableStateOf(false) }
    var showCreateClientDialog by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        if (sellerViewModel.clientsState !is UiState.Success) {
            sellerViewModel.fetchClients()
        }
    }

    // Observe client creation result
    LaunchedEffect(sellerViewModel.clientOperationState) {
        when (val state = sellerViewModel.clientOperationState) {
            is UiState.Success -> {
                selectedClient = state.data // Set to server-returned ClientDTO with id
            }
            else -> {} // Loading or null, do nothing
        }
        sellerViewModel.resetClientOperationState()
    }

    val basicTicket = event.tickets?.find { it.ttypeId == 1L }
    val premiumTicket = event.tickets?.find { it.ttypeId == 2L }
    val vipTicket = event.tickets?.find { it.ttypeId == 3L }
    val totalPrice = (basicQuantity * (basicTicket?.price ?: 0.0)) +
            (premiumQuantity * (premiumTicket?.price ?: 0.0)) +
            (vipQuantity * (vipTicket?.price ?: 0.0))

    // Check if there are any available tickets
    val hasAvailableTickets = (event.avBasicSeats > 0 && basicTicket != null) ||
            (event.avPremiumSeats > 0 && premiumTicket != null) ||
            (event.avVipSeats > 0 && vipTicket != null)

    Dialog(onDismissRequest = onDismiss) {
        Surface(
            shape = MaterialTheme.shapes.medium,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline)
        ) {
            Column(
                modifier = Modifier
                    .padding(16.dp)
                    .fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = stringResource(R.string.sale_dialog, event.title!!),
                        fontSize = 20.sp,
                        modifier = Modifier
                            .weight(1f)
                            .padding(end = 8.dp)
                    )
                    IconButton(
                        onClick = onDismiss,
                        modifier = Modifier.size(48.dp)
                    ) {
                        Icon(imageVector = Icons.Default.Close, contentDescription = "Close")
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                if (hasAvailableTickets) {
                    // Ticket Quantity Selectors
                    if (event.avBasicSeats > 0 && basicTicket != null) {
                        TicketQuantitySelector(
                            label = stringResource(
                                R.string.sale_basic_tickets_input,
                                basicTicket.price
                            ),
                            maxQuantity = event.avBasicSeats,
                            quantity = basicQuantity,
                            onQuantityChange = { basicQuantity = it }
                        )
                    }
                    if (event.avPremiumSeats > 0 && premiumTicket != null) {
                        TicketQuantitySelector(
                            label = stringResource(
                                R.string.sale_premium_tickets_input,
                                premiumTicket.price
                            ),
                            maxQuantity = event.avPremiumSeats,
                            quantity = premiumQuantity,
                            onQuantityChange = { premiumQuantity = it }
                        )
                    }
                    if (event.avVipSeats > 0 && vipTicket != null) {
                        TicketQuantitySelector(
                            label = stringResource(R.string.sale_vip_tickets_input, vipTicket.price),
                            maxQuantity = event.avVipSeats,
                            quantity = vipQuantity,
                            onQuantityChange = { vipQuantity = it }
                        )
                    }
                } else {
                    // No available tickets message
                    Text(
                        text = stringResource(R.string.no_available_tickets),
                        fontSize = 16.sp,
                        color = MaterialTheme.colorScheme.error,
                        modifier = Modifier.padding(vertical = 16.dp)
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        text = stringResource(R.string.sale_client_input, selectedClient?.let { "${it.name1} ${it.name2}" } ?: stringResource(R.string.no_client_selected)),
                        maxLines = 1
                    )
                    Button(onClick = { showClientSelection = true }) {
                        Text(stringResource(R.string.select_client_button))
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))

                Button(onClick = { showCreateClientDialog = true }) {
                    Text(stringResource(R.string.register_client_button))
                }

                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = stringResource(R.string.sale_total_price, String.format("%.2f", totalPrice)),
                    fontSize = 16.sp,
                    modifier = Modifier.padding(bottom = 8.dp)
                )

                val totalQuantity = basicQuantity + premiumQuantity + vipQuantity
                val isFormValid = totalQuantity > 0 && selectedClient != null

                Button(
                    onClick = {
                        val saleDTO = SaleDTO(
                            seller = authViewModel.currentRoleUserId ?: 0L,
                            saleDate = LocalDate.now(),
                            client = selectedClient!!,
                            salesTickets = listOfNotNull(
                                if (basicQuantity > 0 && basicTicket != null) SaleTicketDTO(
                                    ticket = basicTicket,
                                    quantity = basicQuantity
                                ) else null,
                                if (premiumQuantity > 0 && premiumTicket != null) SaleTicketDTO(
                                    ticket = premiumTicket,
                                    quantity = premiumQuantity
                                ) else null,
                                if (vipQuantity > 0 && vipTicket != null) SaleTicketDTO(
                                    ticket = vipTicket,
                                    quantity = vipQuantity
                                ) else null
                            )
                        )
                        sellerViewModel.createSale(saleDTO)
                        onDismiss()
                    },
                    enabled = isFormValid,
                    modifier = Modifier.fillMaxWidth(0.8f)
                ) {
                    Text(stringResource(R.string.create_button))
                }
            }
        }
    }

    if (showClientSelection) {
        ClientSelectionDialog(
            clients = when (val state = sellerViewModel.clientsState) {
                is UiState.Success -> state.data
                else -> emptyList()
            },
            onSelect = { client ->
                selectedClient = client
                showClientSelection = false
            },
            onDismiss = { showClientSelection = false }
        )
    }

    if (showCreateClientDialog) {
        ClientDialog(
            onDismiss = { showCreateClientDialog = false },
            onCreate = { newClient ->
                sellerViewModel.createClient(newClient)
                showCreateClientDialog = false
            },
            validator = validator
        )
    }
}