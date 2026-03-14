package bd.com.infobox.browser.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import bd.com.infobox.browser.repository.AppTheme
import bd.com.infobox.browser.repository.ThemeSettings
import org.koin.compose.koinInject

private val DarkColorScheme = darkColorScheme(
    primary = Purple80,
    secondary = PurpleGrey80,
    tertiary = Pink80
)

private val LightColorScheme = lightColorScheme(
    primary = Purple40,
    secondary = PurpleGrey40,
    tertiary = Pink40
)

@Composable
fun BrowserTheme(
    themeSettings: ThemeSettings = koinInject(),
    content: @Composable () -> Unit
) {
    val selectedTheme by themeSettings.selectedTheme.collectAsState(AppTheme.SYSTEM)
    val systemInDarkTheme = isSystemInDarkTheme()
    
    val darkTheme = when (selectedTheme) {
        AppTheme.LIGHT -> false
        AppTheme.DARK -> true
        AppTheme.SYSTEM -> systemInDarkTheme
    }

    val colorScheme = when {
        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }

    MaterialTheme(
        colorScheme = colorScheme,
        content = content
    )
}
