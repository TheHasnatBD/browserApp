package bd.com.infobox.browser

import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import bd.com.infobox.browser.ui.theme.BrowserTheme
import bd.com.infobox.browser.views.BrowserScreen
import com.multiplatform.webview.util.addTempDirectoryRemovalHook
import dev.datlag.kcef.KCEF
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File
import kotlin.math.max

fun main() {
    initKoin()
    application {
        addTempDirectoryRemovalHook()
        Window(
            onCloseRequest = ::exitApplication,
            title = "BrowserApp",
        ) {
            var restartRequired by remember { mutableStateOf(false) }
            var downloading by remember { mutableStateOf(0F) }
            var initialized by remember { mutableStateOf(false) }

            LaunchedEffect(Unit) {
                withContext(Dispatchers.IO) {
                    KCEF.init(builder = {
                        installDir(File("kcef-bundle"))
                        progress {
                            onDownloading {
                                downloading = max(it, 0F)
                            }
                            onInitialized {
                                initialized = true
                            }
                        }
                        settings {
                            cachePath = File("cache").absolutePath
                        }
                    }, onError = {
                        it?.printStackTrace()
                    }, onRestartRequired = {
                        restartRequired = true
                    })
                }
            }

            BrowserTheme {
                if (restartRequired) {
                    Text(text = "Restart required.")
                } else {
                    if (initialized) {
                        BrowserScreen()
                    } else {
                        //Text(text = "Downloading $downloading%")
                    }
                }
            }

            DisposableEffect(Unit) {
                onDispose {
                    KCEF.disposeBlocking()
                }
            }
        }
    }
}
