package tmdb.arch.movieapp.ui.screens.details

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf
import tmdb.arch.movieapp.R
import tmdb.arch.movieapp.compose.utils.compose

class MovieDetails : Fragment(R.layout.movie_details) {

    private val args by navArgs<MovieDetailsArgs>()
    private val viewModel by viewModel<MoviesDetailsViewModel> { parametersOf(args.id) }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ) = compose {
        MovieDetailsScreen(viewModel = viewModel)
    }
}
