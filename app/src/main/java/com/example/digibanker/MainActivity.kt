package com.example.digibanker

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import com.example.digibanker.data.datasource.local.JsonDataSource
import com.example.digibanker.data.repository.BankRepository
import com.example.digibanker.ui.navigation.AppNavigation
import com.example.digibanker.ui.theme.DigiBankerTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val dataSource = JsonDataSource(applicationContext)
        val repository = BankRepository(dataSource)

        setContent {
            DigiBankerTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    AppNavigation(repository = repository)
                }
            }
        }
    }
}