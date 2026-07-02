package media.grab.os.di

import android.content.Context
import media.grab.os.data.preferences.UserPreferences
import media.grab.os.data.repository.DownloadRepository

/** Manual dependency container — no Hilt/KSP, zero annotation processing. */
class AppContainer(context: Context) {
    val appContext: Context = context.applicationContext
    val downloadRepository: DownloadRepository by lazy { DownloadRepository(appContext) }
    val userPreferences: UserPreferences by lazy { UserPreferences(appContext) }
}
