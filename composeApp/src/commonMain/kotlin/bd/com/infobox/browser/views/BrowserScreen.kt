package bd.com.infobox.browser.views

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import bd.com.infobox.browser.*
import bd.com.infobox.browser.models.BrowserTab
import bd.com.infobox.browser.repository.AppLanguage
import bd.com.infobox.browser.repository.AppTheme
import bd.com.infobox.browser.repository.ThemeSettings
import bd.com.infobox.browser.views.components.BrowserBottomBar
import bd.com.infobox.browser.views.components.BrowserTopBar
import bd.com.infobox.browser.views.components.TabSwitcher
import com.multiplatform.webview.web.*
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.koinInject

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BrowserScreen(
    themeSettings: ThemeSettings = koinInject()
) {
    val scope = rememberCoroutineScope()
    
    // Main state for the list of tabs
    var tabs by remember { mutableStateOf(listOf(
        BrowserTab(id = "1", url = "https://www.google.com")
    )) }
    var activeTabId by remember { mutableStateOf("1") }

    // UI state for overlays
    var showTabSwitcher by remember { mutableStateOf(false) }
    var showHistory by remember { mutableStateOf(false) }
    var showBookmarks by remember { mutableStateOf(false) }
    var showSettings by remember { mutableStateOf(false) }

    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)

    // Maps to keep WebView state and navigator persistent across tab switches
    val webViewStates = remember { mutableStateMapOf<String, WebViewState>() }
    val webViewNavigators = remember { mutableStateMapOf<String, WebViewNavigator>() }
    val initializedTabs = remember { mutableStateSetOf<String>() }

    // active tab is initialized so its WebView is composed
    LaunchedEffect(activeTabId) {
        initializedTabs.add(activeTabId)
    }

    // Reference to the active tab metadata
    val activeTab = remember(tabs, activeTabId) {
        tabs.find { it.id == activeTabId } ?: tabs.first()
    }
    
    // Navigator for the currently active tab (used for TopBar/BottomBar actions)
    val currentNavigator = webViewNavigators.getOrPut(activeTabId) { WebViewNavigator(scope) }

    /**
     * Updates tab metadata and triggers the WebView load
     */
    fun navigateTab(tabId: String, url: String) {
        val trimmedUrl = url.trim()
        if (trimmedUrl.isBlank()) return
        
        val finalUrl = if (trimmedUrl.startsWith("http://") || trimmedUrl.startsWith("https://")) {
            trimmedUrl
        } else if (trimmedUrl.contains(".") && !trimmedUrl.contains(" ")) {
            "https://$trimmedUrl"
        } else {
            "https://www.google.com/search?q=$trimmedUrl"
        }

        // 1. Update metadata list immediately
        tabs = tabs.map { 
            if (it.id == tabId) it.copy(url = finalUrl, isLoading = true, progress = 0.05f) else it 
        }
        
        // 2. Explicitly trigger navigation on the WebView state and navigator
        webViewStates[tabId]?.let { state ->
            state.content = WebContent.Url(finalUrl)
        }
        webViewNavigators[tabId]?.loadUrl(finalUrl)
    }

    fun addNewTab() {
        val newId = ((tabs.maxOfOrNull { try { it.id.toInt() } catch(e: Exception) { 0 } } ?: 0) + 1).toString()
        tabs = tabs + BrowserTab(id = newId, url = "https://www.google.com")
        activeTabId = newId
        showTabSwitcher = false
        scope.launch { drawerState.close() }
    }

    fun closeTab(id: String) {
        if (tabs.size > 1) {
            val index = tabs.indexOfFirst { it.id == id }
            val newTabs = tabs.filter { it.id != id }
            if (activeTabId == id) {
                activeTabId = newTabs[if (index > 0) index - 1 else 0].id
            }
            tabs = newTabs
            webViewStates.remove(id)
            webViewNavigators.remove(id)
            initializedTabs.remove(id)
        }
    }

    val isAnyOverlayOpen = showTabSwitcher || showBookmarks || showHistory || showSettings || drawerState.isOpen

    ModalNavigationDrawer(
        drawerState = drawerState,
        gesturesEnabled = !showTabSwitcher && !showBookmarks && !showHistory && !showSettings,
        drawerContent = {
            ModalDrawerSheet {
                Spacer(Modifier.height(12.dp))
                Text(stringResource(Res.string.browser_menu), modifier = Modifier.padding(16.dp), style = MaterialTheme.typography.titleMedium)
                NavigationDrawerItem(
                    label = { Text(stringResource(Res.string.new_tab)) },
                    selected = false,
                    onClick = { addNewTab() },
                    icon = { Icon(Icons.Default.Add, null) }
                )
                NavigationDrawerItem(
                    label = { Text(stringResource(Res.string.refresh)) },
                    selected = false,
                    onClick = {
                        currentNavigator.reload()
                        scope.launch { drawerState.close() }
                    },
                    icon = { Icon(Icons.Default.Refresh, null) }
                )
                NavigationDrawerItem(
                    label = { Text(stringResource(Res.string.bookmarks)) },
                    selected = false,
                    onClick = {
                        showBookmarks = true
                        scope.launch { drawerState.close() }
                    },
                    icon = { Icon(Icons.Default.BookmarkBorder, null) }
                )
                NavigationDrawerItem(
                    label = { Text(stringResource(Res.string.history)) },
                    selected = false,
                    onClick = {
                        showHistory = true
                        scope.launch { drawerState.close() }
                    },
                    icon = { Icon(Icons.Default.History, null) }
                )
                NavigationDrawerItem(
                    label = { Text(stringResource(Res.string.settings)) },
                    selected = false,
                    onClick = {
                        showSettings = true
                        scope.launch { drawerState.close() }
                    },
                    icon = { Icon(Icons.Default.Settings, null) }
                )
            }
        }
    ) {
        BoxWithConstraints(modifier = Modifier.fillMaxSize()) {
            val isWide = maxWidth > 600.dp

            Scaffold(
                modifier = Modifier.windowInsetsPadding(WindowInsets.systemBars),
                topBar = {
                    Surface(modifier = Modifier.zIndex(10f), shadowElevation = 4.dp) {
                        Column {
                            BrowserTopBar(
                                isWide = isWide,
                                activeTab = activeTab,
                                tabs = tabs,
                                showMenu = false,
                                onMenuToggle = { scope.launch { drawerState.open() } },
                                canGoBack = currentNavigator.canGoBack,
                                canGoForward = currentNavigator.canGoForward,
                                onUrlSubmit = { url -> navigateTab(activeTabId, url) },
                                onTabClick = { showTabSwitcher = true },
                                onNewTab = { addNewTab() },
                                onRefresh = { currentNavigator.reload() },
                                onBack = { currentNavigator.navigateBack() },
                                onForward = { currentNavigator.navigateForward() },
                                onTabSelect = { id -> activeTabId = id },
                                onTabClose = { id -> closeTab(id) }
                            )

                            if (activeTab.isLoading) {
                                LinearProgressIndicator(
                                    progress = { activeTab.progress },
                                    modifier = Modifier.fillMaxWidth().height(3.dp),
                                    color = MaterialTheme.colorScheme.primary,
                                    trackColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)
                                )
                            } else {
                                Box(Modifier.fillMaxWidth().height(3.dp).background(Color.Transparent))
                            }
                        }
                    }
                },
                bottomBar = {
                    if (!isWide) {
                        Surface(modifier = Modifier.zIndex(10f), shadowElevation = 8.dp) {
                            BrowserBottomBar(
                                canGoBack = currentNavigator.canGoBack,
                                canGoForward = currentNavigator.canGoForward,
                                onBack = { currentNavigator.navigateBack() },
                                onForward = { currentNavigator.navigateForward() },
                                onHome = { navigateTab(activeTabId, "https://www.google.com") },
                                onBookmarks = { showBookmarks = true },
                                onHistory = { showHistory = true }
                            )
                        }
                    }
                }
            ) { padding ->
                Box(modifier = Modifier.padding(padding).fillMaxSize()) {
                    Box(modifier = Modifier.fillMaxSize().background(MaterialTheme.colorScheme.background))

                    // WebView rendering loop: maintains multiple WebViews in memory for tab switching
                    tabs.forEach { tab ->
                        key(tab.id) {
                            if (initializedTabs.contains(tab.id)) {
                                val state = webViewStates.getOrPut(tab.id) { WebViewState(WebContent.Url(tab.url)) }
                                val navigator = webViewNavigators.getOrPut(tab.id) { WebViewNavigator(scope) }

                                // 1. Sync WebView -> Metadata (Updates URL, Title, Progress, and Loading State)
                                LaunchedEffect(state.loadingState, state.lastLoadedUrl, state.pageTitle) {
                                    val loadingStatus = state.loadingState
                                    val isFinished = loadingStatus is LoadingState.Finished
                                    val webUrl = state.lastLoadedUrl
                                    
                                    tabs = tabs.map { t ->
                                        if (t.id == tab.id) {
                                            // Sync the URL back if a real navigation happened.
                                            // This handles redirects (e.g. fb.com -> facebook.com)
                                            val finalUrl = if (webUrl != null && webUrl != t.url && (isFinished || webUrl.length > t.url.length)) {
                                                webUrl
                                            } else {
                                                t.url
                                            }

                                            t.copy(
                                                url = finalUrl,
                                                title = state.pageTitle ?: t.title,
                                                isLoading = !isFinished,
                                                progress = (loadingStatus as? LoadingState.Loading)?.progress ?: 0f
                                            )
                                        } else t
                                    }
                                }

                                val isActive = tab.id == activeTabId
                                val isTrulyVisible = isActive && !isAnyOverlayOpen

                                Box(
                                    modifier = Modifier
                                        .fillMaxSize()
                                        .zIndex(if (isActive) 1f else 0f)
                                        .alpha(if (isTrulyVisible) 1f else 0f)
                                        .offset(x = if (isTrulyVisible) 0.dp else 20000.dp)
                                ) {
                                    // Using state.content as part of the key forces a fresh WebView when content intent changes
                                    key(tab.id, state.content) {
                                        WebView(
                                            state = state,
                                            navigator = navigator,
                                            modifier = Modifier.fillMaxSize()
                                        )
                                    }
                                }
                            }
                        }
                    }

                    if (showTabSwitcher) {
                        Box(modifier = Modifier.fillMaxSize().zIndex(200f)) {
                            TabSwitcher(
                                tabs = tabs,
                                activeTabId = activeTabId,
                                onTabSelect = { id ->
                                    activeTabId = id
                                    showTabSwitcher = false
                                },
                                onTabClose = { id -> closeTab(id) },
                                onNewTab = { addNewTab() },
                                onClose = { showTabSwitcher = false }
                            )
                        }
                    }

                    if (showBookmarks) {
                        Box(modifier = Modifier.fillMaxSize().zIndex(200f)) {
                            OverlayScreen(title = stringResource(Res.string.bookmarks), onClose = { showBookmarks = false }) {
                                Text(stringResource(Res.string.no_bookmarks), modifier = Modifier.padding(16.dp))
                            }
                        }
                    }

                    if (showHistory) {
                        Box(modifier = Modifier.fillMaxSize().zIndex(200f)) {
                            OverlayScreen(title = stringResource(Res.string.history), onClose = { showHistory = false }) {
                                Text(stringResource(Res.string.no_history), modifier = Modifier.padding(16.dp))
                            }
                        }
                    }

                    if (showSettings) {
                        Box(modifier = Modifier.fillMaxSize().zIndex(200f)) {
                            OverlayScreen(title = stringResource(Res.string.settings), onClose = { showSettings = false }) {
                                SettingsScreen(themeSettings)
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun SettingsScreen(themeSettings: ThemeSettings) {
    val selectedTheme by themeSettings.selectedTheme.collectAsState(AppTheme.SYSTEM)
    val selectedLanguage by themeSettings.selectedLanguage.collectAsState(AppLanguage.ENGLISH)
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
                verticalAlignment = androidx.compose.ui.Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp)
                    .background(
                        if (selectedTheme == theme) MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.3f)
                        else Color.Transparent,
                        RoundedCornerShape(8.dp)
                    )
                    .clickable {
                        scope.launch { themeSettings.setTheme(theme) }
                    }
                    .padding(horizontal = 12.dp)
            ) {
                RadioButton(
                    selected = selectedTheme == theme,
                    onClick = { scope.launch { themeSettings.setTheme(theme) } }
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
                verticalAlignment = androidx.compose.ui.Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp)
                    .background(
                        if (selectedLanguage == language) MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.3f)
                        else Color.Transparent,
                        RoundedCornerShape(8.dp)
                    )
                    .clickable {
                        scope.launch { themeSettings.setLanguage(language) }
                    }
                    .padding(horizontal = 12.dp)
            ) {
                RadioButton(
                    selected = selectedLanguage == language,
                    onClick = { scope.launch { themeSettings.setLanguage(language) } }
                )
                Spacer(Modifier.width(8.dp))
                Text(
                    text = "${language.flag} ${language.displayName}",
                    style = MaterialTheme.typography.bodyLarge
                )
            }
            Spacer(Modifier.height(4.dp))
        }
    }
}
