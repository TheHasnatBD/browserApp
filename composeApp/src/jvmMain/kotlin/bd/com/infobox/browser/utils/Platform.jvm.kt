package bd.com.infobox.browser.utils

class JVMPlatform : Platform {
    override val name: String = "${System.getProperty("os.name")} ${System.getProperty("os.version")}"
    override val versionCode: Int = 1
    override val versionName: String = System.getProperty("jpackage.app-version") ?: "1.x.x"
    override val packageName: String = "bd.com.infobox.browser"
}

actual fun getPlatform(): Platform = JVMPlatform()