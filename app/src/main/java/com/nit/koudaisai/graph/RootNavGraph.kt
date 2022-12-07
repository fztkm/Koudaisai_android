package com.nit.koudaisai.graph

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navDeepLink
import com.nit.koudaisai.common.DeepLink
import com.nit.koudaisai.presentation.convert_to_admin.AdminSplashScreen
import com.nit.koudaisai.presentation.convert_to_admin.auth_menu.AuthMenuScreen
import com.nit.koudaisai.presentation.convert_to_admin.signin.AdminSignInScreen
import com.nit.koudaisai.presentation.convert_to_admin.signup.AdminSignUpScreen
import com.nit.koudaisai.presentation.splash.SplashScreen

@Composable
fun RootNavGraph(navController: NavHostController, innerPadding: PaddingValues) {
    NavHost(
        modifier = Modifier.padding(innerPadding),
        navController = navController,
        route = Graph.ROOT,
        startDestination = Graph.SPLASH
    ) {
        composable(
            route = Graph.SPLASH,
        ) {
            SplashScreen(
                openAndPopUpHome = {
                    navController.clearAndNavigate(Graph.HOME)
                }
            )
        }
        this.authNavGraph(navController)
        this.homeNavGraph(navController)
        composable(
            route = Graph.ADMIN,
            deepLinks = listOf(navDeepLink { uriPattern = "${DeepLink.HOST}/${DeepLink.ADMIN}" })
        ) {
            AdminSplashScreen(
                openAndPopUpHome = { navController.navigateSingleTopTo(Graph.HOME) },
                openAndPopUpMenu = { navController.navigateSingleTopTo(AdminRoot.menu) }
            )
        }
        composable(
            route = AdminRoot.menu
        ) {
            AuthMenuScreen(onClickCreateAccount = { navController.navigate(AdminRoot.signUp) },
                onClickLogin = { navController.navigate(AdminRoot.signIn) })
        }
        composable(
            route = AdminRoot.signUp
        ) {
            AdminSignUpScreen(
                onBackClicked = { navController.popBackStack() },
                onSuccess = { navController.clearAndNavigate(Graph.HOME) },
                onFail = {}
            )
        }
        composable(
            route = AdminRoot.signIn
        ) {
            AdminSignInScreen(onBackPressed = { navController.popBackStack() },
                popUpToHome = { navController.clearAndNavigate(Graph.HOME) })

        }
    }
}

object Graph {
    const val ROOT = "root_graph"
    const val SPLASH = "splash"
    const val HOME = "home_graph"
    const val AUTHENTICATE = "auth_graph"
    const val ADMIN = "admin_graph"
}

object AdminRoot {
    const val menu = "admin_menu"
    const val signIn = "admin_sign_in"
    const val signUp = "admin_sign_up"
}

fun NavHostController.navigateSingleTopTo(route: String) =
    this.navigate(route) {
        popUpTo(
            this@navigateSingleTopTo.graph.findStartDestination().id
        ) {
            saveState = true
        }
        launchSingleTop = true
        restoreState = true
    }

fun NavHostController.navigateAndPopUpInclusive(route: String, popUp: String) =
    this.navigate(route) {
        launchSingleTop = true
        popUpTo(popUp) { inclusive = true }
    }

fun NavHostController.navigateAndPopUp(route: String, popUp: String) =
    this.navigate(route) {
        launchSingleTop = true
        popUpTo(popUp) { inclusive = false }
    }


fun NavHostController.clearAndNavigate(route: String) =
    this.navigate(route) {
        launchSingleTop = true
        popUpTo(Graph.ROOT) { inclusive = true }
    }

