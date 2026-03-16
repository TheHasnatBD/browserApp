@file:OptIn(ExperimentalForeignApi::class)

package bd.com.infobox.browser.data.local

import androidx.room.Room
import androidx.room.RoomDatabase
import kotlinx.cinterop.ExperimentalForeignApi
import platform.Foundation.NSDocumentDirectory
import platform.Foundation.NSFileManager
import platform.Foundation.NSUserDomainMask

fun getDatabaseBuilder(): RoomDatabase.Builder<AppDatabase> {
    val directory = NSFileManager.defaultManager.URLForDirectory(
        directory = NSDocumentDirectory,
        inDomain = NSUserDomainMask,
        appropriateForURL = null,
        create = false,
        error = null
    )
    val path = directory?.path + "/${dbFileName}"
    return Room.databaseBuilder<AppDatabase>(
        name = path,
        factory = { AppDatabaseConstructor.initialize() }
    )
}
