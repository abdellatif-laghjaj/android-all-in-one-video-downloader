package com.abdellatif.clipsave

import android.app.Application
import com.abdellatif.clipsave.di.AppContainer
import com.abdellatif.clipsave.download.YtDlpEngine
import com.abdellatif.clipsave.notif.NotificationHelper
import kotlin.concurrent.thread

class ClipSaveApp : Application() {

    lateinit var container: AppContainer
        private set

    override fun onCreate() {
        super.onCreate()
        container = AppContainer(this)
        NotificationHelper.ensureChannel(this)
        // Warm up yt-dlp off the main thread (first run unpacks python/ffmpeg).
        thread(start = true, isDaemon = true) {
            runCatching {
                if (YtDlpEngine.ensureInit(this)) {
                    // Refresh extractors so new site changes keep working.
                    runCatching { YtDlpEngine.update(this) }
                }
            }
        }
    }
}
