package com.example.digibanker

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import com.example.digibanker.ui.navigation.AppNavigation
import com.example.digibanker.ui.theme.DigiBankerTheme
import com.example.digibanker.util.SessionManager

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // --- PERBAIKAN DI SINI ---
        // 1. Dapatkan instance aplikasi.
        // 2. Lakukan cast ke MyApplication.
        // 3. Ambil repository dari instance tersebut.
        val repository = (application as MyApplication).repository

        val sessionManager = SessionManager(applicationContext)
        setContent {
            DigiBankerTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    AppNavigation(
                        repository = repository,
                        sessionManager = sessionManager
                    )
                }
            }
        }
    }
}