package bd.com.infobox.browser

import bd.com.infobox.browser.utils.JvmLocalizationManager
import bd.com.infobox.browser.utils.LocalizationManager
import bd.com.infobox.browser.utils.createDataStore
import org.koin.dsl.module

actual val platformModule = module {
    single { createDataStore() }
    single<LocalizationManager> { JvmLocalizationManager() }
}
