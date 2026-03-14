package bd.com.infobox.browser.models

import kotlinx.serialization.Serializable

@Serializable
data class BrowserTab(
    val id: String,
    val title: String = "New Tab",
    val url: String = "about:blank",
    val isLoading: Boolean = false,
    val progress: Float = 0f
)