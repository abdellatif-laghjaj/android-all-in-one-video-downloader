package media.grab.os.ui

import android.Manifest
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.viewmodel.compose.viewModel
import media.grab.os.data.model.DownloadFormat
import media.grab.os.data.model.Platform
import media.grab.os.data.preferences.ThemeMode
import media.grab.os.download.DownloadService
import media.grab.os.ui.navigation.AppScaffold
import media.grab.os.ui.onboarding.OnboardingScreen
import media.grab.os.ui.theme.MediaGrabTheme

class MainActivity : ComponentActivity() {

    private val requestNotif =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { }

    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen()
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        maybeRequestNotifications()
        setContent { MediaGrabRoot() }
        handleIntent(intent)
    }

    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        setIntent(intent)
        handleIntent(intent)
    }

    /** "Quick grab": triggered by the floating bubble — download whatever link is on the clipboard. */
    private fun handleIntent(intent: Intent?) {
        if (intent?.action != ACTION_QUICK_GRAB) return
        val clip = readClipboardUrl()
        if (clip != null) {
            DownloadService.start(this, clip, DownloadFormat.BEST)
            Toast.makeText(this, "Downloading from ${Platform.fromUrl(clip).displayName}…", Toast.LENGTH_LONG).show()
        } else {
            Toast.makeText(this, "No link on clipboard. In the app tap ••• → Copy link, then tap the bubble again.", Toast.LENGTH_LONG).show()
        }
    }

    private fun readClipboardUrl(): String? {
        val cm = getSystemService(Context.CLIPBOARD_SERVICE) as? ClipboardManager ?: return null
        val text = cm.primaryClip?.getItemAt(0)?.text?.toString() ?: return null
        return Regex("https?://[^\\s]+").find(text)?.value
    }

    private fun maybeRequestNotifications() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            requestNotif.launch(Manifest.permission.POST_NOTIFICATIONS)
        }
    }

    companion object {
        const val ACTION_QUICK_GRAB = "media.grab.os.action.QUICK_GRAB"
    }
}

@Composable
fun MediaGrabRoot() {
    val vm: AppViewModel = viewModel(factory = AppViewModel.Factory)
    val settings by vm.settings.collectAsState()

    val dark = when (settings.themeMode) {
        ThemeMode.LIGHT -> false
        ThemeMode.DARK -> true
        ThemeMode.SYSTEM -> isSystemInDarkTheme()
    }

    MediaGrabTheme(darkTheme = dark) {
        Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background) {
            if (!settings.onboardingDone) {
                OnboardingScreen(onDone = { vm.completeOnboarding() })
            } else {
                AppScaffold(vm = vm)
            }
        }
    }
}
