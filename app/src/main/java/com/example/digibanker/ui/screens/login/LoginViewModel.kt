package com.example.digibanker.ui.screens.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.digibanker.data.repository.BankRepository
import com.example.digibanker.util.SessionManager
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

data class LoginUiState(
    val email: String = "",
    val password: String = "",
    val isLoading: Boolean = false,
    val loginError: String? = null
)

class LoginViewModel(
    private val repository: BankRepository,
    private val sessionManager: SessionManager
) : ViewModel() {

    private val _uiState = MutableStateFlow(LoginUiState())
    val uiState: StateFlow<LoginUiState> = _uiState.asStateFlow()

    private val _loginSuccess = MutableSharedFlow<Boolean>()
    val loginSuccess = _loginSuccess.asSharedFlow()

    fun onEmailChange(email: String) {
        _uiState.update { it.copy(email = email, loginError = null) }
    }

    fun onPasswordChange(password: String) {
        _uiState.update { it.copy(password = password, loginError = null) }
    }

    fun loginUser() {
        if (_uiState.value.isLoading) return

        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }

            val user = repository.login(_uiState.value.email, _uiState.value.password)

            if (user != null) {
                sessionManager.saveUserSession(user.id)
                _loginSuccess.emit(true)
            } else {
                _uiState.update { it.copy(loginError = "Email atau password salah.") }
            }

            _uiState.update { it.copy(isLoading = false) }
        }
    }
}