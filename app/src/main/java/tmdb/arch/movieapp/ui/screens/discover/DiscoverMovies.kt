package tmdb.arch.movieapp.ui.screens.discover

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.compose.ui.platform.ComposeView
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import org.koin.androidx.viewmodel.ext.android.viewModel
import tmdb.arch.movieapp.compose.theme.TmdbTheme
import tmdb.arch.movieapp.domain.usecases.GetSavedMoviesUseCase

class DiscoverMovies : Fragment() {

    private val viewModel by viewModel<DiscoverMoviesViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ) = ComposeView(requireContext()).apply {
        setContent {
            TmdbTheme {
                DiscoverMoviesScreen(
                    viewModel = viewModel,
                    onMovieClicked = {
                        findNavController().navigate(
                            DiscoverMoviesDirections.discoverMoviesToMovieDetails(
                                it
                            )
                        )
                    },
                    onToWatchClicked = {
                        findNavController()
                            .navigate(
                                DiscoverMoviesDirections.discoverMoviesToSavedMovies(
                                    GetSavedMoviesUseCase.Cmd.TO_WATCH
                                )
                            )
                    },
                    onFavoritesClicked = {
                        findNavController()
                            .navigate(
                                DiscoverMoviesDirections.discoverMoviesToSavedMovies(
                                    GetSavedMoviesUseCase.Cmd.FAVORITES
                                )
                            )
                    },
                    onSearchClicked = {
                        findNavController()
                            .navigate(DiscoverMoviesDirections.discoverMoviesToSearchMovies())
                    }
                )
            }
        }
    }
}
