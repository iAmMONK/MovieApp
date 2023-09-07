package tmdb.arch.movieapp.ui.screens.discover

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ConcatAdapter
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel
import tmdb.arch.movieapp.R
import tmdb.arch.movieapp.databinding.MoviesDiscoverBinding
import tmdb.arch.movieapp.domain.usecases.GetSavedMoviesUseCase
import tmdb.arch.movieapp.ui.screens.discover.adapters.HeaderListAdapter
import tmdb.arch.movieapp.ui.screens.discover.adapters.MoviesListAdapter
import tmdb.arch.movieapp.ui.screens.discover.adapters.MoviesListStateAdapter
import tmdb.arch.movieapp.utils.delegates.autoNull
import tmdb.arch.movieapp.utils.delegates.viewBinding
import tmdb.arch.movieapp.utils.extensions.collectRepeatOnStart

class DiscoverMovies : Fragment(R.layout.movies_discover) {

    private val binding by viewBinding(MoviesDiscoverBinding::bind)
    private val viewModel by viewModel<DiscoverMoviesViewModel>()
    private val headerAdapter by autoNull {
        HeaderListAdapter(
            onFavoritesClicked = {
                findNavController().navigate(
                    DiscoverMoviesDirections.discoverMoviesToSavedMovies(GetSavedMoviesUseCase.Cmd.FAVORITES)
                )
            },
            onToWatchClicked = {
                findNavController().navigate(
                    DiscoverMoviesDirections.discoverMoviesToSavedMovies(GetSavedMoviesUseCase.Cmd.TO_WATCH)
                )
            }
        )
    }
    private val listAdapter by autoNull {
        MoviesListAdapter {
            findNavController().navigate(DiscoverMoviesDirections.discoverMoviesToMovieDetails(it))
        }
    }
    private val listRefreshStateAdapter by autoNull {
        MoviesListStateAdapter { listAdapter.retry() }
    }
    private val listStateAdapter by autoNull {
        MoviesListStateAdapter { listAdapter.retry() }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initView()
        subscribeUi()
    }

    private fun initView() {
        binding.searchButton.setOnClickListener {
            findNavController().navigate(DiscoverMoviesDirections.discoverMoviesToSearchMovies())
        }

        binding.listView.adapter = ConcatAdapter(
            headerAdapter,
            listRefreshStateAdapter,
            listAdapter,
            listStateAdapter
        )
    }

    private fun subscribeUi() {
        viewModel.movies.collectRepeatOnStart(viewLifecycleOwner) { pagingData ->
            viewLifecycleOwner.lifecycleScope.launch {
                listAdapter.submitData(pagingData)
            }
        }

        listAdapter.loadStateFlow.collectRepeatOnStart(viewLifecycleOwner) { combinedLoadStates ->
            listRefreshStateAdapter.loadState = combinedLoadStates.refresh
            listStateAdapter.loadState = combinedLoadStates.append
        }
    }
}
