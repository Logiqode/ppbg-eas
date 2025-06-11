package com.example.digibanker.ui.screens.home

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
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
fun HomeScreen(viewModel: HomeViewModel) {
    val uiState by viewModel.uiState.collectAsState()

    Scaffold(
        topBar = {
            HomeTopAppBar(userName = uiState.userName)
        },
        containerColor = Color(0xFFF0F4F3) // A very light grey-teal
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            TotalBalanceHeader(balance = uiState.totalBalance)
            AccountCarousel(accounts = uiState.accounts)
            ActionButtons()
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeTopAppBar(userName: String) {
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
    LazyRow(
        contentPadding = PaddingValues(horizontal = 16.dp),
        horizontalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        items(accounts) { account ->
            AccountCard(account = account)
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
                    // Format the account number for display
                    text = "●●●● " + account.id.toString().takeLast(4),
                    color = LightTeal,
                    fontSize = 16.sp
                )
            }
        }
    }
}

@Composable
fun ActionButtons() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 40.dp, start = 32.dp, end = 32.dp),
        horizontalArrangement = Arrangement.SpaceAround
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Button(
                onClick = { /* TODO: Handle Transfer */ },
                shape = RoundedCornerShape(16.dp),
                colors = ButtonDefaults.buttonColors(containerColor = LightTeal),
                modifier = Modifier.size(72.dp)
            ) {
                // Placeholder for a FontAwesome Icon.
                // You would replace this with your FontAwesome icon implementation.
                // For example: FontAwesomeIcon(icon = "fa-paper-plane", ...)

                // Text("FA: Send", color = TealSecondary)

                Icon(
                    imageVector = FontAwesomeIcons.Solid.PaperPlane,
                    contentDescription = "Send",
                    tint = TealSecondary
                )

            }
            Spacer(modifier = Modifier.height(8.dp))
            Text("Transfer", fontWeight = FontWeight.SemiBold)
        }
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Button(
                onClick = { /* TODO: Handle QR Pay */ },
                shape = RoundedCornerShape(16.dp),
                colors = ButtonDefaults.buttonColors(containerColor = LightTeal),
                modifier = Modifier.size(72.dp)
            ) {
                // Placeholder for a FontAwesome Icon.
                // You would replace this with your FontAwesome icon implementation.
                // For example: FontAwesomeIcon(icon = "fa-qrcode", ...)
                // Text("FA: QR", color = TealSecondary)

                Icon(
                    imageVector = FontAwesomeIcons.Solid.Qrcode,
                    contentDescription = "Send",
                    tint = TealSecondary
                )
            }
            Spacer(modifier = Modifier.height(8.dp))
            Text("QR Pay", fontWeight = FontWeight.SemiBold)
        }
    }
}
