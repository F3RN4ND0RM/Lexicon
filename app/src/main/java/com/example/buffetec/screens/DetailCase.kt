package com.example.buffetec.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavBackStackEntry

@Composable
fun CaseDetail(navBackStackEntry: NavBackStackEntry, modifier: Modifier = Modifier) {
    val caseNumber = navBackStackEntry.arguments?.getString("caseNumber")

    // You can fetch or pass more case details using the caseNumber
    Box(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text(
                text = "Detalles del Caso #$caseNumber",
                fontWeight = FontWeight.Bold,
                fontSize = 24.sp,
                color = Color.Black
            )
            // Display the details of the case based on the caseNumber
            Text(text = "Aquí irán los detalles completos del caso...")
        }
    }
}