package com.abdellatif.clipsave.ui.navigation

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ContentPaste
import androidx.compose.material.icons.filled.Download
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.abdellatif.clipsave.ui.AppViewModel
import com.abdellatif.clipsave.ui.downloads.DownloadsScreen
import com.abdellatif.clipsave.ui.home.HomeScreen
import com.abdellatif.clipsave.ui.paste.PasteUrlScreen
import com.abdellatif.clipsave.ui.settings.SettingsScreen

private enum class Tab(val route: String, val label: String, val icon: ImageVector) {
    HOME("home", "Home", Icons.Filled.Home),
    DOWNLOADS("downloads", "Downloads", Icons.Filled.Download),
    PASTE("paste", "Paste URL", Icons.Filled.ContentPaste),
    SETTINGS("settings", "Settings", Icons.Filled.Settings)
}

@Composable
fun AppScaffold(vm: AppViewModel) {
    val navController = rememberNavController()
    Scaffold(
        bottomBar = {
            NavigationBar {
                val backStack by navController.currentBackStackEntryAsState()
                val current = backStack?.destination
                Tab.entries.forEach { tab ->
                    val selected = current?.hierarchy?.any { it.route == tab.route } == true
                    NavigationBarItem(
                        selected = selected,
                        onClick = {
                            navController.navigate(tab.route) {
                                popUpTo(navController.graph.findStartDestination().id) { saveState = true }
                                launchSingleTop = true
                                restoreState = true
                            }
                        },
                        icon = { Icon(tab.icon, contentDescription = tab.label) },
                        label = { Text(tab.label) }
                    )
                }
            }
        }
    ) { padding ->
        NavHost(
            navController = navController,
            startDestination = Tab.HOME.route,
            modifier = Modifier.padding(padding)
        ) {
            composable(Tab.HOME.route) {
                HomeScreen(vm, onGoToPaste = { navController.navigate(Tab.PASTE.route) })
            }
            composable(Tab.DOWNLOADS.route) { DownloadsScreen(vm) }
            composable(Tab.PASTE.route) { PasteUrlScreen(vm) }
            composable(Tab.SETTINGS.route) { SettingsScreen(vm) }
        }
    }
}
