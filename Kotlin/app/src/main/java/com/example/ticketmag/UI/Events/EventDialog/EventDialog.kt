package com.example.ticketmag.UI.Events.EventDialog

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Button
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberDatePickerState
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
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.ticketmag.DTOs.EventDTO
import com.example.ticketmag.DTOs.TicketDTO
import com.example.ticketmag.Misc.Validator
import com.example.ticketmag.R
import com.example.ticketmag.ViewModels.AuthViewModel
import com.example.ticketmag.ViewModels.OrganizerViewModel
import com.example.ticketmag.ViewModels.UiState
import java.time.Instant
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.ZoneId

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EventDialog(
    authViewModel: AuthViewModel,
    organizerViewModel: OrganizerViewModel,
    onDismiss: () -> Unit,
    onSave: (EventDTO) -> Unit,
    validator: Validator,
    isEditMode: Boolean = false, // New parameter to toggle edit mode
    eventToEdit: EventDTO? = null // Preloaded event data for edit mode
) {
    // State variables for the form, initialized based on mode
    var title by remember { mutableStateOf(if (isEditMode) eventToEdit?.title ?: "" else "") }
    var description by remember { mutableStateOf(if (isEditMode) eventToEdit?.description ?: "" else "") }
    var enableBasic by remember { mutableStateOf(if (isEditMode) (eventToEdit?.basicSeats ?: 0) > 0 else false) }
    var basicSeats by remember { mutableStateOf(if (isEditMode) eventToEdit?.basicSeats?.toString() ?: "" else "") }
    var enablePremium by remember { mutableStateOf(if (isEditMode) (eventToEdit?.premiumSeats ?: 0) > 0 else false) }
    var premiumSeats by remember { mutableStateOf(if (isEditMode) eventToEdit?.premiumSeats?.toString() ?: "" else "") }
    var enableVip by remember { mutableStateOf(if (isEditMode) (eventToEdit?.vipSeats ?: 0) > 0 else false) }
    var vipSeats by remember { mutableStateOf(if (isEditMode) eventToEdit?.vipSeats?.toString() ?: "" else "") }
    var selectedEtypeId by remember { mutableStateOf(if (isEditMode) eventToEdit?.etypeId ?: 1L else 1L) }
    var basicPrice by remember { mutableStateOf(if (isEditMode) eventToEdit?.tickets?.get(0)?.price?.toString() ?: "" else "") }

    var premiumPrice by remember { mutableStateOf(if (isEditMode) eventToEdit?.tickets?.get(1)?.price?.toString() ?: "" else "") }
    var vipPrice by remember { mutableStateOf(if (isEditMode) eventToEdit?.tickets?.get(2)?.price?.toString() ?: "" else "") }
    var selectedSellers by remember { mutableStateOf(if (isEditMode) eventToEdit?.sellers ?: emptyList() else emptyList()) }
    var showSellerSelection by remember { mutableStateOf(false) }
    val allSellers = when (val state = organizerViewModel.sellersState) {
        is UiState.Success -> state.data
        else -> emptyList()
    }
    val availableSellers = allSellers
        .filterNot { selectedSellers.contains(it) }
        .sortedByDescending { it.currentRating }

    var selectedDate by remember { mutableStateOf(if (isEditMode) eventToEdit?.eventDateTime?.toLocalDate() else null) }
    var selectedTime by remember { mutableStateOf(if (isEditMode) eventToEdit?.eventDateTime?.toLocalTime() else null) }
    var showDatePicker by remember { mutableStateOf(false) }
    var showTimePicker by remember { mutableStateOf(false) }

    // Fetch sellers if not already loaded
    LaunchedEffect(Unit) {
        if (organizerViewModel.sellersState !is UiState.Success) {
            organizerViewModel.fetchSellers()
        }
    }

    Dialog(onDismissRequest = onDismiss) {
        Surface(
            shape = MaterialTheme.shapes.medium,
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline)
        ) {
            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                item {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = if (isEditMode) stringResource(R.string.edit_event_dialog) else stringResource(R.string.create_event_dialog),
                            fontSize = 20.sp
                        )
                        IconButton(onClick = onDismiss) {
                            Icon(
                                imageVector = Icons.Default.Close,
                                contentDescription = "Close",
                                tint = MaterialTheme.colorScheme.onSurface
                            )
                        }
                    }
                }

                item {
                    OutlinedTextField(
                        value = title,
                        onValueChange = { title = it },
                        label = { Text(stringResource(R.string.title_input)) },
                        placeholder = { Text(stringResource(R.string.short_text_placeholder)) },
                        trailingIcon = {
                            if (title.isNotEmpty() && !validator.isValidLength(title)) {
                                Icon(
                                    imageVector = Icons.Default.Close,
                                    contentDescription = "Invalid length",
                                    tint = MaterialTheme.colorScheme.error
                                )
                            }
                        },
                        modifier = Modifier.fillMaxWidth()
                    )
                }

                item {
                    OutlinedTextField(
                        value = description,
                        onValueChange = { description = it },
                        label = { Text(stringResource(R.string.description_input)) },
                        placeholder = { Text(stringResource(R.string.long_text_placeholder)) },
                        trailingIcon = {
                            if (description.isNotEmpty() && !validator.isValidLengthLong(description)) {
                                Icon(
                                    imageVector = Icons.Default.Close,
                                    contentDescription = "Invalid length",
                                    tint = MaterialTheme.colorScheme.error
                                )
                            }
                        },
                        modifier = Modifier.fillMaxWidth()
                    )
                }

                item {
                    SeatTypeInput(
                        label = stringResource(R.string.basic_seats_input),
                        enabled = enableBasic,
                        onEnabledChange = { if (!isEditMode) enableBasic = it },
                        seats = basicSeats,
                        onSeatsChange = { if (!isEditMode) basicSeats = it },
                        price = basicPrice,
                        onPriceChange = { if (!isEditMode) basicPrice = it },
                        isValidSeatCount = validator::isValidSeatCount,
                        isValidPrice = validator::isValidPrice,
                        isEditable = !isEditMode
                    )
                }

                item {
                    SeatTypeInput(
                        label = stringResource(R.string.premium_seats_input),
                        enabled = enablePremium,
                        onEnabledChange = { if (!isEditMode) enablePremium = it },
                        seats = premiumSeats,
                        onSeatsChange = { if (!isEditMode) premiumSeats = it },
                        price = premiumPrice,
                        onPriceChange = { if (!isEditMode) premiumPrice = it },
                        isValidSeatCount = validator::isValidSeatCount,
                        isValidPrice = validator::isValidPrice,
                        isEditable = !isEditMode
                    )
                }

                item {
                    SeatTypeInput(
                        label = stringResource(R.string.vip_seats_input),
                        enabled = enableVip,
                        onEnabledChange = { if (!isEditMode) enableVip = it },
                        seats = vipSeats,
                        onSeatsChange = { if (!isEditMode) vipSeats = it },
                        price = vipPrice,
                        onPriceChange = { if (!isEditMode) vipPrice = it },
                        isValidSeatCount = validator::isValidSeatCount,
                        isValidPrice = validator::isValidPrice,
                        isEditable = !isEditMode
                    )
                }

                item {
                    EventTypeSpinner(
                        selectedId = selectedEtypeId,
                        onIdChange = { selectedEtypeId = it },
                        label = { Text(stringResource(R.string.event_type_input)) }
                    )
                }

                item {
                    val remainingSellers = 5 - selectedSellers.size
                    Button(
                        onClick = { showSellerSelection = true },
                        enabled = remainingSellers > 0
                    ) {
                        Text(stringResource(R.string.add_seller_button, remainingSellers))
                    }
                }

                items(selectedSellers) { seller ->
                    Surface(
                        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline),
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 4.dp)
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(8.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "${seller.name1} ${seller.name2}",
                                modifier = Modifier.weight(0.8f, fill = false)
                            )
                            Icon(
                                imageVector = Icons.Default.Star,
                                contentDescription = "Rating"
                            )
                            Text("${seller.currentRating}", fontSize = 14.sp)
                            IconButton(onClick = { selectedSellers = selectedSellers - seller }) {
                                Icon(
                                    imageVector = Icons.Default.Close,
                                    contentDescription = "Remove"
                                )
                            }
                        }
                    }
                }

                item {
                    Button(onClick = { showDatePicker = true }) {
                        Text(if (selectedDate != null) stringResource(R.string.selected_date_button, selectedDate!!) else stringResource(R.string.date_input_button))
                    }
                }

                item {
                    Button(onClick = { showTimePicker = true }) {
                        Text(if (selectedTime != null) stringResource(R.string.selected_time_button, selectedTime!!) else stringResource(R.string.time_input_button))
                    }
                }

                item {
                    val atLeastOneSeatEnabled = enableBasic || enablePremium || enableVip
                    val allEnabledSeatsValid =
                        (!enableBasic || (validator.isValidSeatCount(basicSeats) && validator.isValidPrice(basicPrice))) &&
                                (!enablePremium || (validator.isValidSeatCount(premiumSeats) && validator.isValidPrice(premiumPrice))) &&
                                (!enableVip || (validator.isValidSeatCount(vipSeats) && validator.isValidPrice(vipPrice)))
                    val isFormValid = title.isNotBlank() &&
                            validator.isValidLength(title) &&
                            description.isNotBlank() &&
                            validator.isValidLengthLong(description) &&
                            atLeastOneSeatEnabled &&
                            allEnabledSeatsValid &&
                            selectedSellers.isNotEmpty() &&
                            selectedDate != null &&
                            selectedTime != null

                    Button(
                        onClick = {
                            val tickets = listOf(
                                TicketDTO(
                                    ttypeId = 1L,
                                    price = if (enableBasic) (if (isEditMode) eventToEdit?.tickets?.get(0)?.price else basicPrice.toDoubleOrNull()) ?: 0.0 else 0.0
                                ),
                                TicketDTO(
                                    ttypeId = 2L,
                                    price = if (enablePremium) (if (isEditMode) eventToEdit?.tickets?.get(1)?.price else premiumPrice.toDoubleOrNull()) ?: 0.0 else 0.0
                                ),
                                TicketDTO(
                                    ttypeId = 3L,
                                    price = if (enableVip) (if (isEditMode) eventToEdit?.tickets?.get(2)?.price else vipPrice.toDoubleOrNull()) ?: 0.0 else 0.0
                                )
                            )
                            val eventDTO = EventDTO(
                                id = if (isEditMode) eventToEdit?.id else null,
                                title = title,
                                description = description,
                                basicSeats = if (enableBasic) (if (isEditMode) eventToEdit?.basicSeats else basicSeats.toIntOrNull()) ?: 0 else 0,
                                premiumSeats = if (enablePremium) (if (isEditMode) eventToEdit?.premiumSeats else premiumSeats.toIntOrNull()) ?: 0 else 0,
                                vipSeats = if (enableVip) (if (isEditMode) eventToEdit?.vipSeats else vipSeats.toIntOrNull()) ?: 0 else 0,
                                avBasicSeats = if (isEditMode) eventToEdit?.avBasicSeats ?: 0 else if (enableBasic) basicSeats.toIntOrNull() ?: 0 else 0,
                                avPremiumSeats = if (isEditMode) eventToEdit?.avPremiumSeats ?: 0 else if (enablePremium) premiumSeats.toIntOrNull() ?: 0 else 0,
                                avVipSeats = if (isEditMode) eventToEdit?.avVipSeats ?: 0 else if (enableVip) vipSeats.toIntOrNull() ?: 0 else 0,
                                eventDateTime = LocalDateTime.of(selectedDate!!, selectedTime!!),
                                etypeId = selectedEtypeId,
                                organizer = authViewModel.currentUser?.organizer,
                                sellers = selectedSellers,
                                tickets = tickets
                            )
                            onSave(eventDTO)
                            onDismiss()
                        },
                        modifier = Modifier
                            .padding(top = 16.dp)
                            .fillMaxWidth(),
                        enabled = isFormValid
                    ) {
                        Text(if (isEditMode) stringResource(R.string.update_button) else stringResource(R.string.create_button))
                    }
                }
            }
        }
    }

    if (showSellerSelection) {
        SellerSelectionDialog(
            availableSellers = availableSellers,
            onSelect = { selectedSellers = selectedSellers + it; showSellerSelection = false },
            onDismiss = { showSellerSelection = false }
        )
    }

    if (showDatePicker) {
        val datePickerState = rememberDatePickerState(
            initialSelectedDateMillis = selectedDate?.atStartOfDay(ZoneId.systemDefault())?.toInstant()?.toEpochMilli()
        )
        DatePickerDialog(
            onDismissRequest = { showDatePicker = false },
            confirmButton = {
                TextButton(onClick = {
                    datePickerState.selectedDateMillis?.let {
                        selectedDate = Instant.ofEpochMilli(it).atZone(ZoneId.systemDefault()).toLocalDate()
                        showDatePicker = false
                    }
                }) { Text("OK") }
            },
            dismissButton = { TextButton(onClick = { showDatePicker = false }) { Text(stringResource(R.string.cancel_button)
            ) } }
        ) {
            DatePicker(state = datePickerState)
        }
    }

    if (showTimePicker) {
        SelectTimeDialog(
            onConfirm = { selectedTime = LocalTime.of(it.hour, it.minute); showTimePicker = false },
            onDismiss = { showTimePicker = false }
        )
    }
}