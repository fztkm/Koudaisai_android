package com.nit.koudaisai.presentation

import android.content.res.Resources
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import com.nit.koudaisai.common.snackbar.SnackbarManager
import com.nit.koudaisai.graph.*
import com.nit.koudaisai.presentation.home.components.HomeBottomNavigationBar
import com.nit.koudaisai.presentation.home.components.HomeTopAppBar
import com.nit.koudaisai.theme.KoudaisaiTheme
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope

@AndroidEntryPoint
class KoudaisaiActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            KoudaisaiAppFrame()
        }
    }
}

@Composable
fun KoudaisaiAppFrame() {
    KoudaisaiTheme {
        val navController = rememberNavController()
        val systemUiController = rememberSystemUiController()
        val statusBarColor = MaterialTheme.colors.secondaryVariant
        SideEffect {
            systemUiController.setStatusBarColor(statusBarColor)
        }
        val appState = rememberAppState()
        val items = listOf(
            Info,
            Events,
            Schedule,
            Mapview,
            Settings
        )
        val navBackStackEntry by navController.currentBackStackEntryAsState()
        val currentRoute = navBackStackEntry?.destination?.route
        val currentTitle = items.firstOrNull {
            it.route == currentRoute
        }?.title
        val bottomDestination = items.any { it.route == currentRoute }
        val isProvisional = currentRoute == Provisional.route
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colors.background
        ) {
            Scaffold(
                snackbarHost = {
                    SnackbarHost(
                        hostState = it,
                        modifier = Modifier.padding(8.dp),
                        snackbar = { snackbarData ->
                            Snackbar(
                                snackbarData,
                                contentColor = MaterialTheme.colors.onPrimary,
                                backgroundColor = MaterialTheme.colors.primaryVariant
                            )
                        }
                    )
                },
                scaffoldState = appState.scaffoldState,
                topBar = {
                    if (bottomDestination || isProvisional) HomeTopAppBar(currentTitle)
                },
                bottomBar = {
                    if (bottomDestination) HomeBottomNavigationBar(
                        items,
                        currentRoute,
                        onClickIcon = { route ->
                            navController.navigateAndPopUp(route, Graph.HOME)
                        }
                    )
                },
            ) { paddingValues ->
                RootNavGraph(navController = navController, innerPadding = paddingValues)
            }
        }
    }
}

@Composable
fun rememberAppState(
    scaffoldState: ScaffoldState = rememberScaffoldState(),
    navController: NavHostController = rememberNavController(),
    snackbarManager: SnackbarManager = SnackbarManager,
    resources: Resources = resources(),
    coroutineScope: CoroutineScope = rememberCoroutineScope()
) = remember(scaffoldState, navController, snackbarManager, resources, coroutineScope) {
    KoudaisaiAppState(scaffoldState, navController, snackbarManager, resources, coroutineScope)
}

@Composable
@ReadOnlyComposable
fun resources(): Resources {
    LocalConfiguration.current
    return LocalContext.current.resources
}


