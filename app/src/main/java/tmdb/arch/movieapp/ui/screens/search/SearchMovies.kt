package tmdb.arch.movieapp.ui.screens.search

import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import org.koin.androidx.viewmodel.ext.android.viewModel
import tmdb.arch.movieapp.R
import tmdb.arch.movieapp.databinding.MoviesSearchBinding
import tmdb.arch.movieapp.ui.screens.search.adapters.SearchMoviesAdapter
import com.example.arch.utils.UiState
import com.example.arch.utils.delegates.autoNull
import com.example.arch.utils.delegates.viewBinding
import com.example.arch.utils.extensions.collectRepeatOnStart

class SearchMovies : Fragment(R.layout.movies_search) {

    private val binding by viewBinding(MoviesSearchBinding::bind)
    private val viewModel by viewModel<SearchMoviesViewModel>()
    private val searchMoviesAdapter by autoNull {
        SearchMoviesAdapter {
            findNavController().navigate(SearchMoviesDirections.searchMoviesToMovieDetails(it))
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initViews()
        subscribeUi()
    }

    private fun initViews() {
        binding.resultsList.adapter = searchMoviesAdapter
        binding.searchBar.doOnTextChanged { text, _, _, _ ->
            text?.let(viewModel::onSearchQueryChanged)
        }
    }

    private fun subscribeUi() {
        viewModel.searchResultsState.collectRepeatOnStart(viewLifecycleOwner) { state ->
            if (state is UiState.Result) {
                searchMoviesAdapter.submitList(state.item)
            }
            binding.errorMsg.isVisible = state is UiState.Error
            binding.resultsList.isVisible = state is UiState.Result
        }
    }
}
