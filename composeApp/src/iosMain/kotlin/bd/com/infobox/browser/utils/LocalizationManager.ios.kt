package bd.com.infobox.browser.utils

import platform.Foundation.NSUserDefaults
import platform.Foundation.NSLocale
import platform.Foundation.preferredLanguages

class IosLocalizationManager : LocalizationManager {
    override fun applyLanguage(languageCode: String) {
        val userDefaults = NSUserDefaults.standardUserDefaults
        val languages = listOf(languageCode)
        userDefaults.setObject(languages, "AppleLanguages")
        userDefaults.synchronize()
    }
}
