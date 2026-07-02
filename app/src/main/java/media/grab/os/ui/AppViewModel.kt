package media.grab.os.ui

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import media.grab.os.MediaGrabApp
import media.grab.os.data.model.DownloadStatus
import media.grab.os.data.preferences.AccessMode
import media.grab.os.data.preferences.Settings
import media.grab.os.data.preferences.ThemeMode
import media.grab.os.download.DownloadService

class AppViewModel(app: Application) : AndroidViewModel(app) {

    private val container = (app as MediaGrabApp).container
    private val repo = container.downloadRepository
    private val prefs = container.userPreferences

    val downloads = repo.downloads
    val settings = prefs.settings.stateIn(viewModelScope, SharingStarted.Eagerly, Settings())

    fun download(url: String, format: media.grab.os.data.model.DownloadFormat = media.grab.os.data.model.DownloadFormat.BEST) {
        val clean = url.trim()
        if (clean.isBlank()) return
        DownloadService.start(getApplication(), clean, format)
    }

    fun retry(id: String) {
        val item = repo.get(id) ?: return
        val fmt = if (item.mediaType == media.grab.os.data.model.MediaType.AUDIO)
            media.grab.os.data.model.DownloadFormat.AUDIO_M4A
        else media.grab.os.data.model.DownloadFormat.BEST
        DownloadService.start(getApplication(), item.url, fmt, retryId = id)
    }

    fun updateEngine(onResult: (String) -> Unit) = viewModelScope.launch {
        val msg = kotlinx.coroutines.withContext(kotlinx.coroutines.Dispatchers.IO) {
            media.grab.os.download.YtDlpEngine.update(getApplication(), force = true)
        }
        onResult(msg)
    }

    fun delete(id: String) = repo.remove(id)
    fun clearCompleted() = repo.clearCompleted()
    fun clearAll() = repo.clearAll()

    fun setTheme(mode: ThemeMode) = viewModelScope.launch { prefs.setTheme(mode) }
    fun setAccessMode(mode: AccessMode) = viewModelScope.launch { prefs.setAccessMode(mode) }
    fun completeOnboarding() = viewModelScope.launch { prefs.setOnboardingDone(true) }

    val activeCount get() = downloads.value.count {
        it.status == DownloadStatus.DOWNLOADING || it.status == DownloadStatus.EXTRACTING || it.status == DownloadStatus.QUEUED
    }

    companion object {
        val Factory = object : ViewModelProvider.Factory {
            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel> create(modelClass: Class<T>, extras: androidx.lifecycle.viewmodel.CreationExtras): T {
                val app = extras[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY] as Application
                return AppViewModel(app) as T
            }
        }
    }
}
