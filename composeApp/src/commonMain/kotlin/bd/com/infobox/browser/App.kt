package bd.com.infobox.browser

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import bd.com.infobox.browser.ui.theme.BrowserTheme
import bd.com.infobox.browser.views.BrowserScreen

@Composable
fun App() {
    BrowserTheme {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background
        ) {
            BrowserScreen()
        }
    }
}
