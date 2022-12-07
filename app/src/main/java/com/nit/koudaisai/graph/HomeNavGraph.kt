package com.nit.koudaisai.graph

import androidx.compose.runtime.remember
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import com.nit.koudaisai.local_database.EventDatabase
import com.nit.koudaisai.presentation.home.screen.event_detail.EventDetailScreen
import com.nit.koudaisai.presentation.home.screen.event_detail.EventLocationScreen
import com.nit.koudaisai.presentation.home.screen.info.InfoScreen
import com.nit.koudaisai.presentation.home.screen.info.InfoViewModel
import com.nit.koudaisai.presentation.home.screen.map_view.MapViewScreen
import com.nit.koudaisai.presentation.home.screen.overview.OverviewScreen
import com.nit.koudaisai.presentation.home.screen.schedule.ScheduleScreen
import com.nit.koudaisai.presentation.home.screen.settings.SettingsScreen
import com.nit.koudaisai.presentation.reserve.resereve_screen.ReserveInputUserDataScreen
import com.nit.koudaisai.presentation.reserve.resereve_screen.ReserveViewModel
import com.nit.koudaisai.presentation.reserve.resereve_screen.TermsScreen
import com.nit.koudaisai.presentation.reserve.resereve_screen.reserve_companion.ReserveCompanionScreen
import com.nit.koudaisai.presentation.reserve.resereve_screen.reserve_companion.ReserveCompanionViewModel
import com.nit.koudaisai.presentation.signIn.SignInScreen

fun NavGraphBuilder.homeNavGraph(navController: NavHostController) {
    navigation(
        route = Graph.HOME,
        startDestination = Info.route,
    ) {
        composable(route = Info.route) { backStackEntry ->
            val parentEntry = remember(backStackEntry) {
                navController.getBackStackEntry(Graph.HOME)
            }
            val parentViewModel = hiltViewModel<InfoViewModel>(parentEntry)
            InfoScreen(
                onClickReserve = {
                    navController.navigateAndPopUp(
                        HomeNavGraph.RESERVE,
                        Info.route
                    )
                },
                onClickLogin = { navController.navigateAndPopUp(HomeNavGraph.LOGIN, Info.route) },
                onClickAddCompanion = {
                    navController.navigateAndPopUp(
                        HomeNavGraph.REGISTER_COMPANION,
                        Info.route
                    )
                },
                onReload = {
                    navController.navigateAndPopUpInclusive(
                        Graph.HOME,
                        Graph.HOME
                    )
                },
                onClickAdmin = {
                    navController.navigate(AdmissionGraph.root)
                },
                viewModel = parentViewModel
            )
        }
        reserveNavGraph(navController)
        loginNavGraph(navController)
        reserveCompanionNavGraph(navController)
        admissionNavGraph(navController)
//        composable(route = Overview.route) {
//            OverviewScreen()
//        }
        composable(route = Mapview.route) {
            //UndoneScreen()
            MapViewScreen(
                onClickDetail = { eventName ->
                    navController.navigate("$EVENT_DETAIL/$eventName")
                }
            )
        }
        composable(route = Schedule.route) {
            ScheduleScreen()
            //UndoneScreen()
        }
        composable(route = Events.route) {
//            UndoneScreen()
            OverviewScreen(
                onClickEvent = { eventName ->
                    navController.navigate("$EVENT_DETAIL/$eventName")
                }
            )
        }
        composable(route = "$EVENT_DETAIL/{eventName}") { backStack ->
            val event =
                EventDatabase.generalEventList.find {
                    it.name == (backStack.arguments?.getString("eventName") ?: "")
                } ?: EventDatabase.specialEventList.find {
                    it.name == (backStack.arguments?.getString("eventName") ?: "")
                }
            EventDetailScreen(event = event,
                onClickLocation = { navController.navigate("$EVENT_LOCATION/${event?.name}") },
                onBackPressed = { navController.popBackStack() })
        }
        composable(route = "$EVENT_LOCATION/{eventName}") { backStack ->
            val event =
                EventDatabase.generalEventList.find {
                    it.name == (backStack.arguments?.getString("eventName") ?: "")
                } ?: EventDatabase.specialEventList.find {
                    it.name == (backStack.arguments?.getString("eventName") ?: "")
                }
            EventLocationScreen(event = event,
                onBackPressed = { navController.popBackStack() })
        }
        composable(route = Settings.route) {
            SettingsScreen(popUpToSplash = {
                navController.navigateAndPopUp(
                    Graph.SPLASH,
                    Graph.ROOT
                )
            })
        }
        composable(route = CAMERA_ROUTE) {
            //QRCameraScreen()
        }
    }
}

fun NavGraphBuilder.reserveNavGraph(navController: NavHostController) {
    navigation(
        route = HomeNavGraph.RESERVE,
        startDestination = ReserveNavGraph.RESERVE,
    ) {
        composable(route = ReserveNavGraph.RESERVE) { backStackEntry ->
            val parentEntry = remember(backStackEntry) {
                navController.getBackStackEntry(HomeNavGraph.RESERVE)
            }
            val parentViewModel = hiltViewModel<ReserveViewModel>(parentEntry)
            ReserveInputUserDataScreen(
                onClickNext = {
                    navController.navigateAndPopUp(
                        ReserveNavGraph.TERM,
                        ReserveNavGraph.RESERVE
                    )
                },
                onBackClicked = { navController.popBackStack() },
                viewModel = parentViewModel
            )
        }
        composable(route = ReserveNavGraph.TERM) { backStackEntry ->
            val parentEntry = remember(backStackEntry) {
                navController.getBackStackEntry(HomeNavGraph.RESERVE)
            }
            val parentViewModel = hiltViewModel<ReserveViewModel>(parentEntry)
            TermsScreen(
                onReserveSuccess = { navController.clearAndNavigate(Graph.HOME) },
                onReserveFail = {},
                onBackClicked = { navController.popBackStack() },
                viewModel = parentViewModel,
                onDisAgree = { navController.popBackStack(HomeNavGraph.RESERVE, true) }
            )
        }
    }
}

fun NavGraphBuilder.loginNavGraph(navController: NavHostController) {
    navigation(
        route = HomeNavGraph.LOGIN,
        startDestination = LoginNavGraph.LOGIN,
    ) {
        composable(route = LoginNavGraph.LOGIN) {
            SignInScreen(onBackPressed = { navController.popBackStack() },
                popUpToHome = { navController.clearAndNavigate(Graph.HOME) })
        }
    }
}

fun NavGraphBuilder.reserveCompanionNavGraph(navController: NavHostController) {
    navigation(
        route = HomeNavGraph.REGISTER_COMPANION,
        startDestination = CompanionNavGraph.RESERVE,
    ) {
        composable(route = CompanionNavGraph.RESERVE) { backStackEntry ->
            val parentEntry = remember(backStackEntry) {
                navController.getBackStackEntry(HomeNavGraph.REGISTER_COMPANION)
            }
            val parentViewModel = hiltViewModel<ReserveCompanionViewModel>(parentEntry)
            ReserveCompanionScreen(
                onSuccess = { navController.clearAndNavigate(Graph.HOME) },
                onFail = {},
                onBackClicked = { navController.popBackStack() },
                viewModel = parentViewModel
            )
        }
    }
}

object HomeNavGraph {
    const val RESERVE = "new_reserve"
    const val LOGIN = "login_user"
    const val REGISTER_COMPANION = "add_companion"
}

object ReserveNavGraph {
    const val RESERVE = "reserve_navgraph_reserve"
    const val TERM = "reserve_navgraph_term"
}

object LoginNavGraph {
    const val LOGIN = "login_navgraph_login"
}

object CompanionNavGraph {
    const val RESERVE = "companion_navgraph_reserve"
}

const val CAMERA_ROUTE = "camera"
const val EVENT_DETAIL = "event_detail"
const val EVENT_LOCATION = "event_location"