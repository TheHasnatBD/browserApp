package bd.com.infobox.browser.models

import kotlinx.serialization.Serializable

@Serializable
data class Bookmark(
    val id: String,
    val title: String,
    val url: String
)