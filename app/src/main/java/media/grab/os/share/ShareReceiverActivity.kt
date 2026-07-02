package media.grab.os.share

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Download
import androidx.compose.material.icons.filled.MusicNote
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import media.grab.os.data.model.DownloadFormat
import media.grab.os.data.model.Platform
import media.grab.os.download.DownloadService
import media.grab.os.ui.theme.MediaGrabTheme

class ShareReceiverActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val shared = extractUrl(intent)
        if (shared == null) {
            Toast.makeText(this, "No link found in shared text.", Toast.LENGTH_SHORT).show()
            finish(); return
        }
        setContent {
            MediaGrabTheme(darkTheme = isSystemInDarkTheme()) {
                ConfirmDialog(
                    url = shared,
                    onDownload = { format ->
                        DownloadService.start(this, shared, format)
                        Toast.makeText(this, "MediaGrab: download queued", Toast.LENGTH_SHORT).show()
                        finish()
                    },
                    onCancel = { finish() }
                )
            }
        }
    }

    private fun extractUrl(intent: Intent?): String? {
        if (intent?.action != Intent.ACTION_SEND) return null
        val text = intent.getStringExtra(Intent.EXTRA_TEXT) ?: return null
        return Regex("https?://[^\\s]+").find(text)?.value?.trim()
    }
}

@Composable
private fun ConfirmDialog(url: String, onDownload: (DownloadFormat) -> Unit, onCancel: () -> Unit) {
    val platform = Platform.fromUrl(url)
    AlertDialog(
        onDismissRequest = onCancel,
        title = { Text("Download from ${platform.displayName}?") },
        text = { Text(url, maxLines = 3, overflow = TextOverflow.Ellipsis) },
        confirmButton = {
            Button(onClick = { onDownload(DownloadFormat.BEST) }) {
                Icon(Icons.Filled.Download, contentDescription = null)
                Text("  Download")
            }
        },
        dismissButton = {
            Column(Modifier.fillMaxWidth(), verticalArrangement = Arrangement.spacedBy(4.dp)) {
                OutlinedButton(onClick = { onDownload(DownloadFormat.AUDIO_M4A) }, modifier = Modifier.fillMaxWidth()) {
                    Icon(Icons.Filled.MusicNote, contentDescription = null)
                    Text("  Audio only")
                }
                TextButton(onClick = onCancel) { Text("Cancel") }
            }
        }
    )
}
