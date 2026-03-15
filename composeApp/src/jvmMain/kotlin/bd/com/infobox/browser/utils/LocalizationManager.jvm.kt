package bd.com.infobox.browser.utils

import java.util.Locale

class JvmLocalizationManager : LocalizationManager {
    override fun applyLanguage(languageCode: String) {
        val locale = Locale(languageCode)
        Locale.setDefault(locale)
    }
}
