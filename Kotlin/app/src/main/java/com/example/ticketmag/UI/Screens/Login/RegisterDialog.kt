package com.example.ticketmag.UI.Screens.Login

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.ticketmag.DTOs.OrganizerDTO
import com.example.ticketmag.DTOs.SellerDTO
import com.example.ticketmag.DTOs.UserDTO
import com.example.ticketmag.Misc.Validator
import com.example.ticketmag.R
import com.example.ticketmag.ViewModels.AuthViewModel
import com.example.ticketmag.ViewModels.UiState

@Composable
fun RegisterDialog(authViewModel: AuthViewModel, onDismiss: () -> Unit) {
    val validator = Validator()

    var username by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }
    var firstName by remember { mutableStateOf("") }
    var lastName by remember { mutableStateOf("") }
    var phone by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var selectedRole by remember { mutableLongStateOf(1L) } // Default to Organizer (1)

    // State for feedback within the dialog
    var showFeedbackAlert by remember { mutableStateOf(false) }
    var feedbackMessage by remember { mutableStateOf("") }
    var isError by remember { mutableStateOf(false) }

    // Validation states
    val isUsernameValid = validator.isValidLength(username)
    val isPasswordValid = validator.isValidPasswordFormat(password)
    val isConfirmPasswordValid = validator.doPasswordsMatch(password, confirmPassword) && confirmPassword.isNotEmpty()
    val isFirstNameValid = validator.isValidLength(firstName)
    val isLastNameValid = validator.isValidLength(lastName)
    val isPhoneValid = validator.isValidBulgarianPhone(phone) && phone.isNotEmpty()
    val isEmailValid = validator.isValidEmail(email)

    // Button enabled only if all fields are valid
    val isFormValid = isUsernameValid && isPasswordValid && isConfirmPasswordValid &&
            isFirstNameValid && isLastNameValid && isPhoneValid && isEmailValid

    LaunchedEffect(authViewModel.authState) {
        when (val state = authViewModel.authState) {
            is UiState.Error -> {
                feedbackMessage = state.message
                isError = true
                showFeedbackAlert = true
            }
            else -> {}
        }
    }

    Dialog(onDismissRequest = onDismiss) {
        Surface(
            shape = MaterialTheme.shapes.medium,
            modifier = Modifier.padding(16.dp),
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
                        .padding(bottom = 16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(stringResource(R.string.register_dialog), fontSize = 20.sp)
                    IconButton(onClick = onDismiss) {
                        Text("✕", fontSize = 18.sp)
                    }
                }

                OutlinedTextField(
                    value = username,
                    onValueChange = { username = it },
                    label = { Text(stringResource(R.string.username_input)) },
                    placeholder = { Text(stringResource(R.string.short_text_placeholder)) },
                    trailingIcon = {
                        if (username.isNotEmpty() && !isUsernameValid) {
                            Icon(
                                imageVector = Icons.Default.Close,
                                contentDescription = "Error",
                                tint = MaterialTheme.colorScheme.error,
                                modifier = Modifier.size(24.dp)
                            )
                        }
                    },
                    modifier = Modifier.fillMaxWidth()
                )

                OutlinedTextField(
                    value = password,
                    onValueChange = { password = it },
                    label = { Text(stringResource(R.string.password_input)) },
                    placeholder = { Text(stringResource(R.string.password_placeholder)) },
                    visualTransformation = PasswordVisualTransformation(),
                    trailingIcon = {
                        if (password.isNotEmpty() && !isPasswordValid) {
                            Icon(
                                imageVector = Icons.Default.Close,
                                contentDescription = "Error",
                                tint = MaterialTheme.colorScheme.error,
                                modifier = Modifier.size(24.dp)
                            )
                        }
                    },
                    modifier = Modifier.fillMaxWidth()
                )

                OutlinedTextField(
                    value = confirmPassword,
                    onValueChange = { confirmPassword = it },
                    label = { Text(stringResource(R.string.confirm_password_input)) },
                    placeholder = { Text(stringResource(R.string.confirm_password_placeholder)) },
                    visualTransformation = PasswordVisualTransformation(),
                    trailingIcon = {
                        if (confirmPassword.isNotEmpty() && !isConfirmPasswordValid) {
                            Icon(
                                imageVector = Icons.Default.Close,
                                contentDescription = "Error",
                                tint = MaterialTheme.colorScheme.error,
                                modifier = Modifier.size(24.dp)
                            )
                        }
                    },
                    modifier = Modifier.fillMaxWidth()
                )

                OutlinedTextField(
                    value = firstName,
                    onValueChange = { firstName = it },
                    label = { Text(stringResource(R.string.first_name_input)) },
                    placeholder = { Text(stringResource(R.string.short_text_placeholder)) },
                    trailingIcon = {
                        if (firstName.isNotEmpty() && !isFirstNameValid) {
                            Icon(
                                imageVector = Icons.Default.Close,
                                contentDescription = "Error",
                                tint = MaterialTheme.colorScheme.error,
                                modifier = Modifier.size(24.dp)
                            )
                        }
                    },
                    modifier = Modifier.fillMaxWidth()
                )

                OutlinedTextField(
                    value = lastName,
                    onValueChange = { lastName = it },
                    label = { Text(stringResource(R.string.last_name_input)) },
                    placeholder = { Text(stringResource(R.string.short_text_placeholder)) },
                    trailingIcon = {
                        if (lastName.isNotEmpty() && !isLastNameValid) {
                            Icon(
                                imageVector = Icons.Default.Close,
                                contentDescription = "Error",
                                tint = MaterialTheme.colorScheme.error,
                                modifier = Modifier.size(24.dp)
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
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
                    trailingIcon = {
                        if (phone.isNotEmpty() && !isPhoneValid) {
                            Icon(
                                imageVector = Icons.Default.Close,
                                contentDescription = "Error",
                                tint = MaterialTheme.colorScheme.error,
                                modifier = Modifier.size(24.dp)
                            )
                        }
                    },
                    modifier = Modifier.fillMaxWidth()
                )

                OutlinedTextField(
                    value = email,
                    onValueChange = { email = it },
                    label = { Text(stringResource(R.string.email_input)) },
                    placeholder = { Text(stringResource(R.string.email_placeholder)) },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                    trailingIcon = {
                        if (email.isNotEmpty() && !isEmailValid) {
                            Icon(
                                imageVector = Icons.Default.Close,
                                contentDescription = "Error",
                                tint = MaterialTheme.colorScheme.error,
                                modifier = Modifier.size(24.dp)
                            )
                        }
                    },
                    modifier = Modifier.fillMaxWidth()
                )

                Row(
                    modifier = Modifier.padding(top = 16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    RadioButton(selected = selectedRole == 1L, onClick = { selectedRole = 1L })
                    Text(stringResource(R.string.organizer_radio), modifier = Modifier.padding(end = 16.dp))
                    RadioButton(selected = selectedRole == 2L, onClick = { selectedRole = 2L })
                    Text(stringResource(R.string.seller_radio))
                }

                Button(
                    onClick = {
                        val userDTO = if (selectedRole == 1L) {
                            UserDTO(
                                username = username,
                                password = password,
                                roleId = selectedRole,
                                organizer = OrganizerDTO(
                                    name1 = firstName,
                                    name2 = lastName,
                                    phone = phone,
                                    email = email
                                )
                            )
                        } else {
                            UserDTO(
                                username = username,
                                password = password,
                                roleId = selectedRole,
                                seller = SellerDTO(
                                    name1 = firstName,
                                    name2 = lastName,
                                    phone = phone,
                                    email = email
                                )
                            )
                        }
                        authViewModel.register(userDTO)
                    },
                    modifier = Modifier
                        .padding(top = 16.dp)
                        .fillMaxWidth(),
                    enabled = isFormValid
                ) {
                    Text(stringResource(R.string.register_button))
                }
            }
        }
    }
}