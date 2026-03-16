package bd.com.infobox.browser.utils

import platform.UIKit.UIDevice
import platform.Foundation.NSBundle

class IOSPlatform: Platform {
    override val name: String = UIDevice.currentDevice.systemName() + " " + UIDevice.currentDevice.systemVersion
    override val versionCode: Int = (NSBundle.mainBundle.infoDictionary?.get("CFBundleVersion") as? String)?.toIntOrNull() ?: 1
    override val versionName: String = (NSBundle.mainBundle.infoDictionary?.get("CFBundleShortVersionString") as? String) ?: "1.0"
    override val packageName: String = NSBundle.mainBundle.bundleIdentifier ?: "bd.com.infobox.browser"
}

actual fun getPlatform(): Platform = IOSPlatform()