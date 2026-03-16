package bd.com.infobox.browser.data.local

import androidx.room.ConstructedBy
import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.RoomDatabaseConstructor
import androidx.sqlite.driver.bundled.BundledSQLiteDriver
import bd.com.infobox.browser.data.local.dao.BookmarkDao
import bd.com.infobox.browser.data.local.dao.BrowserTabDao
import bd.com.infobox.browser.data.local.dao.HistoryDao
import bd.com.infobox.browser.models.Bookmark
import bd.com.infobox.browser.models.BrowserTab
import bd.com.infobox.browser.models.HistoryEntry
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO

internal const val dbFileName = "aah_browser.db"

@Database(entities = [HistoryEntry::class, Bookmark::class, BrowserTab::class], version = 2)
@ConstructedBy(AppDatabaseConstructor::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun historyDao(): HistoryDao
    abstract fun bookmarkDao(): BookmarkDao
    abstract fun browserTabDao(): BrowserTabDao
}

expect object AppDatabaseConstructor : RoomDatabaseConstructor<AppDatabase> {
    override fun initialize(): AppDatabase
}

fun getRoomDatabase(
    builder: RoomDatabase.Builder<AppDatabase>
): AppDatabase {
    return builder
        .setDriver(BundledSQLiteDriver())
        .setQueryCoroutineContext(Dispatchers.IO)
        .fallbackToDestructiveMigration(true)
        .build()
}