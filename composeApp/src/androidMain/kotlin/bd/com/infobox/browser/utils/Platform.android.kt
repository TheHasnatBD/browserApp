package bd.com.infobox.browser.utils

import android.os.Build
import bd.com.infobox.browser.BuildConfig

class AndroidPlatform : Platform {
    override val name: String = "Android ${Build.VERSION.SDK_INT}"
    override val versionCode: Int = BuildConfig.VERSION_CODE
    override val versionName: String = BuildConfig.VERSION_NAME
    override val packageName: String = BuildConfig.APPLICATION_ID
}

actual fun getPlatform(): Platform = AndroidPlatform()