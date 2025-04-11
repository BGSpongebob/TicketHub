package com.example.ticketmag.UI.Clients

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.ticketmag.DTOs.ClientDTO
import com.example.ticketmag.Misc.Validator
import com.example.ticketmag.R

@Composable
fun ClientDialog(
    onDismiss: () -> Unit,
    onCreate: (ClientDTO) -> Unit,
    validator: Validator
) {
    var name1 by remember { mutableStateOf("") }
    var name2 by remember { mutableStateOf("") }
    var phone by remember { mutableStateOf("") }

    Dialog(onDismissRequest = onDismiss) {
        Surface(
            shape = MaterialTheme.shapes.medium,
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline)
        ) {
            Column(
                modifier = Modifier
                    .padding(16.dp)
                    .fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Title and Close Button
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(stringResource(R.string.create_client), fontSize = 20.sp)
                    IconButton(onClick = onDismiss) {
                        Icon(imageVector = Icons.Default.Close, contentDescription = "Close")
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                OutlinedTextField(
                    value = name1,
                    onValueChange = { name1 = it },
                    label = { Text(stringResource(R.string.first_name_input)) },
                    placeholder = { Text(stringResource(R.string.short_text_placeholder)) },
                    trailingIcon = {
                        if (name1.isNotEmpty() && !validator.isValidLength(name1)) {
                            Icon(
                                imageVector = Icons.Default.Close,
                                contentDescription = "Invalid length",
                                tint = MaterialTheme.colorScheme.error
                            )
                        }
                    },
                    modifier = Modifier.fillMaxWidth()
                )

                OutlinedTextField(
                    value = name2,
                    onValueChange = { name2 = it },
                    label = { Text(stringResource(R.string.last_name_input)) },
                    placeholder = { Text(stringResource(R.string.short_text_placeholder)) },
                    trailingIcon = {
                        if (name2.isNotEmpty() && !validator.isValidLength(name2)) {
                            Icon(
                                imageVector = Icons.Default.Close,
                                contentDescription = "Invalid length",
                                tint = MaterialTheme.colorScheme.error
                            )
                        }
                    },
                    modifier = Modifier.fillMaxWidth()
                )

                OutlinedTextField(
                    value = phone,
                    onValueChange = { phone = it },
                    label = { Text(stringResource(R.string.phone_input)) },
                    placeholder = { Text(stringResource(R.string.phone_placeholder)) },
                    trailingIcon = {
                        if (phone.isNotEmpty() && !validator.isValidBulgarianPhone(phone)) {
                            Icon(
                                imageVector = Icons.Default.Close,
                                contentDescription = "Invalid phone",
                                tint = MaterialTheme.colorScheme.error
                            )
                        }
                    },
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(16.dp))

                val isFormValid = validator.isValidLength(name1) &&
                        validator.isValidLength(name2) &&
                        validator.isValidBulgarianPhone(phone)

                Button(
                    onClick = {
                        val newClient = ClientDTO(
                            name1 = name1,
                            name2 = name2,
                            phone = phone
                        )
                        onCreate(newClient)
                    },
                    enabled = isFormValid,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(stringResource(R.string.create_button))
                }
            }
        }
    }
}