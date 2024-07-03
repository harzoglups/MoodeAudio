package com.moode.android.ui

import android.annotation.SuppressLint
import androidx.annotation.StringRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults.topAppBarColors
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.moode.android.R
import com.moode.android.viewmodel.SettingsViewModel

enum class MoodroidScreens(@StringRes val title: Int) {
    MAIN(R.string.main_view_title),
    SETTINGS(R.string.settings_views_title)
}

@SuppressLint("SetJavaScriptEnabled")
@Composable
fun MainScreen(
    navController: NavHostController = rememberNavController(),
    settingsViewModel: SettingsViewModel
) {
    // Get current back stack entry
    val backStackEntry by navController.currentBackStackEntryAsState()
    // Get the name of the current screen
    val currentScreen = MoodroidScreens.valueOf(
        backStackEntry?.destination?.route ?: MoodroidScreens.MAIN.name
    )
    Scaffold(
        topBar = {
            MoodroidTopBar(
                currentScreen = currentScreen,
                canNavigateBack = navController.previousBackStackEntry != null,
                navigateUp = { navController.navigateUp() },
                action = { navController.navigate(MoodroidScreens.SETTINGS.name) }
            )
        }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = MoodroidScreens.MAIN.name,
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            composable(MoodroidScreens.MAIN.name) {
                WebViewContent(settingsViewModel = settingsViewModel)
            }
            composable(MoodroidScreens.SETTINGS.name) {
                PreferenceScreen(settingsViewModel = settingsViewModel)
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MoodroidTopBar(
    currentScreen: MoodroidScreens,
    canNavigateBack: Boolean,
    navigateUp: () -> Unit,
    action: () -> Unit
) {
    TopAppBar(
        colors = topAppBarColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer,
            titleContentColor = MaterialTheme.colorScheme.onPrimaryContainer,
        ),
        title = {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Image(
                    painter = painterResource(id = R.drawable.img),
                    contentDescription = "Moodroid",
                    modifier = Modifier.size(32.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(stringResource(currentScreen.title))
            }
        },
        actions = {
            if (currentScreen == MoodroidScreens.MAIN) {
                IconButton(
                    onClick = {
                        action()
                    },
                ) {
                    Icon(Icons.Filled.Settings, contentDescription = "Settings")
                }
            }
        },
        navigationIcon = {
            if (canNavigateBack) {
                IconButton(onClick = navigateUp) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = stringResource(R.string.back_button)
                    )
                }
            }
        }
    )
}