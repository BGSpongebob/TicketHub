package com.example.ticketmag.UI.Sales

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.ticketmag.DTOs.SaleDTO
import com.example.ticketmag.R

@SuppressLint("DefaultLocale")
@Composable
fun SaleCard(sale: SaleDTO) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(2.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            var totalPrice = 0.0
            Text(stringResource(R.string.sale_card_date_field, sale.saleDate!!), fontWeight = FontWeight.Bold)
            Text(stringResource(R.string.sale_card_client_field, sale.client?.name1!!, sale.client.name2!!))
            Text(stringResource(R.string.sale_card_event_field, sale.event?.title!!))
            Text(stringResource(R.string.sale_card_tickets_field), fontWeight = FontWeight.Bold)
            sale.salesTickets?.forEach { saleTicket ->
                val typeName = when (saleTicket.ticket?.ttypeId) {
                    1L -> stringResource(R.string.basic)
                    2L -> stringResource(R.string.premium)
                    3L -> stringResource(R.string.vip)
                    else -> ""
                }
                Text("${saleTicket.quantity} - $typeName - \$${saleTicket.ticket?.price}")
                totalPrice += saleTicket.quantity * saleTicket.ticket?.price!!
            }
            Text(
                text = stringResource(R.string.sale_card_total_price_field, String.format("%.2f", totalPrice)),
                fontWeight = FontWeight.Bold
            )
        }
    }
}