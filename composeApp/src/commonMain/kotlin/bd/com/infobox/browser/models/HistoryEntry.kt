package bd.com.infobox.browser.models

import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.serialization.Serializable

@Serializable
@Entity(tableName = "history")
data class HistoryEntry(
    @PrimaryKey
    val id: String,
    val title: String,
    val url: String,
    val timestamp: Long
)