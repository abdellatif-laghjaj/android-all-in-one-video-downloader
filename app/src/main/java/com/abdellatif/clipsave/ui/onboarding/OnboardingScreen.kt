package com.abdellatif.clipsave.ui.onboarding

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CloudDownload
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

@Composable
fun OnboardingScreen(onDone: () -> Unit) {
    Column(
        Modifier.fillMaxSize().verticalScroll(rememberScrollState()).padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Icon(
            Icons.Filled.CloudDownload, contentDescription = null,
            modifier = Modifier.size(96.dp).padding(top = 32.dp),
            tint = MaterialTheme.colorScheme.primary
        )
        Text("Welcome to ClipSave", style = MaterialTheme.typography.headlineMedium, fontWeight = FontWeight.Bold)
        Text(
            "Download images, video and audio from 1000+ sites straight to your Downloads folder. Free, open-source, no ads, no tracking.",
            style = MaterialTheme.typography.bodyMedium
        )
        Feature("Paste or share any link", "Share a URL from any app, or paste it on the Paste tab.")
        Feature("One-tap floating button", "Enable Accessibility to grab media while browsing social apps.")
        Feature("Saved to /Download/ClipSave/", "Files land in your public Downloads via MediaStore — no extra permissions on Android 10+.")
        Button(onClick = onDone, modifier = Modifier.fillMaxWidth().padding(top = 16.dp)) {
            Text("Get started")
        }
    }
}

@Composable
private fun Feature(title: String, body: String) {
    Card(Modifier.fillMaxWidth()) {
        Column(Modifier.padding(16.dp)) {
            Text(title, style = MaterialTheme.typography.titleSmall, fontWeight = FontWeight.SemiBold)
            Text(body, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
        }
    }
}
