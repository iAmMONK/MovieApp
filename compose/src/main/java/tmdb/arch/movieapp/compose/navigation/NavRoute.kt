package tmdb.arch.movieapp.compose.navigation

import androidx.navigation.NavController
import androidx.navigation.NavOptions
import com.google.gson.GsonBuilder

interface NavRoute {
    val route: String
}

abstract class NavRouteWithoutArgs(private val path: String) : NavRoute {
    override val route: String
        get() = path

    fun navigate(
        navController: NavController,
        navOptions: NavOptions? = null
    ) {
        navController.navigate(route, navOptions = navOptions)
    }
}

abstract class NavRouteWithArgs<T>(private val path: String) : NavRoute {

    val argName = "arg"

    override val route: String
        get() = "$path?$argName={$argName}"

    fun navigate(
        navController: NavController,
        arg: T,
        navOptions: NavOptions? = null
    ) {
        if (arg == null) return
        val route = "$path?$argName=${arg.toJson()}"
        navController.navigate(route, navOptions = navOptions)
    }
}

private fun Any.toJson(): String = GsonBuilder().create().toJson(this)
