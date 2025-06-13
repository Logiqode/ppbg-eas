package com.example.digibanker.ui.navigation

import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.digibanker.data.repository.BankRepository
import com.example.digibanker.ui.screens.home.HomeScreen
import com.example.digibanker.ui.screens.home.HomeViewModel
import com.example.digibanker.ui.screens.home.HomeViewModelFactory
import com.example.digibanker.ui.screens.qrcode.QrCodeScreen
import com.example.digibanker.ui.screens.qrcode.QrCodeViewModel
import com.example.digibanker.ui.screens.qrcode.QrCodeViewModelFactory
import com.example.digibanker.ui.screens.transfer.TransferScreen
import com.example.digibanker.ui.screens.transfer.TransferViewModel
import com.example.digibanker.ui.screens.transfer.TransferViewModelFactory

@Composable
fun AppNavigation(repository: BankRepository) {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = "home") {
        composable("home") {
            val homeViewModel: HomeViewModel = viewModel(
                factory = HomeViewModelFactory(repository)
            )
            HomeScreen(viewModel = homeViewModel, navController = navController)
        }
        composable(
            "transfer/{fromAccountId}",
            arguments = listOf(navArgument("fromAccountId") { type = NavType.LongType })
        ) { backStackEntry ->
            val fromAccountId = backStackEntry.arguments?.getLong("fromAccountId") ?: 0L
            val transferViewModel: TransferViewModel = viewModel(
                factory = TransferViewModelFactory(repository)
            )
            TransferScreen(
                navController = navController,
                viewModel = transferViewModel,
                fromAccountId = fromAccountId
            )
        }
        composable(
            "qrcode/{accountId}",
            arguments = listOf(navArgument("accountId") { type = NavType.LongType })
        ) { backStackEntry ->
            val accountId = backStackEntry.arguments?.getLong("accountId") ?: 0L
            val qrCodeViewModel: QrCodeViewModel = viewModel(
                factory = QrCodeViewModelFactory(repository)
            )
            QrCodeScreen(
                navController = navController,
                viewModel = qrCodeViewModel,
                accountId = accountId
            )
        }
    }
}