package com.abdellatif.clipsave.di

import android.content.Context
import com.abdellatif.clipsave.data.preferences.UserPreferences
import com.abdellatif.clipsave.data.repository.DownloadRepository

/** Manual dependency container — no Hilt/KSP, zero annotation processing. */
class AppContainer(context: Context) {
    val appContext: Context = context.applicationContext
    val downloadRepository: DownloadRepository by lazy { DownloadRepository(appContext) }
    val userPreferences: UserPreferences by lazy { UserPreferences(appContext) }
}
