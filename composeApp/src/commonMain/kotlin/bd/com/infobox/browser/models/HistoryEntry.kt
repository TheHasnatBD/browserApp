package bd.com.infobox.browser.models

import kotlinx.serialization.Serializable

@Serializable
data class HistoryEntry(
    val id: String,
    val title: String,
    val url: String,
    val timestamp: Long
)