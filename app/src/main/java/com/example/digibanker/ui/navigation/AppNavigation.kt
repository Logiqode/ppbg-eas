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
import com.example.digibanker.ui.screens.login.LoginScreen
import com.example.digibanker.ui.screens.login.LoginViewModel
import com.example.digibanker.ui.screens.login.LoginViewModelFactory
import com.example.digibanker.ui.screens.qrcode.QrCodeScreen
import com.example.digibanker.ui.screens.qrcode.QrCodeViewModel
import com.example.digibanker.ui.screens.qrcode.QrCodeViewModelFactory
import com.example.digibanker.ui.screens.qrcode.QrScannerScreen
import com.example.digibanker.ui.screens.register.RegisterScreen
import com.example.digibanker.ui.screens.register.RegisterViewModel
import com.example.digibanker.ui.screens.register.RegisterViewModelFactory
import com.example.digibanker.ui.screens.transfer.TransferScreen
import com.example.digibanker.ui.screens.transfer.TransferViewModel
import com.example.digibanker.ui.screens.transfer.TransferViewModelFactory
import com.example.digibanker.util.SessionManager

@Composable
fun AppNavigation(
    repository: BankRepository,
    sessionManager: SessionManager
) {
    val navController = rememberNavController()

    val startDestination = if (sessionManager.getActiveUserId() != null) "home" else "login"

    NavHost(navController = navController, startDestination = startDestination) {

        composable("login") {
            val loginViewModel: LoginViewModel = viewModel(
                factory = LoginViewModelFactory(repository, sessionManager)
            )
            LoginScreen(
                navController = navController,
                viewModel = loginViewModel
            )
        }

        composable("home") {
            sessionManager.getActiveUserId()?.let { activeUserId ->
                val homeViewModel: HomeViewModel = viewModel(
                    factory = HomeViewModelFactory(repository, activeUserId, sessionManager)
                )
                HomeScreen(viewModel = homeViewModel, navController = navController)
            }
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

        composable("qr_scanner") {
            QrScannerScreen(navController = navController)
        }

        composable("register") {
            val registerViewModel: RegisterViewModel = viewModel(
                factory = RegisterViewModelFactory(repository)
            )
            RegisterScreen(
                navController = navController,
                viewModel = registerViewModel
            )
        }
    }
}