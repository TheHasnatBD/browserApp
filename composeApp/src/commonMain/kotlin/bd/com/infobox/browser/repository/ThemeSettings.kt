package bd.com.infobox.browser.repository

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

enum class AppTheme {
    LIGHT, DARK, SYSTEM
}

class ThemeSettings(private val dataStore: DataStore<Preferences>) {
    private val themeKey = stringPreferencesKey("app_theme")

    val selectedTheme: Flow<AppTheme> = dataStore.data.map { preferences ->
        val themeName = preferences[themeKey] ?: AppTheme.SYSTEM.name
        try {
            AppTheme.valueOf(themeName)
        } catch (e: Exception) {
            AppTheme.SYSTEM
        }
    }

    suspend fun setTheme(theme: AppTheme) {
        dataStore.edit { preferences ->
            preferences[themeKey] = theme.name
        }
    }
}
