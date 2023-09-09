package tmdb.arch.movieapp.ui.screens.search

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import org.koin.androidx.viewmodel.ext.android.viewModel
import tmdb.arch.movieapp.R
import tmdb.arch.movieapp.compose.utils.compose

class SearchMovies : Fragment(R.layout.movies_search) {

    private val viewModel by viewModel<SearchMoviesViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ) = compose {
        SearchMoviesScreen(
            viewModel = viewModel,
            onMovieClicked = {
                findNavController().navigate(SearchMoviesDirections.searchMoviesToMovieDetails(it))
            }
        )
    }
}
