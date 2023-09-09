package tmdb.arch.movieapp.ui.screens.search

import android.content.res.Configuration
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.arch.utils.UiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import tmdb.arch.movieapp.BuildConfig
import tmdb.arch.movieapp.compose.components.TmdbError
import tmdb.arch.movieapp.compose.components.TmdbListItemMovie
import tmdb.arch.movieapp.compose.components.TmdbLoading
import tmdb.arch.movieapp.compose.theme.TmdbTheme
import tmdb.arch.movieapp.repository.models.Movie

@Composable
fun SearchMoviesScreen(
    viewModel: SearchMoviesViewModel,
    onMovieClicked: (Long) -> Unit
) {
    ScreenContent(
        onQueryChanged = viewModel::onSearchQueryChanged,
        searchResultState = viewModel.searchResultsState,
        onMovieClicked = onMovieClicked
    )
}

@Composable
private fun ScreenContent(
    onQueryChanged: (String) -> Unit,
    searchResultState: StateFlow<UiState<List<Movie>>>,
    onMovieClicked: (Long) -> Unit
) {
    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        var text by remember { mutableStateOf("") }

        TextField(
            value = text,
            onValueChange = {
                text = it
                onQueryChanged(it)
            },
            modifier = Modifier.fillMaxWidth()
        )

        MoviesList(
            searchResultState = searchResultState,
            onMovieClicked = onMovieClicked
        )
    }
}

@Composable
private fun ColumnScope.MoviesList(
    searchResultState: StateFlow<UiState<List<Movie>>>,
    onMovieClicked: (Long) -> Unit
) {
    val state by searchResultState.collectAsState()

    when (val result = state) {
        UiState.Loading -> {
            TmdbLoading(
                modifier = Modifier.align(Alignment.CenterHorizontally)
                    .padding(top = 20.dp)
            )
        }

        UiState.Error -> {
            Spacer(modifier = Modifier.height(20.dp))
            TmdbError()
        }

        is UiState.Result -> {
            Column(
                modifier = Modifier
                    .weight(1f)
                    .verticalScroll(rememberScrollState())
                    .padding(horizontal = 20.dp)
            ) {
                Spacer(modifier = Modifier.height(20.dp))
                result.item.forEach { item ->
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
    }
}

@Preview(showBackground = true)
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun ScreenPreviewLoading() {
    TmdbTheme {
        Column {
            ScreenContent(
                onQueryChanged = {},
                searchResultState = MutableStateFlow(UiState.Loading),
                onMovieClicked = {}
            )
        }
    }
}

@Preview(showBackground = true)
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun ScreenPreviewError() {
    TmdbTheme {
        ScreenContent(
            onQueryChanged = {},
            searchResultState = MutableStateFlow(UiState.Error),
            onMovieClicked = {}
        )
    }
}

@Preview(showBackground = true)
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun ScreenPreviewResult() {
    TmdbTheme {
        ScreenContent(
            onQueryChanged = {},
            searchResultState = MutableStateFlow(
                UiState.Result(
                    listOf(
                        Movie(
                            title = "Terminator",
                            id = 1231L,
                            originalTitle = "Terminator",
                            originalLanguage = "English",
                            voteAverage = "8.8",
                            runTime = 151,
                            genres = null
                        )
                    )
                )
            ),
            onMovieClicked = {}
        )
    }
}
