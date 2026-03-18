package com.tekome.androidnativelearning.ui.savedstate

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
import com.tekome.androidnativelearning.ui.viewmodel.InfoBox
import com.tekome.androidnativelearning.ui.viewmodel.SectionHeader

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SavedStateDemoScreen(
    onBack: () -> Unit,
    viewModel: SavedStateDemoViewModel = viewModel()
) {
    val savedCounter by viewModel.savedCounter.collectAsState()
    val regularCounter by viewModel.regularCounter.collectAsState()
    val savedName by viewModel.savedName.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("SavedStateHandle Demo") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.tertiaryContainer
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
                emoji = "💾",
                title = "SavedStateHandle — Cơ chế hoạt động",
                content = "• Hoạt động như một Bundle được inject vào ViewModel\n" +
                          "• Tự động kết nối với onSaveInstanceState của Activity\n" +
                          "• Sống qua CẢ process kill (hệ thống restore Bundle)\n" +
                          "• Giới hạn: chỉ lưu primitive & Parcelable — data nhỏ!\n" +
                          "• Dùng savedStateHandle[\"key\"] = value để ghi",
                isWarning = false
            )

            // === Side-by-side Counter ===
            SectionHeader("🔢 Counter — So sánh trực quan")
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                // Saved Counter
                CounterCard(
                    modifier = Modifier.weight(1f),
                    label = "Saved Counter",
                    badge = "✅ PERSIST",
                    badgeColor = MaterialTheme.colorScheme.primaryContainer,
                    count = savedCounter,
                    description = "Dùng SavedStateHandle\nSống qua process kill",
                    onDecrement = viewModel::decrementSaved,
                    onIncrement = viewModel::incrementSaved
                )
                // Regular Counter
                CounterCard(
                    modifier = Modifier.weight(1f),
                    label = "Regular Counter",
                    badge = "❌ LOST",
                    badgeColor = MaterialTheme.colorScheme.errorContainer,
                    count = regularCounter,
                    description = "Dùng MutableStateFlow\nMất khi process kill",
                    onDecrement = viewModel::decrementRegular,
                    onIncrement = viewModel::incrementRegular
                )
            }

            // === Saved Name ===
            SectionHeader("✏️ Saved Name (String qua Bundle)")
            Card(modifier = Modifier.fillMaxWidth()) {
                Column(modifier = Modifier.padding(16.dp)) {
                    OutlinedTextField(
                        value = savedName,
                        onValueChange = viewModel::updateSavedName,
                        label = { Text("Nhập tên (sẽ persist qua process kill)") },
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "💡 savedStateHandle[\"saved_name\"] = \"$savedName\"",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        fontFamily = androidx.compose.ui.text.font.FontFamily.Monospace
                    )
                }
            }

            // === How it works ===
            InfoBox(
                emoji = "🔬",
                title = "Test Process Kill Step-by-step",
                content = "1. Tăng cả 2 counter lên, nhập tên\n" +
                          "2. Rotate phone → CẢ HAI đều còn (ViewModel sống)\n" +
                          "3. Chạy: adb shell am kill com.tekome.androidnativelearning\n" +
                          "4. Navigate back vào màn hình này\n" +
                          "→ Saved Counter & Name: CÒN NGUYÊN ✅\n" +
                          "→ Regular Counter: VỀ 0 ❌",
                isWarning = false
            )

            // === Internal mechanism ===
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surfaceVariant
                )
            ) {
                Column(modifier = Modifier.padding(12.dp)) {
                    Text(
                        "🏗️ Cách SavedStateHandle hoạt động bên trong:",
                        fontWeight = FontWeight.Bold,
                        style = MaterialTheme.typography.labelMedium
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(
                        text = "Activity.onSaveInstanceState()\n" +
                               "  └─ SavedStateRegistryController.performSave()\n" +
                               "       └─ ViewModel's SavedStateHandle → Bundle\n" +
                               "            └─ Lưu vào system (process kill OK)\n\n" +
                               "Activity.onCreate(savedInstanceState)\n" +
                               "  └─ Restore Bundle → SavedStateHandle\n" +
                               "       └─ ViewModel nhận data tự động",
                        style = MaterialTheme.typography.bodySmall,
                        fontFamily = androidx.compose.ui.text.font.FontFamily.Monospace,
                        lineHeight = 18.sp
                    )
                }
            }
        }
    }
}

@Composable
private fun CounterCard(
    modifier: Modifier = Modifier,
    label: String,
    badge: String,
    badgeColor: androidx.compose.ui.graphics.Color,
    count: Int,
    description: String,
    onDecrement: () -> Unit,
    onIncrement: () -> Unit
) {
    Card(
        modifier = modifier,
        colors = CardDefaults.cardColors(containerColor = badgeColor)
    ) {
        Column(
            modifier = Modifier.padding(12.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            Text(
                text = label,
                fontWeight = FontWeight.Bold,
                style = MaterialTheme.typography.labelMedium
            )
            Text(
                text = badge,
                style = MaterialTheme.typography.labelSmall
            )
            Text(
                text = "$count",
                style = MaterialTheme.typography.headlineLarge,
                fontWeight = FontWeight.ExtraBold,
                color = MaterialTheme.colorScheme.primary
            )
            Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                FilledTonalButton(
                    onClick = onDecrement,
                    contentPadding = PaddingValues(horizontal = 12.dp, vertical = 4.dp)
                ) { Text("−") }
                FilledTonalButton(
                    onClick = onIncrement,
                    contentPadding = PaddingValues(horizontal = 12.dp, vertical = 4.dp)
                ) { Text("+") }
            }
            Text(
                text = description,
                style = MaterialTheme.typography.bodySmall,
                lineHeight = 18.sp,
                fontWeight = FontWeight.Normal
            )
        }
    }
}
