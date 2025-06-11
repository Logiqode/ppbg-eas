package com.example.digibanker.ui.screens.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.digibanker.data.repository.BankRepository
import com.example.digibanker.model.Account
import com.example.digibanker.model.User
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

/**
 * Represents the state of the HomeScreen.
 *
 * @param userName The name of the currently logged-in user.
 * @param accounts The list of accounts belonging to the user.
 * @param totalBalance The sum of balances from all user accounts.
 * @param isLoading Flag to indicate if the data is currently being loaded.
 */
data class HomeUiState(
    val userName: String = "User",
    val accounts: List<Account> = emptyList(),
    val totalBalance: Double = 0.0,
    val isLoading: Boolean = true
)

/**
 * ViewModel for the HomeScreen.
 *
 * Responsible for fetching user and account data from the repository and preparing
 * it for display on the UI.
 *
 * @param bankRepository The repository for accessing bank data.
 */
class HomeViewModel(private val bankRepository: BankRepository) : ViewModel() {

    private val _uiState = MutableStateFlow(HomeUiState())
    val uiState: StateFlow<HomeUiState> = _uiState.asStateFlow()

    init {
        // We are hardcoding the user ID for this prototype.
        // In a real app, this would come from a login process.
        loadUserData(1234567890123456L)
    }

    /**
     * Loads the user's information and their accounts from the repository.
     * Updates the UI state with the fetched data.
     *
     * @param userId The ID of the user to load data for.
     */
    private fun loadUserData(userId: Long) {
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
}

/**
 * Factory for creating instances of [HomeViewModel].
 *
 * This is the modern way to provide dependencies (like a repository) to a ViewModel.
 *
 * @param bankRepository The repository instance to be injected into the ViewModel.
 */
class HomeViewModelFactory(private val bankRepository: BankRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(HomeViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return HomeViewModel(bankRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}