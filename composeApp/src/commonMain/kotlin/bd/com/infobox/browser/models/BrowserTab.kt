package bd.com.infobox.browser.models

import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.serialization.Serializable

@Serializable
@Entity(tableName = "browser_tabs")
data class BrowserTab(
    @PrimaryKey val id: String,
    val title: String = "New Tab",
    val url: String = "about:blank",
    val isLoading: Boolean = false,
    val progress: Float = 0f,
    val lastAccessed: Long = 0L
)