package bd.com.infobox.browser.repository

import bd.com.infobox.browser.data.local.dao.BookmarkDao
import bd.com.infobox.browser.data.local.dao.BrowserTabDao
import bd.com.infobox.browser.data.local.dao.HistoryDao
import bd.com.infobox.browser.models.Bookmark
import bd.com.infobox.browser.models.BrowserTab
import bd.com.infobox.browser.models.HistoryEntry
import kotlinx.coroutines.flow.Flow
import kotlin.time.Clock

class BrowserRepository(
    private val historyDao: HistoryDao,
    private val bookmarkDao: BookmarkDao,
    private val tabDao: BrowserTabDao
) {
    // Tabs
    fun getAllTabs(): Flow<List<BrowserTab>> = tabDao.getAllTabs()

    suspend fun saveTab(tab: BrowserTab) {
        tabDao.insert(tab.copy(lastAccessed = Clock.System.now().toEpochMilliseconds()))
    }

    suspend fun deleteTab(id: String) = tabDao.deleteById(id)

    suspend fun clearAllTabs() = tabDao.deleteAll()

    // History
    fun getAllHistory(): Flow<List<HistoryEntry>> = historyDao.getAllHistory()

    suspend fun addHistoryEntry(title: String, url: String) {
        if (url.isBlank() || url == "about:blank") return
        
        val now = Clock.System.now().toEpochMilliseconds()
        val entry = HistoryEntry(
            id = url.hashCode().toString() + now.toString(),
            title = title.ifBlank { url },
            url = url,
            timestamp = now
        )
        historyDao.insert(entry)
    }

    suspend fun deleteHistoryEntry(id: String) = historyDao.deleteById(id)
    
    suspend fun clearHistory() = historyDao.deleteAll()

    // Bookmarks
    fun getAllBookmarks(): Flow<List<Bookmark>> = bookmarkDao.getAllBookmarks()

    suspend fun addBookmark(title: String, url: String) {
        val now = Clock.System.now().toEpochMilliseconds()
        val bookmark = Bookmark(
            id = url.hashCode().toString(),
            title = title.ifBlank { url },
            url = url,
            timestamp = now
        )
        bookmarkDao.insert(bookmark)
    }

    suspend fun removeBookmark(id: String) = bookmarkDao.deleteById(id)
    
    suspend fun removeBookmarkByUrl(url: String) = bookmarkDao.deleteByUrl(url)

    fun isBookmarked(url: String): Flow<Boolean> = bookmarkDao.isBookmarked(url)
}