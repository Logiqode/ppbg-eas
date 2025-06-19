package com.example.digibanker.ui.screens.register

import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController

private val TealPrimary = Color(0xFF00796B)
private val TealSecondary = Color(0xFF004D40)
private val PageBackground = Color(0xFFF0F4F3)

@Composable
fun RegisterScreen(navController: NavController, viewModel: RegisterViewModel) {
    val uiState by viewModel.uiState.collectAsState()
    val context = LocalContext.current

    LaunchedEffect(uiState.isRegistrationSuccess) {
        if (uiState.isRegistrationSuccess) {
            Toast.makeText(context, "Registrasi Berhasil! Silakan Login.", Toast.LENGTH_LONG).show()
            navController.popBackStack()
        }
    }

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = PageBackground
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 24.dp)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(40.dp))
            Text(
                text = "Register",
                fontSize = 44.sp,
                fontWeight = FontWeight.Bold,
                color = TealPrimary
            )
            Spacer(modifier = Modifier.height(32.dp))

            val textFieldColors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = TealPrimary,
                unfocusedBorderColor = TealSecondary.copy(alpha = 0.5f)
            )
            OutlinedTextField(value = uiState.fullName, onValueChange = viewModel::onFullNameChange, label = { Text("Full Name") }, modifier = Modifier.fillMaxWidth(), colors = textFieldColors)
            Spacer(modifier = Modifier.height(16.dp))
            OutlinedTextField(value = uiState.email, onValueChange = viewModel::onEmailChange, label = { Text("E-mail") }, keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email), modifier = Modifier.fillMaxWidth(), colors = textFieldColors)
            Spacer(modifier = Modifier.height(16.dp))
            OutlinedTextField(value = uiState.phone, onValueChange = viewModel::onPhoneChange, label = { Text("Phone") }, keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone), modifier = Modifier.fillMaxWidth(), colors = textFieldColors)
            Spacer(modifier = Modifier.height(16.dp))
            OutlinedTextField(value = uiState.birthDate, onValueChange = viewModel::onBirthDateChange, label = { Text("Birth Date (YYYY-MM-DD)") }, modifier = Modifier.fillMaxWidth(), colors = textFieldColors)
            Spacer(modifier = Modifier.height(16.dp))
            OutlinedTextField(value = uiState.password, onValueChange = viewModel::onPasswordChange, label = { Text("Password") }, visualTransformation = PasswordVisualTransformation(), keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password), modifier = Modifier.fillMaxWidth(), colors = textFieldColors)
            Spacer(modifier = Modifier.height(16.dp))
            OutlinedTextField(value = uiState.confirmPassword, onValueChange = viewModel::onConfirmPasswordChange, label = { Text("Confirm Password") }, visualTransformation = PasswordVisualTransformation(), keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password), modifier = Modifier.fillMaxWidth(), colors = textFieldColors)

            uiState.errorMessage?.let {
                Spacer(modifier = Modifier.height(8.dp))
                Text(text = it, color = MaterialTheme.colorScheme.error)
            }

            Spacer(modifier = Modifier.height(24.dp))

            Button(
                onClick = viewModel::attemptRegistration,
                modifier = Modifier.fillMaxWidth().height(50.dp),
                enabled = !uiState.isLoading,
                shape = RoundedCornerShape(20.dp),
                colors = ButtonDefaults.buttonColors(containerColor = TealSecondary)
            ) {
                if (uiState.isLoading) {
                    CircularProgressIndicator(modifier = Modifier.size(24.dp), color = Color.White)
                } else {
                    Text("Register", fontSize = 16.sp, color = Color.White)
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            Button(
                onClick = { navController.popBackStack() },
                modifier = Modifier.fillMaxWidth().height(50.dp),
                shape = RoundedCornerShape(20.dp),
                colors = ButtonDefaults.buttonColors(containerColor = TealPrimary)
            ) {
                Text("Have account? Sign In", fontSize = 16.sp, color = Color.White)
            }
            Spacer(modifier = Modifier.height(40.dp))
        }
    }
}