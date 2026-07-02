package com.abdellatif.clipsave.ui.settings

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.provider.Settings
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.FilterChip
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.abdellatif.clipsave.data.preferences.AccessMode
import com.abdellatif.clipsave.data.preferences.ThemeMode
import com.abdellatif.clipsave.download.YtDlpEngine
import com.abdellatif.clipsave.privileged.RootHelper
import com.abdellatif.clipsave.privileged.ShizukuHelper
import com.abdellatif.clipsave.ui.AppViewModel

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun SettingsScreen(vm: AppViewModel) {
    val context = LocalContext.current
    val settings by vm.settings.collectAsState()
    var engineMsg by remember { mutableStateOf("") }
    var updating by remember { mutableStateOf(false) }

    Column(
        Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text(
            "Settings",
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.Bold
        )

        SettingsCard("Appearance") {
            Text("Theme", style = MaterialTheme.typography.bodyMedium)
            FlowRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                ThemeMode.entries.forEach { mode ->
                    FilterChip(
                        selected = settings.themeMode == mode,
                        onClick = { vm.setTheme(mode) },
                        label = { Text(mode.name.lowercase().replaceFirstChar { it.uppercase() }) }
                    )
                }
            }
        }

        SettingsCard("Download engine") {
            Text(
                "yt-dlp powers downloads from 1000+ sites. Keep it updated so new site changes keep working.",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Text(
                "Version: ${YtDlpEngine.ytdlpVersion ?: "initializing…"}",
                style = MaterialTheme.typography.bodySmall
            )
            OutlinedButton(
                onClick = {
                    if (!updating) {
                        updating = true; engineMsg = "Updating…"
                        vm.updateEngine { result -> engineMsg = result; updating = false }
                    }
                },
                enabled = !updating,
                modifier = Modifier.fillMaxWidth()
            ) {
                if (updating) {
                    CircularProgressIndicator(
                        Modifier
                            .padding(end = 8.dp)
                            .size(18.dp),
                        strokeWidth = 2.dp
                    )
                    Text("Updating…")
                } else Text("Update engine (yt-dlp)")
            }
            if (engineMsg.isNotBlank()) {
                Text(
                    engineMsg,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.primary
                )
            }
        }

        SettingsCard("Access mode") {
            Text(
                "Downloads to /Download/ need no root. These modes are for grabbing from protected locations.",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            FlowRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                AccessMode.entries.forEach { mode ->
                    FilterChip(
                        selected = settings.accessMode == mode,
                        onClick = { vm.setAccessMode(mode) },
                        label = { Text(mode.name.lowercase().replaceFirstChar { it.uppercase() }) }
                    )
                }
            }
            HorizontalDivider()
            Text(
                "Shizuku: ${ShizukuHelper.statusText()}",
                style = MaterialTheme.typography.bodySmall
            )
            Text("Root: ${RootHelper.statusText()}", style = MaterialTheme.typography.bodySmall)
        }

        SettingsCard("Permissions") {
            OutlinedButton(
                onClick = { openAppNotificationSettings(context) },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Notification settings")
            }
            OutlinedButton(
                onClick = { openAccessibilitySettings(context) },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Accessibility (floating button)")
            }
            OutlinedButton(
                onClick = { openOverlaySettings(context) },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Display over other apps")
            }
        }

        SettingsCard("About") {
            Text("ClipSave v1.0.2", style = MaterialTheme.typography.bodyMedium)
            Text(
                "Free & open-source · MIT License · No ads, no telemetry.",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            OutlinedButton(
                onClick = {
                    openUrl(
                        context,
                        "https://github.com/abdellatif-laghjaj/android-all-in-one-video-downloader"
                    )
                },
                modifier = Modifier.fillMaxWidth()
            ) { Text("Source code") }
            OutlinedButton(
                onClick = {
                    openUrl(
                        context,
                        "https://github.com/abdellatif-laghjaj/android-all-in-one-video-downloader/blob/main/LICENSE"
                    )
                },
                modifier = Modifier.fillMaxWidth()
            ) { Text("License") }
        }
    }
}

@Composable
private fun SettingsCard(title: String, content: @Composable () -> Unit) {
    Card(Modifier.fillMaxWidth()) {
        Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
            Text(
                title,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.SemiBold
            )
            content()
        }
    }
}

private fun openUrl(context: Context, url: String) {
    runCatching { context.startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(url))) }
}

private fun openAppNotificationSettings(context: Context) {
    val intent = Intent(Settings.ACTION_APP_NOTIFICATION_SETTINGS)
        .putExtra(Settings.EXTRA_APP_PACKAGE, context.packageName)
    runCatching { context.startActivity(intent) }
}

private fun openAccessibilitySettings(context: Context) {
    runCatching { context.startActivity(Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS)) }
}

private fun openOverlaySettings(context: Context) {
    val intent = Intent(
        Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
        Uri.parse("package:${context.packageName}")
    )
    runCatching { context.startActivity(intent) }
}
