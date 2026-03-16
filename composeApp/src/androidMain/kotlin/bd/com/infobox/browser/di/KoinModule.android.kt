package bd.com.infobox.browser.di

import android.content.Context
import bd.com.infobox.browser.data.local.AppDatabase
import bd.com.infobox.browser.data.local.getDatabaseBuilder
import bd.com.infobox.browser.data.local.getRoomDatabase
import bd.com.infobox.browser.utils.AndroidLocalizationManager
import bd.com.infobox.browser.utils.LocalizationManager
import bd.com.infobox.browser.data.cache.createDataStore
import org.koin.dsl.module

actual val platformModule = module {
    single { createDataStore(get<Context>()) }
    single<LocalizationManager> { AndroidLocalizationManager(get()) }
    single<AppDatabase> { getRoomDatabase(getDatabaseBuilder(get())) }
}
