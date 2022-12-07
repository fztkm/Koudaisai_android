package com.nit.koudaisai.graph

import android.util.Log
import androidx.compose.runtime.remember
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.nit.koudaisai.presentation.reserve.resereve_screen.ReserveInputUserDataScreen
import com.nit.koudaisai.presentation.reserve.resereve_screen.ReserveViewModel
import com.nit.koudaisai.presentation.signIn.SignInScreen

fun NavGraphBuilder.authNavGraph(navController: NavHostController) {
    navigation(
        route = Graph.AUTHENTICATE,
        startDestination = ReserveMenu.route,
    ) {
        /**サインイン画面*/
        composable(
            route = SignIn.route,
        ) {
            SignInScreen(
                onBackPressed = {
                    navController.navigateSingleTopTo(ReserveMenu.route)
                },
                popUpToHome = {
                    Log.i("Login", "${Firebase.auth.currentUser?.uid}")
                    //TODO 仮
                    navController.navigateSingleTopTo(Provisional.route)
                    //navController.navigateSingleTopTo(Graph.HOME)
                }
            )
        }
        /**予約情報入力画面*/
        composable(route = ReserveInputUserData.route) { backStackEntry ->
            val parentEntry = remember(backStackEntry) {
                navController.getBackStackEntry("${Graph.AUTHENTICATE}?id={id}")
            }
            val parentViewModel = hiltViewModel<ReserveViewModel>(parentEntry)
            ReserveInputUserDataScreen(
                onClickNext = {
                    navController.navigateSingleTopTo(ReserveConfirm.route)
                },
                onBackClicked = {
                    navController.popBackStack()
                },
                viewModel = parentViewModel
            )
        }
    }
}
