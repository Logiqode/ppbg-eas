package com.example.digibanker.ui.screens.transfer

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.digibanker.data.repository.BankRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class TransferUiState(
    val fromAccount: String = "",
    val recipientAccount: String = "",
    val amount: String = "",
    val note: String = "",
    val isTransferSuccessful: Boolean? = null,
    val errorMessage: String? = null
)

class TransferViewModel(private val bankRepository: BankRepository) : ViewModel() {

    private val _uiState = MutableStateFlow(TransferUiState())
    val uiState: StateFlow<TransferUiState> = _uiState.asStateFlow()

    fun onRecipientAccountChange(newValue: String) {
        _uiState.update { it.copy(recipientAccount = newValue, errorMessage = null, isTransferSuccessful = null) }
    }

    fun onAmountChange(newValue: String) {
        _uiState.update { it.copy(amount = newValue, errorMessage = null, isTransferSuccessful = null) }
    }

    fun onNoteChange(newValue: String) {
        _uiState.update { it.copy(note = newValue, errorMessage = null, isTransferSuccessful = null) }
    }

    fun performTransfer(fromAccountId: Long) {
        viewModelScope.launch {
            val recipientId = _uiState.value.recipientAccount.toLongOrNull()
            val transferAmount = _uiState.value.amount.toDoubleOrNull()

            if (recipientId == null || transferAmount == null || transferAmount <= 0) {
                _uiState.update { it.copy(errorMessage = "Invalid input.") }
                return@launch
            }

            val success = bankRepository.performTransfer(fromAccountId, recipientId, transferAmount)

            _uiState.update {
                if (success) {
                    it.copy(isTransferSuccessful = true, errorMessage = null)
                } else {
                    it.copy(errorMessage = "Transfer failed. Check balance or recipient account.", isTransferSuccessful = false)
                }
            }
        }
    }
}

class TransferViewModelFactory(private val bankRepository: BankRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(TransferViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return TransferViewModel(bankRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}