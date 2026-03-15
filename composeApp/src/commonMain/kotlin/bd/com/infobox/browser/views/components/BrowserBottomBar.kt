package bd.com.infobox.browser.views.components

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import bd.com.infobox.browser.Res
import bd.com.infobox.browser.back
import bd.com.infobox.browser.bookmarks
import bd.com.infobox.browser.forward
import bd.com.infobox.browser.history
import bd.com.infobox.browser.home
import org.jetbrains.compose.resources.stringResource

@Composable
fun BrowserBottomBar(
    canGoBack: Boolean,
    canGoForward: Boolean,
    onBack: () -> Unit,
    onForward: () -> Unit,
    onHome: () -> Unit,
    onBookmarks: () -> Unit,
    onHistory: () -> Unit
) {
    Surface(
        color = MaterialTheme.colorScheme.surfaceVariant,
        tonalElevation = 2.dp
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .windowInsetsPadding(WindowInsets.navigationBars)
                .padding(vertical = 4.dp),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = onBack, enabled = canGoBack) { 
                Icon(Icons.AutoMirrored.Filled.ArrowBack, stringResource(Res.string.back))
            }
            IconButton(onClick = onForward, enabled = canGoForward) { 
                Icon(Icons.AutoMirrored.Filled.ArrowForward, stringResource(Res.string.forward)) 
            }
            IconButton(onClick = onHome) { 
                Icon(Icons.Default.Home, stringResource(Res.string.home)) 
            }
            IconButton(onClick = onBookmarks) { 
                Icon(Icons.Default.BookmarkBorder, stringResource(Res.string.bookmarks)) 
            }
            IconButton(onClick = onHistory) { 
                Icon(Icons.Default.History, stringResource(Res.string.history))
            }
        }
    }
}
