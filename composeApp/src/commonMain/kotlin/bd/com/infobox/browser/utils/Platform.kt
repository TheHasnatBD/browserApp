package bd.com.infobox.browser.utils

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform