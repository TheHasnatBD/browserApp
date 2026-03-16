package bd.com.infobox.browser.utils

interface Platform {
    val name: String
    val versionCode: Int
    val versionName: String
    val packageName: String
}

expect fun getPlatform(): Platform