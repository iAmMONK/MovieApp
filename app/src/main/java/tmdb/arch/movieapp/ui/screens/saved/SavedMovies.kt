package tmdb.arch.movieapp.ui.screens.saved

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf
import tmdb.arch.movieapp.R
import tmdb.arch.movieapp.compose.utils.compose

class SavedMovies : Fragment(R.layout.movies_saved) {

    private val args by navArgs<SavedMoviesArgs>()
    private val viewModel by viewModel<SavedMoviesViewModel> {
        parametersOf(args.command)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ) = compose {
        SavedMoviesScreen(
            viewModel = viewModel,
            onMovieClicked = {
                findNavController().navigate(SavedMoviesDirections.savedMoviesToMovieDetails(it))
            }
        )
    }
}
