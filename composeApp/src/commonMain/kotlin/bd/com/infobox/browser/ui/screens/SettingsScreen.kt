package bd.com.infobox.browser.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import bd.com.infobox.browser.Res
import bd.com.infobox.browser.dark
import bd.com.infobox.browser.language
import bd.com.infobox.browser.light
import bd.com.infobox.browser.repository.AppLanguage
import bd.com.infobox.browser.repository.AppTheme
import bd.com.infobox.browser.repository.SettingsRepository
import bd.com.infobox.browser.system
import bd.com.infobox.browser.theme
import bd.com.infobox.browser.utils.getPlatform
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.stringResource

@Composable
fun SettingsScreen(settingsRepository: SettingsRepository) {
    val selectedTheme by settingsRepository.selectedTheme.collectAsState(AppTheme.SYSTEM)
    val selectedLanguage by settingsRepository.selectedLanguage.collectAsState(AppLanguage.ENGLISH)
    val scope = rememberCoroutineScope()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(rememberScrollState())
    ) {
        // Theme Selection Section
        Text(stringResource(Res.string.theme), style = MaterialTheme.typography.titleMedium)
        Spacer(Modifier.height(8.dp))

        AppTheme.entries.forEach { theme ->
            val themeName = when(theme) {
                AppTheme.LIGHT -> stringResource(Res.string.light)
                AppTheme.DARK -> stringResource(Res.string.dark)
                AppTheme.SYSTEM -> stringResource(Res.string.system)
            }
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp)
                    .background(
                        if (selectedTheme == theme) MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.3f)
                        else Color.Transparent,
                        RoundedCornerShape(8.dp)
                    )
                    .clickable {
                        scope.launch { settingsRepository.setTheme(theme) }
                    }
                    .padding(horizontal = 12.dp)
            ) {
                RadioButton(
                    selected = selectedTheme == theme,
                    onClick = { scope.launch { settingsRepository.setTheme(theme) } }
                )
                Spacer(Modifier.width(8.dp))
                Text(themeName)
            }
            Spacer(Modifier.height(4.dp))
        }

        Spacer(Modifier.height(24.dp))

        // Language Selection Section
        Text(stringResource(Res.string.language), style = MaterialTheme.typography.titleMedium)
        Spacer(Modifier.height(8.dp))

        AppLanguage.entries.forEach { language ->
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp)
                    .background(
                        if (selectedLanguage == language) MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.3f)
                        else Color.Transparent,
                        RoundedCornerShape(8.dp)
                    )
                    .clickable {
                        scope.launch { settingsRepository.setLanguage(language) }
                    }
                    .padding(horizontal = 12.dp)
            ) {
                RadioButton(
                    selected = selectedLanguage == language,
                    onClick = { scope.launch { settingsRepository.setLanguage(language) } }
                )
                Spacer(Modifier.width(8.dp))
                Text(
                    text = "${language.flag} ${language.displayName}",
                    style = MaterialTheme.typography.bodyLarge
                )
            }
            Spacer(Modifier.height(4.dp))
        }


        HorizontalDivider(modifier = Modifier.padding(vertical = 16.dp))
        // show app version info
        val platform = remember { getPlatform() }
        Text(
            text = "App Version: ${platform.versionName} (${platform.versionCode})",
            style = MaterialTheme.typography.bodyMedium
        )
        Spacer(Modifier.height(4.dp))
        Text(
            text = "Package: ${platform.packageName}",
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Spacer(Modifier.height(4.dp))
        Text(
            text = "OS: ${platform.name}",
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Spacer(Modifier.height(16.dp))
    }
}
