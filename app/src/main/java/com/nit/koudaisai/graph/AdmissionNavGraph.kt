package com.nit.koudaisai.graph

import androidx.compose.runtime.remember
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import com.nit.koudaisai.presentation.admn.AdmissionHomeScreen
import com.nit.koudaisai.presentation.admn.AdmissionResultScreen
import com.nit.koudaisai.presentation.admn.AdmissionViewModel
import com.nit.koudaisai.presentation.admn.QRCameraScreen

fun NavGraphBuilder.admissionNavGraph(navController: NavHostController) {
    navigation(
        route = AdmissionGraph.root,
        startDestination = AdmissionGraph.menu
    ){
        composable(route = AdmissionGraph.menu){
            AdmissionHomeScreen(popUpToCamera = {navController.navigate(AdmissionGraph.qrScanner)},
                popBack = {navController.popBackStack()})
        }
        composable(route = AdmissionGraph.qrScanner) { backStackEntry ->
            val parentEntry = remember(backStackEntry) {
                navController.getBackStackEntry(AdmissionGraph.root)
            }
            val parentViewModel = hiltViewModel<AdmissionViewModel>(parentEntry)
            QRCameraScreen(viewModel = parentViewModel,
                popUpTo = {navController.navigate(AdmissionGraph.result)},
                popBack = {navController.popBackStack()}
            )
        }
        composable(route = AdmissionGraph.result) { backStackEntry ->
            val parentEntry = remember(backStackEntry) {
                navController.getBackStackEntry(AdmissionGraph.root)
            }
            val parentViewModel = hiltViewModel<AdmissionViewModel>(parentEntry)
            AdmissionResultScreen(viewModel = parentViewModel,
                popUpToHome = { navController.navigateAndPopUp(
                    AdmissionGraph.menu, AdmissionGraph.root
                ) },
                popBack = {navController.popBackStack()}
            )
        }
    }
}

object AdmissionGraph{
    const val root = "admin_entry"
    const val menu = "admin_entry_menu"
    const val qrScanner = "admin_entry_qr_scanner"
    const val result = "admin_entry_result"
}