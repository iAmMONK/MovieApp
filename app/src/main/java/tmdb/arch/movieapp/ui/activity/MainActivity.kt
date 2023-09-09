package tmdb.arch.movieapp.ui.activity

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.material3.Surface
import androidx.compose.runtime.CompositionLocalProvider
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import org.koin.androidx.compose.koinViewModel
import org.koin.core.parameter.parametersOf
import tmdb.arch.movieapp.compose.locals.LocalNavController
import tmdb.arch.movieapp.compose.navigation.screenWithArgs
import tmdb.arch.movieapp.compose.navigation.screenWithoutArgs
import tmdb.arch.movieapp.compose.theme.TmdbTheme
import tmdb.arch.movieapp.domain.usecases.GetSavedMoviesUseCase
import tmdb.arch.movieapp.ui.screens.details.MovieDetailsScreen
import tmdb.arch.movieapp.ui.screens.details.MoviesDetailsViewModel
import tmdb.arch.movieapp.ui.screens.discover.DiscoverMoviesScreen
import tmdb.arch.movieapp.ui.screens.discover.DiscoverMoviesViewModel
import tmdb.arch.movieapp.ui.screens.saved.SavedMoviesScreen
import tmdb.arch.movieapp.ui.screens.saved.SavedMoviesViewModel
import tmdb.arch.movieapp.ui.screens.search.SearchMoviesScreen
import tmdb.arch.movieapp.ui.screens.search.SearchMoviesViewModel

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            TmdbTheme {
                val navController = rememberNavController()

                CompositionLocalProvider(
                    LocalNavController provides navController
                ) {
                    Surface {
                        NavHost(
                            navController = navController,
                            startDestination = DiscoverMoviesScreen.route
                        ) {
                            screenWithoutArgs(DiscoverMoviesScreen) {
                                val viewModel = koinViewModel<DiscoverMoviesViewModel>()
                                DiscoverMoviesScreen(viewModel = viewModel)
                            }

                            screenWithoutArgs(SearchMoviesScreen) {
                                val viewModel = koinViewModel<SearchMoviesViewModel>()
                                SearchMoviesScreen(viewModel = viewModel)
                            }

                            screenWithArgs(SavedMoviesScreen) { arg, _ ->
                                val viewModel = koinViewModel<SavedMoviesViewModel> {
                                    parametersOf(GetSavedMoviesUseCase.Cmd.valueOf(arg))
                                }
                                SavedMoviesScreen(viewModel = viewModel)
                            }

                            screenWithArgs(MovieDetailsScreen) { arg, _ ->
                                val viewModel = koinViewModel<MoviesDetailsViewModel> {
                                    parametersOf(arg)
                                }
                                MovieDetailsScreen(viewModel = viewModel)
                            }
                        }
                    }
                }
            }
        }
    }
}
