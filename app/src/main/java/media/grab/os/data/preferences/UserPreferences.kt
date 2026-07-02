package media.grab.os.data.preferences

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

enum class ThemeMode { SYSTEM, LIGHT, DARK }
enum class AccessMode { NORMAL, ACCESSIBILITY, SHIZUKU, ROOT }

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "mediagrab_prefs")

data class Settings(
    val themeMode: ThemeMode = ThemeMode.SYSTEM,
    val accessMode: AccessMode = AccessMode.NORMAL,
    val onboardingDone: Boolean = false
)

class UserPreferences(private val context: Context) {

    private object Keys {
        val THEME = stringPreferencesKey("theme_mode")
        val ACCESS = stringPreferencesKey("access_mode")
        val ONBOARDING = booleanPreferencesKey("onboarding_done")
    }

    val settings: Flow<Settings> = context.dataStore.data.map { p ->
        Settings(
            themeMode = runCatching { ThemeMode.valueOf(p[Keys.THEME] ?: "SYSTEM") }.getOrDefault(ThemeMode.SYSTEM),
            accessMode = runCatching { AccessMode.valueOf(p[Keys.ACCESS] ?: "NORMAL") }.getOrDefault(AccessMode.NORMAL),
            onboardingDone = p[Keys.ONBOARDING] ?: false
        )
    }

    suspend fun setTheme(mode: ThemeMode) {
        context.dataStore.edit { it[Keys.THEME] = mode.name }
    }

    suspend fun setAccessMode(mode: AccessMode) {
        context.dataStore.edit { it[Keys.ACCESS] = mode.name }
    }

    suspend fun setOnboardingDone(done: Boolean) {
        context.dataStore.edit { it[Keys.ONBOARDING] = done }
    }
}
