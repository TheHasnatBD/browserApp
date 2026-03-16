package bd.com.infobox.browser.repository

import bd.com.infobox.browser.data.local.dao.BookmarkDao
import bd.com.infobox.browser.data.local.dao.HistoryDao
import bd.com.infobox.browser.models.Bookmark
import bd.com.infobox.browser.models.HistoryEntry
import bd.com.infobox.browser.utils.formatMillisWithTimeNow
import kotlinx.coroutines.flow.Flow
import kotlin.time.Clock

class BrowserRepository(
    private val historyDao: HistoryDao,
    private val bookmarkDao: BookmarkDao
) {
    // History
    fun getAllHistory(): Flow<List<HistoryEntry>> = historyDao.getAllHistory()

    suspend fun addHistoryEntry(title: String, url: String) {
        if (url.isBlank() || url == "about:blank") return
        
        val entry = HistoryEntry(
            id = url.hashCode().toString() + kotlin.time.Clock.System.now().toEpochMilliseconds().toString(),
            title = if (title.isBlank()) url else title,
            url = url,
            timestamp = kotlin.time.Clock.System.now().toEpochMilliseconds()
        )
        historyDao.insert(entry)
    }

    suspend fun deleteHistoryEntry(id: String) = historyDao.deleteById(id)
    
    suspend fun clearHistory() = historyDao.deleteAll()

    // Bookmarks
    fun getAllBookmarks(): Flow<List<Bookmark>> = bookmarkDao.getAllBookmarks()

    suspend fun addBookmark(title: String, url: String) {
        val bookmark = Bookmark(
            id = url.hashCode().toString(),
            title = if (title.isBlank()) url else title,
            url = url,
            timestamp = Clock.System.now().toEpochMilliseconds()
        )
        bookmarkDao.insert(bookmark)
    }

    suspend fun removeBookmark(id: String) = bookmarkDao.deleteById(id)
    
    suspend fun removeBookmarkByUrl(url: String) = bookmarkDao.deleteByUrl(url)

    fun isBookmarked(url: String): Flow<Boolean> = bookmarkDao.isBookmarked(url)
}