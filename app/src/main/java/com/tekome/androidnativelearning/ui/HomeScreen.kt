package com.tekome.androidnativelearning.ui

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    onNavigateToViewModel: () -> Unit,
    onNavigateToSavedState: () -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Android State Management") },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer
                )
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp, Alignment.Top),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "Chọn demo để học:",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )

            // Card 1 — ViewModel
            DemoCard(
                title = "1️⃣ ViewModel",
                description = "Sống qua: Rotation, Config Change\nMất khi: System kill process (low memory)",
                survives = "✅ Config Change",
                loses = "❌ Process Kill",
                onClick = onNavigateToViewModel,
                containerColor = MaterialTheme.colorScheme.secondaryContainer
            )

            // Card 2 — SavedStateHandle
            DemoCard(
                title = "2️⃣ SavedStateHandle",
                description = "Lưu vào Bundle (onSaveInstanceState)\nSống qua: Rotation + cả Process Kill",
                survives = "✅ Config Change + Process Kill",
                loses = "⚠️ Limit: chỉ lưu được primitive/Parcelable",
                onClick = onNavigateToSavedState,
                containerColor = MaterialTheme.colorScheme.tertiaryContainer
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Quick reference
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surfaceVariant
                )
            ) {
                Column(modifier = Modifier.padding(12.dp)) {
                    Text(
                        text = "💡 Quick Reference",
                        fontWeight = FontWeight.Bold,
                        style = MaterialTheme.typography.labelLarge
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "• Rotate screen → test Config Change\n" +
                               "• adb shell am kill <package> → test Process Kill\n" +
                               "• Package: com.tekome.androidnativelearning",
                        style = MaterialTheme.typography.bodySmall,
                        lineHeight = 20.sp
                    )
                }
            }
        }
    }
}

@Composable
private fun DemoCard(
    title: String,
    description: String,
    survives: String,
    loses: String,
    onClick: () -> Unit,
    containerColor: androidx.compose.ui.graphics.Color
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = containerColor),
        onClick = onClick
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text(
                text = title,
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = description,
                style = MaterialTheme.typography.bodyMedium,
                lineHeight = 22.sp
            )
            HorizontalDivider(modifier = Modifier.padding(vertical = 2.dp))
            Text(text = survives, style = MaterialTheme.typography.bodySmall)
            Text(text = loses, style = MaterialTheme.typography.bodySmall)
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = "Nhấn để xem demo →",
                modifier = Modifier.align(Alignment.End),
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.primary,
                fontWeight = FontWeight.SemiBold,
                textAlign = TextAlign.End
            )
        }
    }
}
