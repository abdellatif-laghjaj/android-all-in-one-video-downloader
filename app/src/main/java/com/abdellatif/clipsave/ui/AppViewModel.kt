package com.abdellatif.clipsave.ui

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import com.abdellatif.clipsave.ClipSaveApp
import com.abdellatif.clipsave.data.model.DownloadStatus
import com.abdellatif.clipsave.data.preferences.AccessMode
import com.abdellatif.clipsave.data.preferences.Settings
import com.abdellatif.clipsave.data.preferences.ThemeMode
import com.abdellatif.clipsave.download.DownloadService

class AppViewModel(app: Application) : AndroidViewModel(app) {

    private val container = (app as ClipSaveApp).container
    private val repo = container.downloadRepository
    private val prefs = container.userPreferences

    val downloads = repo.downloads
    val settings = prefs.settings.stateIn(viewModelScope, SharingStarted.Eagerly, Settings())

    fun download(
        url: String,
        format: com.abdellatif.clipsave.data.model.DownloadFormat = com.abdellatif.clipsave.data.model.DownloadFormat.BEST
    ) {
        val clean = url.trim()
        if (clean.isBlank()) return
        DownloadService.start(getApplication(), clean, format)
    }

    fun retry(id: String) {
        val item = repo.get(id) ?: return
        val fmt = if (item.mediaType == com.abdellatif.clipsave.data.model.MediaType.AUDIO)
            com.abdellatif.clipsave.data.model.DownloadFormat.AUDIO_M4A
        else com.abdellatif.clipsave.data.model.DownloadFormat.BEST
        DownloadService.start(getApplication(), item.url, fmt, retryId = id)
    }

    fun updateEngine(onResult: (String) -> Unit) = viewModelScope.launch {
        val msg = kotlinx.coroutines.withContext(kotlinx.coroutines.Dispatchers.IO) {
            com.abdellatif.clipsave.download.YtDlpEngine.update(getApplication(), force = true)
        }
        onResult(msg)
    }

    fun delete(id: String) = repo.remove(id)
    fun clearCompleted() = repo.clearCompleted()
    fun clearAll() = repo.clearAll()

    fun setTheme(mode: ThemeMode) = viewModelScope.launch { prefs.setTheme(mode) }
    fun setAccessMode(mode: AccessMode) = viewModelScope.launch { prefs.setAccessMode(mode) }
    fun completeOnboarding() = viewModelScope.launch { prefs.setOnboardingDone(true) }

    val activeCount
        get() = downloads.value.count {
            it.status == DownloadStatus.DOWNLOADING || it.status == DownloadStatus.EXTRACTING || it.status == DownloadStatus.QUEUED
        }

    companion object {
        val Factory = object : ViewModelProvider.Factory {
            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel> create(
                modelClass: Class<T>,
                extras: androidx.lifecycle.viewmodel.CreationExtras
            ): T {
                val app =
                    extras[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY] as Application
                return AppViewModel(app) as T
            }
        }
    }
}
