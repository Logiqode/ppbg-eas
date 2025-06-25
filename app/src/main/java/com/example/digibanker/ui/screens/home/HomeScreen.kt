package com.example.digibanker.ui.screens.home

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddCard
import androidx.compose.material.icons.filled.Logout
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.digibanker.model.Account
import com.example.digibanker.util.formatCurrency
import compose.icons.FontAwesomeIcons
import compose.icons.fontawesomeicons.Solid
import compose.icons.fontawesomeicons.solid.PaperPlane
import compose.icons.fontawesomeicons.solid.Qrcode

// Custom Teal Color Palette
val TealPrimary = Color(0xFF00796B)
val TealSecondary = Color(0xFF004D40)
val LightTeal = Color(0xFFB2DFDB)
val TextOnTeal = Color.White

@Composable
fun HomeScreen(viewModel: HomeViewModel, navController: NavController) {
    val uiState by viewModel.uiState.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.logoutComplete.collect { isLogoutComplete ->
            if (isLogoutComplete) {
                navController.navigate("login") {
                    popUpTo(navController.graph.startDestinationId) { inclusive = true }
                    launchSingleTop = true
                }
            }
        }
    }

    Scaffold(
        topBar = {
            HomeTopAppBar(
                userName = uiState.userName,
                onLogoutClicked = { viewModel.logout() }
            )
        },
        containerColor = Color(0xFFF0F4F3)
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            if (uiState.isLoading) {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator()
                }
            } else {
                TotalBalanceHeader(balance = uiState.totalBalance)
                AccountCarousel(accounts = uiState.accounts)
                ActionButtons(
                    navController = navController,
                    fromAccountId = uiState.accounts.firstOrNull()?.id,
                    viewModel = viewModel
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeTopAppBar(
    userName: String,
    onLogoutClicked: () -> Unit
) {
    TopAppBar(
        title = {
            Column {
                Text(
                    text = "Welcome back,",
                    fontSize = 14.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Text(
                    text = userName,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface
                )
            }
        },
        actions = {
            IconButton(onClick = onLogoutClicked) {
                Icon(
                    imageVector = Icons.Default.Logout,
                    contentDescription = "Logout",
                    tint = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = Color.Transparent
        )
    )
}

@Composable
fun TotalBalanceHeader(balance: Double) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Total Balance",
            fontSize = 18.sp,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Text(
            text = formatCurrency(balance),
            fontSize = 40.sp,
            fontWeight = FontWeight.ExtraBold,
            color = TealPrimary
        )
    }
}

@Composable
fun AccountCarousel(accounts: List<Account>) {
    if (accounts.size == 1) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 16.dp),
            contentAlignment = Alignment.Center
        ) {
            AccountCard(account = accounts.first())
        }
    } else {
        LazyRow(
            contentPadding = PaddingValues(horizontal = 16.dp),
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            items(accounts) { account ->
                AccountCard(account = account)
            }
        }
    }
}


@Composable
fun AccountCard(account: Account) {
    val cardGradient = Brush.verticalGradient(
        colors = listOf(TealPrimary, TealSecondary)
    )
    Card(
        modifier = Modifier
            .width(300.dp)
            .height(180.dp),
        shape = RoundedCornerShape(20.dp),
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(cardGradient)
                .padding(20.dp)
        ) {
            Text(
                text = "DigiBanker",
                modifier = Modifier.align(Alignment.TopStart),
                color = TextOnTeal.copy(alpha = 0.8f),
                fontWeight = FontWeight.Bold
            )
            Text(
                text = "VISA",
                modifier = Modifier.align(Alignment.TopEnd),
                color = TextOnTeal,
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold
            )
            Column(modifier = Modifier.align(Alignment.BottomStart)) {
                Text(
                    text = "Balance",
                    color = LightTeal,
                    fontSize = 14.sp
                )
                Text(
                    text = formatCurrency(account.balance),
                    color = TextOnTeal,
                    fontSize = 26.sp,
                    fontWeight = FontWeight.SemiBold
                )
                Spacer(Modifier.height(4.dp))
                Text(
                    text = "●●●● " + account.id.toString().takeLast(4),
                    color = LightTeal,
                    fontSize = 16.sp
                )
            }
        }
    }
}

@Composable
fun ActionButtons(navController: NavController, fromAccountId: Long?, viewModel: HomeViewModel) {
    // Mengubah Row utama menjadi Column
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 40.dp, start = 16.dp, end = 16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(24.dp) // Memberi jarak antar baris
    ) {
        // Baris Pertama (Tombol Transfer & Scan to Pay)
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceAround
        ) {
            ActionButton(
                label = "Transfer",
                icon = FontAwesomeIcons.Solid.PaperPlane,
                onClick = {
                    fromAccountId?.let {
                        navController.navigate("transfer/$it")
                    }
                },
                enabled = fromAccountId != null
            )

            ActionButton(
                label = "Scan to Pay",
                icon = FontAwesomeIcons.Solid.Qrcode,
                onClick = {
                    navController.navigate("qr_scanner")
                }
            )
        }

        // Baris Kedua (Tombol Receive QR & Add Card)
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceAround
        ) {
            ActionButton(
                label = "Receive QR",
                icon = FontAwesomeIcons.Solid.Qrcode,
                onClick = {
                    fromAccountId?.let {
                        navController.navigate("qrcode/$it")
                    }
                },
                enabled = fromAccountId != null
            )

            ActionButton(
                label = "Add Card",
                icon = Icons.Default.AddCard,
                onClick = { viewModel.addAccount() }
            )
        }
    }
}


@Composable
fun ActionButton(
    label: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    onClick: () -> Unit,
    enabled: Boolean = true
) {
    // Menambahkan modifier agar setiap tombol memiliki lebar yang konsisten
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.width(90.dp) // Memberi lebar tetap
    ) {
        Button(
            onClick = onClick,
            shape = RoundedCornerShape(16.dp),
            colors = ButtonDefaults.buttonColors(containerColor = LightTeal),
            modifier = Modifier.size(72.dp),
            enabled = enabled
        ) {
            Icon(
                imageVector = icon,
                contentDescription = label,
                tint = TealSecondary
            )
        }
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = label,
            fontWeight = FontWeight.SemiBold,
            // Menambahkan textAlign untuk memastikan teks berada di tengah jika lebih dari 1 baris
            textAlign = androidx.compose.ui.text.style.TextAlign.Center
        )
    }
}