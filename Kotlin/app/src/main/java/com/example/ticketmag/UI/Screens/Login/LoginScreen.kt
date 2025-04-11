package com.example.ticketmag.UI.Screens.Login

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ticketmag.R
import com.example.ticketmag.UI.FeedbackAlert
import com.example.ticketmag.ViewModels.AuthViewModel
import com.example.ticketmag.ViewModels.UiState

@Composable
fun LoginScreen(authViewModel: AuthViewModel, onLoginSuccess: () -> Unit) {
    var username by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var showRegisterDialog by remember { mutableStateOf(false) }

    // Add state for feedback alert
    var showFeedbackAlert by remember { mutableStateOf(false) }
    var feedbackMessage by remember { mutableStateOf("") }
    var isError by remember { mutableStateOf(false) }

    // Handle auth state changes
    LaunchedEffect(authViewModel.authState) {
        when (val state = authViewModel.authState) {
            is UiState.Success -> {
                if (state.data.token != null) { // Login success (//else is register success)
                    onLoginSuccess()
                }
            }
            is UiState.Error -> {
                feedbackMessage = state.message
                isError = true
                showFeedbackAlert = true
            }
            else -> {}
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = stringResource(R.string.welcome_text),
            fontSize = 32.sp,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(bottom = 32.dp)
        )


        OutlinedTextField(
            value = username,
            onValueChange = { username = it },
            label = { Text(stringResource(R.string.username_input)) },
            modifier = Modifier
                .fillMaxWidth(0.8f)
                .padding(bottom = 8.dp)
        )

        OutlinedTextField(
            value = password,
            onValueChange = { password = it },
            label = { Text(stringResource(R.string.password_input)) },
            visualTransformation = PasswordVisualTransformation(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
            modifier = Modifier
                .fillMaxWidth(0.8f)
                .padding(bottom = 8.dp)
        )

        Button(
            onClick = { authViewModel.login(username, password) },
            modifier = Modifier
                .fillMaxWidth(0.8f)
                .padding(bottom = 8.dp),
            enabled = username.isNotBlank() && password.isNotBlank()
        ) {
            Text(stringResource(R.string.login_button))
        }

        Button(
            onClick = { showRegisterDialog = true },
            modifier = Modifier
                .fillMaxWidth(0.8f)
                .padding(bottom = 8.dp),
            colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.secondary)
        ) {
            Text(stringResource(R.string.register_button))
        }
    }

    if (showRegisterDialog) {
        RegisterDialog(
            authViewModel = authViewModel,
            onDismiss = { showRegisterDialog = false }
        )
    }

    // Show feedback alert if needed
    if (showFeedbackAlert) {
        FeedbackAlert(
            message = feedbackMessage,
            isError = isError,
            onDismiss = { showFeedbackAlert = false; authViewModel.resetAuthState() }
        )
    }
}