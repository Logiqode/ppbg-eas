package com.example.digibanker

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import com.example.digibanker.data.datasource.local.JsonDataSource
import com.example.digibanker.data.repository.BankRepository
import com.example.digibanker.ui.screens.home.HomeScreen
import com.example.digibanker.ui.screens.home.HomeViewModel
import com.example.digibanker.ui.screens.home.HomeViewModelFactory
import com.example.digibanker.ui.theme.DigiBankerTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // --- Dependency Injection ---
        // In a real app, you would use a library like Hilt for this.
        // For our prototype, we'll create the instances here manually.
        val dataSource = JsonDataSource(applicationContext)
        val repository = BankRepository(dataSource)
        val viewModelFactory = HomeViewModelFactory(repository)

        // Get the ViewModel instance using the factory
        val homeViewModel: HomeViewModel by viewModels { viewModelFactory }
        // --------------------------

        setContent {
            DigiBankerTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    // Pass the ViewModel instance to your HomeScreen
                    HomeScreen(viewModel = homeViewModel)
                }
            }
        }
    }
}