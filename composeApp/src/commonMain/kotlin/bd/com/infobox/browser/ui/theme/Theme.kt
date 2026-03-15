package bd.com.infobox.browser.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.*
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.unit.LayoutDirection
import bd.com.infobox.browser.repository.AppLanguage
import bd.com.infobox.browser.repository.AppTheme
import bd.com.infobox.browser.repository.ThemeSettings
import bd.com.infobox.browser.utils.LocalizationManager
import org.jetbrains.compose.resources.*
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

@OptIn(ExperimentalResourceApi::class)
@Composable
fun BrowserTheme(
    themeSettings: ThemeSettings = koinInject(),
    localizationManager: LocalizationManager = koinInject(),
    content: @Composable () -> Unit
) {
    val selectedTheme by themeSettings.selectedTheme.collectAsState(AppTheme.SYSTEM)
    val selectedLanguage by themeSettings.selectedLanguage.collectAsState(AppLanguage.ENGLISH)
    val systemInDarkTheme = isSystemInDarkTheme()
    
    // Sync platform locale with saved language on startup and change
    LaunchedEffect(selectedLanguage) {
        localizationManager.applyLanguage(selectedLanguage.code)
    }

    val darkTheme = when (selectedTheme) {
        AppTheme.LIGHT -> false
        AppTheme.DARK -> true
        AppTheme.SYSTEM -> systemInDarkTheme
    }

    val colorScheme = when {
        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }

    val layoutDirection = if (selectedLanguage.isRtl) LayoutDirection.Rtl else LayoutDirection.Ltr

    CompositionLocalProvider(
        LocalLayoutDirection provides layoutDirection
    ) {
        // Use key(selectedLanguage) to force a full content refresh when language changes.
        // This ensures all stringResource calls are re-evaluated.
        key(selectedLanguage) {
            MaterialTheme(
                colorScheme = colorScheme,
                content = content
            )
        }
    }
}
