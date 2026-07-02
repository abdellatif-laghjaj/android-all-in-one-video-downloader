package media.grab.os

import android.app.Application
import media.grab.os.di.AppContainer
import media.grab.os.download.YtDlpEngine
import media.grab.os.notif.NotificationHelper
import kotlin.concurrent.thread

class MediaGrabApp : Application() {

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
