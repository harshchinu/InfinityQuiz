package com.alpha.infinityquiz.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.alpha.infinityquiz.ui.home.HomeScreen
import com.alpha.infinityquiz.ui.test.TestScreen

@Composable
fun QuizNavGraph(navController: NavHostController) {
    NavHost(navController = navController, startDestination = NavigationRoutes.HOME) {
        composable(NavigationRoutes.HOME) {
            HomeScreen(
                onStartTest = { navController.navigate(NavigationRoutes.TEST) },
                onSolveBookmarks = { navController.navigate("${NavigationRoutes.TEST}/true") }
            )
        }

        composable(NavigationRoutes.TEST) {
            TestScreen(
                onFinish = { navController.popBackStack() }
            )
        }
        composable(route = NavigationRoutes.TEST_BOOKMARK, arguments = listOf(navArgument("bookmarked") {type=NavType.BoolType})) {
            TestScreen(
                onFinish = { navController.popBackStack() }
            )
        }
    }
}

object NavigationRoutes {
    const val HOME = "home"
    const val TEST = "test"
    const val TEST_BOOKMARK = "test/{bookmarked}"
}
