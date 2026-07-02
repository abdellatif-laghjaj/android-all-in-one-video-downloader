package media.grab.os.ui.paste

import android.content.ClipboardManager
import android.content.Context
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ContentPaste
import androidx.compose.material.icons.filled.Download
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import media.grab.os.data.model.DownloadFormat
import media.grab.os.data.model.Platform
import media.grab.os.ui.AppViewModel

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun PasteUrlScreen(vm: AppViewModel) {
    val context = LocalContext.current
    var url by remember { mutableStateOf("") }
    var format by remember { mutableStateOf(DownloadFormat.BEST) }
    var lastAction by remember { mutableStateOf("") }
    val platform = remember(url) { if (url.isBlank()) null else Platform.fromUrl(url) }

    Column(
        Modifier.fillMaxSize().verticalScroll(rememberScrollState()).padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        Text("Download media", style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Bold)
        Text(
            "1000+ sites via the built-in yt-dlp engine. Paste a link and pick a quality.",
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )

        Card(Modifier.fillMaxWidth()) {
            Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
                OutlinedTextField(
                    value = url,
                    onValueChange = { url = it },
                    modifier = Modifier.fillMaxWidth(),
                    label = { Text("Media URL") },
                    placeholder = { Text("https://…") },
                    singleLine = false,
                    supportingText = platform?.let { { Text("Detected: ${it.displayName}") } }
                )
                OutlinedButton(onClick = { url = readClipboard(context) }, modifier = Modifier.fillMaxWidth()) {
                    Icon(Icons.Filled.ContentPaste, contentDescription = null)
                    Text("  Paste from clipboard")
                }
            }
        }

        Text("Quality / format", style = MaterialTheme.typography.titleSmall, fontWeight = FontWeight.SemiBold)
        FlowRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            DownloadFormat.entries.forEach { f ->
                FilterChip(
                    selected = format == f,
                    onClick = { format = f },
                    label = { Text(f.label) }
                )
            }
        }

        Button(
            onClick = {
                vm.download(url, format)
                lastAction = "Queued (${format.label}). Check the Downloads tab for progress."
                url = ""
            },
            enabled = url.isNotBlank(),
            modifier = Modifier.fillMaxWidth()
        ) {
            Icon(Icons.Filled.Download, contentDescription = null)
            Text("  Download")
        }

        if (lastAction.isNotBlank()) {
            Card(Modifier.fillMaxWidth()) {
                Text(
                    lastAction,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.padding(12.dp)
                )
            }
        }
    }
}

private fun readClipboard(context: Context): String {
    val cm = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
    return cm.primaryClip?.getItemAt(0)?.text?.toString()?.trim().orEmpty()
}
