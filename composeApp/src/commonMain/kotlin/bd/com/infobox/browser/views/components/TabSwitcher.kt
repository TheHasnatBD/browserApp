package bd.com.infobox.browser.views.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import bd.com.infobox.browser.Res
import bd.com.infobox.browser.close
import bd.com.infobox.browser.close_tab
import bd.com.infobox.browser.models.BrowserTab
import bd.com.infobox.browser.new_tab
import bd.com.infobox.browser.tabs
import org.jetbrains.compose.resources.stringResource

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TabSwitcher(
    tabs: List<BrowserTab>,
    activeTabId: String,
    onTabSelect: (String) -> Unit,
    onTabClose: (String) -> Unit,
    onNewTab: () -> Unit,
    onClose: () -> Unit
) {
    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        Column {
            TopAppBar(
                title = { Text(stringResource(Res.string.tabs)) },
                navigationIcon = {
                    IconButton(onClick = onClose) { 
                        Icon(Icons.Default.Close, stringResource(Res.string.close)) 
                    }
                },
                actions = {
                    IconButton(onClick = onNewTab) { 
                        Icon(Icons.Default.Add, stringResource(Res.string.new_tab)) 
                    }
                }
            )
            BoxWithConstraints(modifier = Modifier.fillMaxSize()) {
                val columns = if (maxWidth > 900.dp) 4 else if (maxWidth > 600.dp) 3 else if (maxWidth > 400.dp) 2 else 1
                
                LazyVerticalGrid(
                    columns = GridCells.Fixed(columns),
                    contentPadding = PaddingValues(16.dp),
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(tabs, key = { it.id }) { tab ->
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(120.dp)
                                .clickable { onTabSelect(tab.id) },
                            border = if (tab.id == activeTabId) BorderStroke(2.dp, MaterialTheme.colorScheme.primary) else null
                        ) {
                            Column(modifier = Modifier.padding(12.dp).fillMaxSize()) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Text(
                                        tab.title,
                                        fontWeight = FontWeight.Bold,
                                        maxLines = 1,
                                        overflow = TextOverflow.Ellipsis,
                                        modifier = Modifier.weight(1f)
                                    )
                                    if (tabs.size > 1) {
                                        IconButton(
                                            onClick = { onTabClose(tab.id) },
                                            modifier = Modifier.size(24.dp)
                                        ) {
                                            Icon(Icons.Default.Close, stringResource(Res.string.close_tab), modifier = Modifier.size(16.dp))
                                        }
                                    }
                                }
                                Spacer(Modifier.height(4.dp))
                                Text(
                                    tab.url,
                                    fontSize = 12.sp,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                                    maxLines = 2,
                                    overflow = TextOverflow.Ellipsis
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}
