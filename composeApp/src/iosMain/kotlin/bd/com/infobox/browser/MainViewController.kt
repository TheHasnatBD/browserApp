package bd.com.infobox.browser

import androidx.compose.ui.window.ComposeUIViewController
import bd.com.infobox.browser.di.initKoin

fun MainViewController() = ComposeUIViewController(
    configure = {
        initKoin()
    }
) {
    App()
}
