package tmdb.arch.movieapp.ui.screens.discover

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import tmdb.arch.movieapp.R
import tmdb.arch.movieapp.databinding.MoviesDiscoverBinding
import tmdb.arch.movieapp.utils.delegates.viewBinding

class DiscoverMovies : Fragment(R.layout.movies_discover) {

    private val binding: MoviesDiscoverBinding by viewBinding(MoviesDiscoverBinding::bind)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.searchButton.text = "ViewBinding search"
    }
}
