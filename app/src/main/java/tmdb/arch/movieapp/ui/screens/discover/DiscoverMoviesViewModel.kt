package tmdb.arch.movieapp.ui.screens.discover

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import tmdb.arch.movieapp.domain.repository.MoviesRepository

class DiscoverMoviesViewModel(private val service: MoviesRepository) : ViewModel() {

    private val _text = MutableStateFlow("Hello world")

    val text: StateFlow<String> get() = _text.asStateFlow()

    init {
        viewModelScope.launch {
            _text.emit("Hello Android")

            delay(5000L)

            _text.emit("Hello World")

        }
    }
}
