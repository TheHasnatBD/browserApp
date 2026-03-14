package bd.com.infobox.browser.views.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.input.key.*
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import bd.com.infobox.browser.models.BrowserTab

@Composable
fun BrowserTopBar(
    isWide: Boolean,
    activeTab: BrowserTab,
    tabs: List<BrowserTab>,
    showMenu: Boolean,
    onMenuToggle: (Boolean) -> Unit,
    canGoBack: Boolean,
    canGoForward: Boolean,
    onUrlSubmit: (String) -> Unit,
    onTabClick: () -> Unit,
    onNewTab: () -> Unit,
    onRefresh: () -> Unit,
    onBack: () -> Unit,
    onForward: () -> Unit,
    onTabSelect: (String) -> Unit,
    onTabClose: (String) -> Unit
) {
    var urlText by remember(activeTab.id) { mutableStateOf(activeTab.url) }
    var isFocused by remember { mutableStateOf(false) }
    val focusManager = LocalFocusManager.current
    
    LaunchedEffect(activeTab.url) {
        if (!isFocused) {
            urlText = activeTab.url
        }
    }

    Surface(
        shadowElevation = 1.dp,
        color = MaterialTheme.colorScheme.surface,
        tonalElevation = 1.dp
    ) {
        Column {
            if (isWide) {
                LazyRow(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.3f))
                        .padding(horizontal = 4.dp, vertical = 2.dp),
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    items(tabs, key = { it.id }) { tab ->
                        val isActive = tab.id == activeTab.id
                        Surface(
                            modifier = Modifier
                                .widthIn(max = 200.dp)
                                .height(32.dp)
                                .clickable { onTabSelect(tab.id) },
                            shape = RoundedCornerShape(topStart = 8.dp, topEnd = 8.dp),
                            color = if (isActive) MaterialTheme.colorScheme.surface else Color.Transparent,
                            border = if (isActive) BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.3f)) else null
                        ) {
                            Row(
                                modifier = Modifier.padding(horizontal = 8.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    tab.title,
                                    fontSize = 11.sp,
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis,
                                    modifier = Modifier.weight(1f),
                                    fontWeight = if (isActive) FontWeight.Bold else FontWeight.Normal
                                )
                                if (tabs.size > 1) {
                                    IconButton(
                                        onClick = { onTabClose(tab.id) },
                                        modifier = Modifier.size(14.dp)
                                    ) {
                                        Icon(Icons.Default.Close, null, modifier = Modifier.size(10.dp))
                                    }
                                }
                            }
                        }
                    }
                    item {
                        IconButton(onClick = onNewTab, modifier = Modifier.size(32.dp)) {
                            Icon(Icons.Default.Add, null, modifier = Modifier.size(18.dp))
                        }
                    }
                }
            }

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 4.dp, vertical = 4.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                if (isWide) {
                    IconButton(onClick = onBack, enabled = canGoBack, modifier = Modifier.size(36.dp)) { 
                        Icon(Icons.Default.ArrowBack, "Back", modifier = Modifier.size(18.dp)) 
                    }
                    IconButton(onClick = onForward, enabled = canGoForward, modifier = Modifier.size(36.dp)) { 
                        Icon(Icons.Default.ArrowForward, "Forward", modifier = Modifier.size(18.dp)) 
                    }
                    IconButton(onClick = onRefresh, modifier = Modifier.size(36.dp)) { 
                        Icon(Icons.Default.Refresh, "Refresh", modifier = Modifier.size(18.dp)) 
                    }
                    Spacer(Modifier.width(4.dp))
                }

                BasicTextField(
                    value = urlText,
                    onValueChange = { urlText = it },
                    modifier = Modifier
                        .weight(1f)
                        .height(36.dp)
                        .onFocusChanged { isFocused = it.isFocused }
                        .onKeyEvent {
                            if (it.key == Key.Enter || it.key == Key.NumPadEnter) {
                                if (it.type == KeyEventType.KeyDown) {
                                    onUrlSubmit(urlText)
                                    focusManager.clearFocus()
                                }
                                true
                            } else {
                                false
                            }
                        },
                    textStyle = MaterialTheme.typography.bodyMedium.copy(
                        fontSize = 14.sp,
                        color = MaterialTheme.colorScheme.onSurface
                    ),
                    singleLine = true,
                    cursorBrush = SolidColor(MaterialTheme.colorScheme.primary),
                    keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
                    keyboardActions = KeyboardActions(onSearch = { 
                        onUrlSubmit(urlText)
                        focusManager.clearFocus()
                    }),
                    decorationBox = { innerTextField ->
                        Row(
                            modifier = Modifier
                                .background(
                                    color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f),
                                    shape = RoundedCornerShape(18.dp)
                                )
                                .padding(horizontal = 12.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            val isHttps = activeTab.url.startsWith("https")
                            Icon(
                                imageVector = if (isHttps) Icons.Default.Lock else Icons.Default.LockOpen,
                                contentDescription = null,
                                modifier = Modifier.size(16.dp),
                                tint = if (isHttps) Color(0xFF4CAF50) else Color.Gray
                            )
                            
                            Spacer(Modifier.width(8.dp))
                            
                            Box(modifier = Modifier.weight(1f), contentAlignment = Alignment.CenterStart) {
                                if (urlText.isEmpty() && !isFocused) {
                                    Text(
                                        "Search or type URL",
                                        fontSize = 13.sp,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.7f)
                                    )
                                }
                                innerTextField()
                            }
                            
                            if (urlText.isNotEmpty() && isFocused) {
                                IconButton(
                                    onClick = { urlText = "" },
                                    modifier = Modifier.size(24.dp)
                                ) {
                                    Icon(
                                        Icons.Default.Clear,
                                        contentDescription = "Clear",
                                        modifier = Modifier.size(14.dp),
                                        tint = MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                }
                            }
                        }
                    }
                )
                
                Spacer(Modifier.width(4.dp))

                if (!isWide) {
                    IconButton(onClick = onTabClick, modifier = Modifier.size(36.dp)) {
                        Box(contentAlignment = Alignment.Center) {
                            Icon(Icons.Default.CropSquare, contentDescription = "Tabs", modifier = Modifier.size(22.dp))
                            Text(tabs.size.toString(), fontSize = 8.sp, fontWeight = FontWeight.Bold)
                        }
                    }
                }
                
                Box {
                    IconButton(onClick = { onMenuToggle(true) }, modifier = Modifier.size(36.dp)) {
                        Icon(Icons.Default.MoreVert, contentDescription = "Menu", modifier = Modifier.size(22.dp))
                    }
                    DropdownMenu(
                        expanded = showMenu,
                        onDismissRequest = { onMenuToggle(false) },
                        modifier = Modifier.zIndex(1000f)
                    ) {
                        DropdownMenuItem(
                            text = { Text("New Tab") },
                            onClick = {
                                onNewTab()
                                onMenuToggle(false)
                            },
                            leadingIcon = { Icon(Icons.Default.Add, null) }
                        )
                        DropdownMenuItem(
                            text = { Text("Refresh") },
                            onClick = {
                                onRefresh()
                                onMenuToggle(false)
                            },
                            leadingIcon = { Icon(Icons.Default.Refresh, null) }
                        )
                        if (isWide) {
                            DropdownMenuItem(
                                text = { Text("Home") },
                                onClick = {
                                    onUrlSubmit("https://www.google.com")
                                    onMenuToggle(false)
                                },
                                leadingIcon = { Icon(Icons.Default.Home, null) }
                            )
                        }
                    }
                }
            }
        }
    }
}
