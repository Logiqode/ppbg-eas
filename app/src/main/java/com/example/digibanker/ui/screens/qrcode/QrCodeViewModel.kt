package com.example.digibanker.ui.screens.qrcode

import android.graphics.Bitmap
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.digibanker.data.repository.BankRepository
import com.example.digibanker.model.Account
import com.example.digibanker.model.User
import com.example.digibanker.util.QrCodeGenerator
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

data class QrCodeUiState(
    val user: User? = null,
    val account: Account? = null,
    val qrBitmap: Bitmap? = null,
    val isLoading: Boolean = true
)

class QrCodeViewModel(private val bankRepository: BankRepository) : ViewModel() {

    private val _uiState = MutableStateFlow(QrCodeUiState())
    val uiState: StateFlow<QrCodeUiState> = _uiState.asStateFlow()

    fun loadQrCodeData(accountId: Long, qrSize: Int) {
        val account = bankRepository.getAccount(accountId)
        val user = account?.let { bankRepository.getUser(it.userId) }

        if (account != null && user != null) {
            // Data yang akan di-encode ke dalam QR Code
            val qrContent = Json.encodeToString(mapOf("accountId" to account.id.toString()))
            val bitmap = QrCodeGenerator.generate(qrContent, qrSize, qrSize)

            _uiState.update {
                it.copy(
                    user = user,
                    account = account,
                    qrBitmap = bitmap,
                    isLoading = false
                )
            }
        } else {
            _uiState.update { it.copy(isLoading = false) }
        }
    }
}

class QrCodeViewModelFactory(private val bankRepository: BankRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(QrCodeViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return QrCodeViewModel(bankRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}