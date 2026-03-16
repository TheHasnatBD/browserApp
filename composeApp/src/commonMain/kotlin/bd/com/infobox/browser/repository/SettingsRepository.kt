package bd.com.infobox.browser.repository

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import bd.com.infobox.browser.utils.LocalizationManager
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

enum class AppTheme {
    LIGHT, DARK, SYSTEM
}

enum class AppLanguage(val code: String, val displayName: String, val flag: String, val isRtl: Boolean = false) {
    ENGLISH("en", "English", "🇺🇸"),
    BENGALI("bn", "বাংলা", "🇧🇩"),
    HINDI("hi", "हिन्दी", "🇮🇳"),
    ARABIC("ar", "العربية", "🇸🇦", isRtl = true),
    FRENCH("fr", "Français", "🇫🇷")
}

class SettingsRepository(
    private val dataStore: DataStore<Preferences>,
    private val localizationManager: LocalizationManager
) {
    private val themeKey = stringPreferencesKey("app_theme")
    private val languageKey = stringPreferencesKey("app_language")
    private val activeTabIdKey = stringPreferencesKey("active_tab_id")

    val selectedTheme: Flow<AppTheme> = dataStore.data.map { preferences ->
        val themeName = preferences[themeKey] ?: AppTheme.SYSTEM.name
        try {
            AppTheme.valueOf(themeName)
        } catch (e: Exception) {
            AppTheme.SYSTEM
        }
    }

    val selectedLanguage: Flow<AppLanguage> = dataStore.data.map { preferences ->
        val langCode = preferences[languageKey] ?: AppLanguage.ENGLISH.code
        AppLanguage.entries.find { it.code == langCode } ?: AppLanguage.ENGLISH
    }

    val activeTabId: Flow<String?> = dataStore.data.map { preferences ->
        preferences[activeTabIdKey]
    }

    suspend fun setTheme(theme: AppTheme) {
        dataStore.edit { preferences ->
            preferences[themeKey] = theme.name
        }
    }

    suspend fun setLanguage(language: AppLanguage) {
        dataStore.edit { preferences ->
            preferences[languageKey] = language.code
        }
        localizationManager.applyLanguage(language.code)
    }

    suspend fun setActiveTabId(id: String) {
        dataStore.edit { preferences ->
            preferences[activeTabIdKey] = id
        }
    }
}
