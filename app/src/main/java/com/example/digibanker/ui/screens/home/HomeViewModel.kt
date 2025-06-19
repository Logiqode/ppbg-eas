package com.example.digibanker.ui.screens.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.digibanker.data.repository.BankRepository
import com.example.digibanker.model.Account
import com.example.digibanker.model.User
import com.example.digibanker.util.SessionManager
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

data class HomeUiState(
    val userName: String = "User",
    val accounts: List<Account> = emptyList(),
    val totalBalance: Double = 0.0,
    val isLoading: Boolean = true
)

class HomeViewModel(
    private val bankRepository: BankRepository,
    private val userId: Long,
    private val sessionManager: SessionManager
) : ViewModel() {

    private val _uiState = MutableStateFlow(HomeUiState())
    val uiState: StateFlow<HomeUiState> = _uiState.asStateFlow()

    private val _logoutComplete = MutableSharedFlow<Boolean>()
    val logoutComplete = _logoutComplete.asSharedFlow()

    init {
        viewModelScope.launch {
            loadUserData(userId)
        }
    }

    private suspend fun loadUserData(userId: Long) {
        _uiState.update { it.copy(isLoading = true) } // Tampilkan loading saat refresh
        val user: User? = bankRepository.getUser(userId)
        val userAccounts: List<Account> = bankRepository.getAccountsForUser(userId)
        val balance = userAccounts.sumOf { it.balance }

        _uiState.update { currentState ->
            currentState.copy(
                userName = user?.name ?: "User",
                accounts = userAccounts,
                totalBalance = balance,
                isLoading = false
            )
        }
    }

    fun logout() {
        viewModelScope.launch {
            sessionManager.clearSession()
            _logoutComplete.emit(true)
        }
    }

    fun addAccount() {
        viewModelScope.launch {
            bankRepository.addAccountForUser(userId)
            loadUserData(userId)
        }
    }
}


class HomeViewModelFactory(
    private val bankRepository: BankRepository,
    private val userId: Long,
    private val sessionManager: SessionManager
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(HomeViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return HomeViewModel(bankRepository, userId, sessionManager) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}