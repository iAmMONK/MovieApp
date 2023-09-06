package tmdb.arch.movieapp.ui.screens.details

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import tmdb.arch.movieapp.domain.model.Movie
import tmdb.arch.movieapp.domain.usecases.GetMovieDetailsUseCase
import tmdb.arch.movieapp.utils.UiState

class MoviesDetailsViewModel(
    private val movieId: Long,
    private val getMovieDetailsUseCase: GetMovieDetailsUseCase
) : ViewModel() {

    private val _movie: MutableStateFlow<UiState<Movie>> = MutableStateFlow(UiState.Loading)

    val movie: StateFlow<UiState<Movie>> get() = _movie.asStateFlow()

    init {
        loadMovie()
    }

    private fun loadMovie() {
        viewModelScope.launch {
            getMovieDetailsUseCase(movieId)
                .collect(_movie)
        }
    }

    fun onRetryClicked() = loadMovie()
}
