package bd.com.infobox.browser.data.local.dao

import androidx.room.*
import bd.com.infobox.browser.models.BrowserTab
import kotlinx.coroutines.flow.Flow

@Dao
interface BrowserTabDao {
    @Query("SELECT * FROM browser_tabs ORDER BY lastAccessed DESC")
    fun getAllTabs(): Flow<List<BrowserTab>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(tab: BrowserTab)

    @Update
    suspend fun update(tab: BrowserTab)

    @Delete
    suspend fun delete(tab: BrowserTab)

    @Query("DELETE FROM browser_tabs WHERE id = :id")
    suspend fun deleteById(id: String)

    @Query("DELETE FROM browser_tabs")
    suspend fun deleteAll()
}
