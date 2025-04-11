package com.example.ticketmag.UI.Events

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.ticketmag.DTOs.EventDTO
import com.example.ticketmag.DTOs.EventType
import com.example.ticketmag.DTOs.RatingDTO
import com.example.ticketmag.DTOs.SellerDTO
import com.example.ticketmag.R
import com.example.ticketmag.UI.Ratings.RatingDialog
import com.example.ticketmag.ViewModels.AuthViewModel
import com.example.ticketmag.ViewModels.OrganizerViewModel
import com.example.ticketmag.ViewModels.UiState

@Composable
fun EventDetailsDialog(
    event: EventDTO,
    organizerViewModel: OrganizerViewModel?,
    authViewModel: AuthViewModel,
    onDismiss: () -> Unit
) {
    var showRatingDialog by remember { mutableStateOf(false) }
    var sellerToRate by remember { mutableStateOf<SellerDTO?>(null) }

    val ratingsList = if (organizerViewModel != null) {
        when (val ratings = organizerViewModel.ratingsState) {
            is UiState.Success -> ratings.data
            else -> emptyList()
        }
    } else {
        emptyList()
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
                modifier = Modifier.padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                item {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(event.title ?: "", fontSize = 20.sp, modifier = Modifier.weight(0.8f))
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
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(
                        text = stringResource(R.string.event_details_organizer_field, event.organizer?.name1!!, event.organizer.name2!!),
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier
                            .fillMaxWidth()
                            .wrapContentWidth(Alignment.CenterHorizontally)
                    )
                }

                item {
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = event.description ?: "",
                        modifier = Modifier
                            .fillMaxWidth()
                            .wrapContentWidth(Alignment.CenterHorizontally),
                        textAlign = TextAlign.Center
                    )
                }

                item {
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = event.getFormattedDate(),
                        modifier = Modifier
                            .fillMaxWidth()
                            .wrapContentWidth(Alignment.CenterHorizontally),
                        textAlign = TextAlign.Center
                    )
                }

                item {
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = event.getFormattedTime(),
                        modifier = Modifier
                            .fillMaxWidth()
                            .wrapContentWidth(Alignment.CenterHorizontally),
                        textAlign = TextAlign.Center
                    )
                }

                item {
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = EventType.fromId(event.etypeId),
                        modifier = Modifier
                            .fillMaxWidth()
                            .wrapContentWidth(Alignment.CenterHorizontally),
                        textAlign = TextAlign.Center
                    )
                }

                if (event.basicSeats > 0){
                    item {
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = stringResource(
                                R.string.event_details_basic_seats_field,
                                event.avBasicSeats,
                                event.basicSeats,
                                event.tickets?.get(0)?.price!!
                            ),
                            modifier = Modifier
                                .fillMaxWidth()
                                .wrapContentWidth(Alignment.CenterHorizontally),
                            textAlign = TextAlign.Center
                        )
                    }
                }
                if (event.premiumSeats > 0){
                    item {
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = stringResource(
                                R.string.event_details_premium_seats_field,
                                event.avPremiumSeats,
                                event.premiumSeats,
                                event.tickets?.get(1)?.price!!
                            ),
                            modifier = Modifier
                                .fillMaxWidth()
                                .wrapContentWidth(Alignment.CenterHorizontally),
                            textAlign = TextAlign.Center
                        )
                    }
                }
                if (event.vipSeats > 0){
                    item {
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = stringResource(
                                R.string.event_details_vip_seats_field,
                                event.avVipSeats,
                                event.vipSeats,
                                event.tickets?.get(2)?.price!!
                            ),
                            modifier = Modifier
                                .fillMaxWidth()
                                .wrapContentWidth(Alignment.CenterHorizontally),
                            textAlign = TextAlign.Center
                        )
                    }
                }

                item {
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(
                        text = stringResource(R.string.event_details_sellers_field),
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier
                            .fillMaxWidth()
                            .wrapContentWidth(Alignment.CenterHorizontally)
                    )
                }



                event.sellers?.forEach { seller ->
                    val myRating = ratingsList.find {
                        it.sellerId == seller.id && it.organizerId == authViewModel.currentRoleUserId
                    }

                    item {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            modifier = Modifier
                                .fillMaxWidth()
                        ) {
                            Spacer(modifier = Modifier.height(16.dp))
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text("${seller.name1} ${seller.name2}", modifier = Modifier.weight(0.8f))
                                Spacer(modifier = Modifier.width(8.dp))
                                Icon(Icons.Default.Star, "Rating")
                                Text("${seller.currentRating}")
                            }
                            if (organizerViewModel != null) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    if (myRating != null) {
                                        Text(stringResource(R.string.my_rating))
                                        Icon(
                                            imageVector = Icons.Default.Star,
                                            contentDescription = "Rating"
                                        )
                                        Text("${myRating.rating}")
                                    } else {
                                        Text(stringResource(R.string.no_rating))
                                    }
                                }
                                Spacer(modifier = Modifier.width(8.dp))
                                Button(onClick = {
                                    sellerToRate = seller
                                    showRatingDialog = true
                                }) {
                                    Text(if (myRating != null) stringResource(R.string.edit_rating_button) else stringResource(R.string.rate_button))
                                }
                            }
                        }
                    }
                }
            }
        }
        if (showRatingDialog && sellerToRate != null) {
            RatingDialog(
                seller = sellerToRate!!,
                existingRating = ratingsList.find {
                    it.sellerId == sellerToRate!!.id && it.organizerId == authViewModel.currentRoleUserId
                }?.rating,
                onConfirm = { newRating ->
                    val ratingDTO = RatingDTO(
                        rating = newRating,
                        sellerId = sellerToRate!!.id,
                        organizerId = authViewModel.currentRoleUserId ?: 0L
                    )
                    val existingRating = ratingsList.find {
                        it.sellerId == sellerToRate!!.id && it.organizerId == authViewModel.currentRoleUserId
                    }
                    if (existingRating != null) {
                        organizerViewModel?.updateRating(ratingDTO.copy(id = existingRating.id))
                    } else {
                        organizerViewModel?.createRating(ratingDTO)
                    }
                    showRatingDialog = false
                    sellerToRate = null // Reset to avoid reuse
                },
                onDismiss = {
                    showRatingDialog = false
                    sellerToRate = null // Reset to avoid reuse
                }
            )
        }
    }
}