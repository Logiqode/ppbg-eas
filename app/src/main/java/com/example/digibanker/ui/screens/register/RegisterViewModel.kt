package com.example.digibanker.ui.screens.register

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.digibanker.data.repository.BankRepository
import com.example.digibanker.model.User
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

data class RegisterUiState(
    val fullName: String = "",
    val email: String = "",
    val phone: String = "",
    val birthDate: String = "",
    val password: String = "",
    val confirmPassword: String = "",
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
    val isRegistrationSuccess: Boolean = false
)

class RegisterViewModel(private val repository: BankRepository) : ViewModel() {

    private val _uiState = MutableStateFlow(RegisterUiState())
    val uiState: StateFlow<RegisterUiState> = _uiState.asStateFlow()

    fun onFullNameChange(value: String) = _uiState.update { it.copy(fullName = value) }
    fun onEmailChange(value: String) = _uiState.update { it.copy(email = value) }
    fun onPhoneChange(value: String) = _uiState.update { it.copy(phone = value) }
    fun onBirthDateChange(value: String) = _uiState.update { it.copy(birthDate = value) }
    fun onPasswordChange(value: String) = _uiState.update { it.copy(password = value) }
    fun onConfirmPasswordChange(value: String) = _uiState.update { it.copy(confirmPassword = value) }

    fun attemptRegistration() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, errorMessage = null) }

            // Validasi Sederhana
            val state = _uiState.value
            if (state.fullName.isBlank() || state.email.isBlank() || state.password.isBlank()) {
                _uiState.update { it.copy(isLoading = false, errorMessage = "Nama, Email, dan Password tidak boleh kosong.") }
                return@launch
            }
            if (state.password != state.confirmPassword) {
                _uiState.update { it.copy(isLoading = false, errorMessage = "Password dan Konfirmasi Password tidak cocok.") }
                return@launch
            }

            // Buat objek User baru
            val newUser = User(
                id = System.currentTimeMillis(),
                name = state.fullName,
                email = state.email,
                password = state.password,
                phoneNumber = state.phone,
                birthDate = state.birthDate
            )

            val success = repository.registerUser(newUser)

            if (success) {
                _uiState.update { it.copy(isLoading = false, isRegistrationSuccess = true) }
            } else {
                _uiState.update { it.copy(isLoading = false, errorMessage = "Email sudah terdaftar.") }
            }
        }
    }
}

class RegisterViewModelFactory(private val repository: BankRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(RegisterViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return RegisterViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}