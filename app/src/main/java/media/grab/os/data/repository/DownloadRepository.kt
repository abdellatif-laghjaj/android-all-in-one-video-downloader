package media.grab.os.data.repository

import android.content.Context
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import media.grab.os.data.model.Download
import media.grab.os.data.model.DownloadStatus
import java.io.File

/**
 * Simple JSON-file backed store for download history. No Room / KSP required.
 */
class DownloadRepository(context: Context) {

    private val json = Json { ignoreUnknownKeys = true; prettyPrint = false }
    private val file = File(context.filesDir, "downloads.json")
    private val lock = Any()

    private val _downloads = MutableStateFlow<List<Download>>(emptyList())
    val downloads: StateFlow<List<Download>> = _downloads.asStateFlow()

    init {
        _downloads.value = load()
    }

    private fun load(): List<Download> = synchronized(lock) {
        runCatching {
            if (file.exists()) json.decodeFromString<List<Download>>(file.readText()) else emptyList()
        }.getOrDefault(emptyList()).sortedByDescending { it.createdAt }
    }

    private fun persist(list: List<Download>) = synchronized(lock) {
        runCatching { file.writeText(json.encodeToString(list)) }
    }

    fun upsert(download: Download) {
        val current = _downloads.value.toMutableList()
        val idx = current.indexOfFirst { it.id == download.id }
        if (idx >= 0) current[idx] = download else current.add(0, download)
        val sorted = current.sortedByDescending { it.createdAt }
        _downloads.value = sorted
        persist(sorted)
    }

    fun get(id: String): Download? = _downloads.value.firstOrNull { it.id == id }

    fun remove(id: String) {
        val updated = _downloads.value.filterNot { it.id == id }
        _downloads.value = updated
        persist(updated)
    }

    fun clearAll() {
        _downloads.value = emptyList()
        persist(emptyList())
    }

    fun clearCompleted() {
        val updated = _downloads.value.filterNot { it.status == DownloadStatus.COMPLETED }
        _downloads.value = updated
        persist(updated)
    }
}
