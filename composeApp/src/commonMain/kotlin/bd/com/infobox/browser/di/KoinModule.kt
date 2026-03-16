package bd.com.infobox.browser.di

import bd.com.infobox.browser.data.local.AppDatabase
import bd.com.infobox.browser.repository.BrowserRepository
import bd.com.infobox.browser.repository.SettingsRepository
import org.koin.core.context.startKoin
import org.koin.core.module.Module
import org.koin.dsl.KoinAppDeclaration
import org.koin.dsl.module

expect val platformModule: Module

val appModule = module {
    single { SettingsRepository(get(), get()) }
    single { BrowserRepository(get(), get(), get()) }
    
    // DAOs
    single { get<AppDatabase>().historyDao() }
    single { get<AppDatabase>().bookmarkDao() }
    single { get<AppDatabase>().browserTabDao() }
}

fun initKoin(appDeclaration: KoinAppDeclaration = {}) =
    startKoin {
        appDeclaration()
        modules(appModule, platformModule)
    }
