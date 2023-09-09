package tmdb.arch.movieapp.ui.screens.saved

import android.content.res.Configuration
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import tmdb.arch.movieapp.BuildConfig
import tmdb.arch.movieapp.compose.components.TmdbListItemMovie
import tmdb.arch.movieapp.compose.theme.TmdbTheme
import tmdb.arch.movieapp.repository.models.Movie

@Composable
fun SavedMoviesScreen(
    viewModel: SavedMoviesViewModel,
    onMovieClicked: (Long) -> Unit
) {
    var movies by remember { mutableStateOf<List<Movie>>(emptyList()) }

    LaunchedEffect(key1 = Unit) {
        launch {
            viewModel.savedMovies.collectLatest {
                movies = it
            }
        }
    }

    ScreenContent(
        movies = movies,
        onMovieClicked = onMovieClicked
    )
}

@Composable
private fun ScreenContent(
    movies: List<Movie>,
    onMovieClicked: (Long) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 20.dp)
    ) {
        Spacer(modifier = Modifier.height(20.dp))
        movies.forEach { item ->
            TmdbListItemMovie(
                title = item.title,
                onClick = { onMovieClicked(item.id) },
                language = item.originalLanguage,
                rating = item.voteAverage,
                playTime = item.runTime,
                model = BuildConfig.IMAGE_URL + item.posterPath,
                modifier = Modifier.padding(vertical = 10.dp)
            )
        }

        Spacer(modifier = Modifier.height(20.dp))
    }
}

@Preview(showBackground = true)
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun ScreenPreview() {
    TmdbTheme {
        ScreenContent(
            movies = listOf(
                Movie(
                    title = "Terminator",
                    id = 1231L,
                    originalTitle = "Terminator",
                    originalLanguage = "English",
                    voteAverage = "8.8",
                    runTime = 151,
                    genres = null
                )
            ),
            onMovieClicked = { }
        )
    }
}
