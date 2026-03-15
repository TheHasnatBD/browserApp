package bd.com.infobox.browser

import android.content.Context
import bd.com.infobox.browser.utils.AndroidLocalizationManager
import bd.com.infobox.browser.utils.LocalizationManager
import bd.com.infobox.browser.utils.createDataStore
import org.koin.dsl.module

actual val platformModule = module {
    single { createDataStore(get<Context>()) }
    single<LocalizationManager> { AndroidLocalizationManager(get()) }
}
