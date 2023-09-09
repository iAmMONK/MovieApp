package tmdb.arch.movieapp.compose.navigation

import androidx.compose.animation.AnimatedContentScope
import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.runtime.Composable
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavDeepLink
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

fun NavGraphBuilder.screenWithoutArgs(
    navRoute: NavRouteWithoutArgs,
    deepLinks: List<NavDeepLink> = emptyList(),
    enterTransition: (
        @JvmSuppressWildcards
        AnimatedContentTransitionScope<NavBackStackEntry>.() -> EnterTransition?
    )? = null,
    exitTransition: (
        @JvmSuppressWildcards
        AnimatedContentTransitionScope<NavBackStackEntry>.() -> ExitTransition?
    )? = null,
    popEnterTransition: (
        @JvmSuppressWildcards
        AnimatedContentTransitionScope<NavBackStackEntry>.() -> EnterTransition?
    )? =
        enterTransition,
    popExitTransition: (
        @JvmSuppressWildcards
        AnimatedContentTransitionScope<NavBackStackEntry>.() -> ExitTransition?
    )? =
        exitTransition,
    content: @Composable AnimatedContentScope.(NavBackStackEntry) -> Unit
) {
    composable(
        route = navRoute.route,
        deepLinks = deepLinks,
        enterTransition = enterTransition,
        popEnterTransition = popEnterTransition,
        exitTransition = exitTransition,
        popExitTransition = popExitTransition,
        content = content
    )
}

fun <T : Any> NavGraphBuilder.screenWithArgs(
    navRoute: NavRouteWithArgs<T>,
    deepLinks: List<NavDeepLink> = emptyList(),
    enterTransition: (
        @JvmSuppressWildcards
        AnimatedContentTransitionScope<NavBackStackEntry>.() -> EnterTransition?
    )? = null,
    exitTransition: (
        @JvmSuppressWildcards
        AnimatedContentTransitionScope<NavBackStackEntry>.() -> ExitTransition?
    )? = null,
    popEnterTransition: (
        @JvmSuppressWildcards
        AnimatedContentTransitionScope<NavBackStackEntry>.() -> EnterTransition?
    )? =
        enterTransition,
    popExitTransition: (
        @JvmSuppressWildcards
        AnimatedContentTransitionScope<NavBackStackEntry>.() -> ExitTransition?
    )? =
        exitTransition,
    content: @Composable AnimatedContentScope.(T, NavBackStackEntry) -> Unit
) {
    composable(
        route = navRoute.route,
        arguments = listOf(navArgument(navRoute.argName) { this.type = NavType.StringType }),
        deepLinks = deepLinks,
        enterTransition = enterTransition,
        popEnterTransition = popEnterTransition,
        exitTransition = exitTransition,
        popExitTransition = popExitTransition,
        content = { backStackEntry ->
            val arg =
                Gson().fromJson<T>(string = backStackEntry.arguments?.getString(navRoute.argName)!!)
            content(arg, backStackEntry)
        }
    )
}

private fun <T> Gson.fromJson(string: String): T = fromJson(
    string,
    object : TypeToken<T>() {}.type
)
