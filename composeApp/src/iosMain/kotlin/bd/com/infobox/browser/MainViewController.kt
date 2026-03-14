package bd.com.infobox.browser

import androidx.compose.ui.window.ComposeUIViewController
import bd.com.infobox.browser.views.BrowserScreen

fun MainViewController() = ComposeUIViewController(
    configure = {
        initKoin()
    }
) {
    App()
}
