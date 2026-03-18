package com.tekome.androidnativelearning.ui.viewmodel

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ViewModelDemoScreen(
    onBack: () -> Unit,
    viewModel: ViewModelDemoViewModel = viewModel()
) {
    val counter by viewModel.counter.collectAsState()
    val items by viewModel.items.collectAsState()
    val userProfile by viewModel.userProfile.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("ViewModel Demo") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.secondaryContainer
                )
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(paddingValues)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {

            // === Theory Box ===
            InfoBox(
                emoji = "🧠",
                title = "ViewModel — Cơ chế hoạt động",
                content = "• Sống trong ViewModelStore của Activity/Fragment\n" +
                          "• Tự động sống qua rotation (config change)\n" +
                          "• KHÔNG cần serialize — lưu được List, Map, Objects\n" +
                          "• Bị xóa khi: Activity finish() hoặc system kill process",
                isWarning = false
            )

            // === Counter Demo ===
            SectionHeader("🔢 Counter (Int)")
            Card(modifier = Modifier.fillMaxWidth()) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "$counter",
                        style = MaterialTheme.typography.displayMedium,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primary
                    )
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        Button(onClick = viewModel::decrement) { Text("−") }
                        Button(onClick = viewModel::increment) { Text("+") }
                    }
                }
            }

            // === List Demo ===
            SectionHeader("📋 List<String>")
            Card(modifier = Modifier.fillMaxWidth()) {
                Column(modifier = Modifier.padding(16.dp)) {
                    if (items.isEmpty()) {
                        Text(
                            "(Chưa có item nào)",
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            style = MaterialTheme.typography.bodyMedium
                        )
                    } else {
                        items.forEach { item ->
                            Text("• $item", style = MaterialTheme.typography.bodyMedium)
                        }
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        Button(onClick = viewModel::addItem) { Text("Add Item") }
                        OutlinedButton(onClick = viewModel::clearItems) { Text("Clear") }
                    }
                }
            }

            // === Complex Object Demo ===
            SectionHeader("👤 Complex Object (UserProfile)")
            Card(modifier = Modifier.fillMaxWidth()) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text("Name: ${userProfile.name}", style = MaterialTheme.typography.bodyLarge)
                    Text("Age:  ${userProfile.age}", style = MaterialTheme.typography.bodyLarge)
                    Text(
                        "Score: ${"%.1f".format(userProfile.score)}",
                        style = MaterialTheme.typography.bodyLarge,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.tertiary
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Button(onClick = viewModel::updateProfile) {
                        Text("Update Profile (+age, +score)")
                    }
                }
            }

            // === Warning Box ===
            InfoBox(
                emoji = "⚠️",
                title = "Test Process Kill",
                content = "Sau khi thay đổi data, chạy lệnh:\n\n" +
                          "adb shell am kill com.tekome.androidnativelearning\n\n" +
                          "Rồi mở lại app → data sẽ RESET về 0/empty.\n" +
                          "Đây là giới hạn của ViewModel thuần túy.",
                isWarning = true
            )
        }
    }
}

@Composable
fun SectionHeader(title: String) {
    Text(
        text = title,
        style = MaterialTheme.typography.titleMedium,
        fontWeight = FontWeight.Bold
    )
}

@Composable
fun InfoBox(
    emoji: String,
    title: String,
    content: String,
    isWarning: Boolean
) {
    val containerColor = if (isWarning)
        MaterialTheme.colorScheme.errorContainer
    else
        MaterialTheme.colorScheme.primaryContainer

    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = containerColor)
    ) {
        Column(modifier = Modifier.padding(12.dp)) {
            Text(
                text = "$emoji $title",
                fontWeight = FontWeight.Bold,
                style = MaterialTheme.typography.labelLarge
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = content,
                style = MaterialTheme.typography.bodySmall,
                lineHeight = 20.sp
            )
        }
    }
}
