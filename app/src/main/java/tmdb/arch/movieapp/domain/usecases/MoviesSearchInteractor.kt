package tmdb.arch.movieapp.domain.usecases

import com.example.arch.utils.UiState
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.map
import tmdb.arch.movieapp.repository.models.Movie
import tmdb.arch.movieapp.repository.repository.MoviesRepository

class MoviesSearchInteractor(
    private val repository: MoviesRepository
) {

    private companion object {
        const val DEBOUNCE_DURATION = 300L
    }

    private val _searchFlow: MutableSharedFlow<CharSequence> = MutableSharedFlow(
        extraBufferCapacity = 1
    )

    @OptIn(FlowPreview::class)
    val searchResult: Flow<UiState<List<Movie>>>
        get() = _searchFlow
            .filter(CharSequence::isNotBlank)
            .debounce(DEBOUNCE_DURATION)
            .map(CharSequence::toString)
            .map(::performSearch)
            .catch { emit(UiState.Error) }

    fun searchQuery(query: CharSequence) {
        _searchFlow.tryEmit(query)
    }

    private suspend fun performSearch(query: String): UiState<List<Movie>> {
        return try {
            val result = repository.findMovies(query)
            if (result.isEmpty()) UiState.Error else UiState.Result(result)
        } catch (ex: Exception) {
            UiState.Error
        }
    }
}
