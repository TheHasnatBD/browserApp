package bd.com.infobox.browser.di

import bd.com.infobox.browser.data.local.AppDatabase
import bd.com.infobox.browser.data.local.getDatabaseBuilder
import bd.com.infobox.browser.data.local.getRoomDatabase
import bd.com.infobox.browser.utils.IosLocalizationManager
import bd.com.infobox.browser.utils.LocalizationManager
import bd.com.infobox.browser.data.cache.createDataStore
import org.koin.dsl.module

actual val platformModule = module {
    single { createDataStore() }
    single<LocalizationManager> { IosLocalizationManager() }
    single<AppDatabase> { getRoomDatabase(getDatabaseBuilder()) }
}
