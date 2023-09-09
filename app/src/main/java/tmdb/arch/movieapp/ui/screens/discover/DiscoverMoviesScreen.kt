package tmdb.arch.movieapp.ui.screens.discover

import android.content.res.Configuration
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.paging.LoadState
import androidx.paging.PagingData
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.itemContentType
import androidx.paging.compose.itemKey
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import tmdb.arch.movieapp.BuildConfig
import tmdb.arch.movieapp.R
import tmdb.arch.movieapp.compose.components.TmdbError
import tmdb.arch.movieapp.compose.components.TmdbListItemMovie
import tmdb.arch.movieapp.compose.components.TmdbLoading
import tmdb.arch.movieapp.compose.theme.TmdbTheme
import tmdb.arch.movieapp.repository.models.Movie

@Composable
fun DiscoverMoviesScreen(
    viewModel: DiscoverMoviesViewModel,
    onMovieClicked: (Long) -> Unit,
    onSearchClicked: () -> Unit,
    onFavoritesClicked: () -> Unit,
    onToWatchClicked: () -> Unit
) {
    ScreenContent(
        movies = viewModel.movies,
        onSearchClicked = onSearchClicked,
        onFavoritesClicked = onFavoritesClicked,
        onToWatchClicked = onToWatchClicked,
        onMovieClicked = onMovieClicked
    )
}

@Composable
private fun ScreenContent(
    movies: StateFlow<PagingData<Movie>>,
    onMovieClicked: (Long) -> Unit,
    onSearchClicked: () -> Unit,
    onFavoritesClicked: () -> Unit,
    onToWatchClicked: () -> Unit
) {
    Column(modifier = Modifier.fillMaxSize()) {
        HeaderButtons(
            onSearchClicked = onSearchClicked,
            onFavoritesClicked = onFavoritesClicked,
            onToWatchClicked = onToWatchClicked
        )
        ListItems(
            movies = movies,
            onClick = onMovieClicked
        )
    }
}

@Composable
private fun HeaderButtons(
    onSearchClicked: () -> Unit,
    onFavoritesClicked: () -> Unit,
    onToWatchClicked: () -> Unit
) {
    Button(
        onClick = onSearchClicked,
        modifier = Modifier.fillMaxWidth()
    ) {
        Icon(
            painter = painterResource(id = tmdb.arch.movieapp.compose.R.drawable.ic_search),
            contentDescription = null,
            tint = MaterialTheme.colorScheme.onPrimary,
            modifier = Modifier.size(24.dp)
        )
    }

    Row(
        modifier = Modifier.fillMaxWidth()
            .padding(horizontal = 20.dp)
    ) {
        Button(
            onClick = onFavoritesClicked,
            modifier = Modifier.weight(1f)
        ) {
            Text(
                text = "Favorites"
            )
        }

        Spacer(modifier = Modifier.width(10.dp))

        Button(
            onClick = onToWatchClicked,
            modifier = Modifier.weight(1f)
        ) {
            Text(
                text = "To Watch"
            )
        }
    }
}

@Composable
private fun ColumnScope.ListItems(
    movies: StateFlow<PagingData<Movie>>,
    onClick: (Long) -> Unit
) {
    val moviesState = movies.collectAsLazyPagingItems()

    when (moviesState.loadState.refresh) {
        is LoadState.Loading -> {
            TmdbLoading(modifier = Modifier.align(Alignment.CenterHorizontally))
        }

        is LoadState.Error -> {
            TmdbError(
                onRetryClick = { moviesState.retry() }
            )
        }

        is LoadState.NotLoading -> {
            LazyColumn(
                contentPadding = PaddingValues(vertical = 10.dp),
                modifier = Modifier.weight(1f)
            ) {
                items(
                    count = moviesState.itemCount,
                    key = moviesState.itemKey { it.id },
                    contentType = moviesState.itemContentType { "movie" }
                ) {
                    val item = moviesState[it] ?: return@items
                    TmdbListItemMovie(
                        title = item.title,
                        model = BuildConfig.IMAGE_URL + item.posterPath,
                        language = item.originalLanguage,
                        rating = item.voteAverage,
                        playTime = item.runTime,
                        onClick = { onClick(item.id) },
                        modifier = Modifier.padding(horizontal = 20.dp, vertical = 10.dp)
                    )
                }

                with(moviesState.loadState.append) {
                    if (endOfPaginationReached || this is LoadState.NotLoading) return@with
                    item {
                        if (this@with is LoadState.Loading) {
                            Box(modifier = Modifier.fillMaxWidth()) {
                                TmdbLoading(modifier = Modifier.align(Alignment.Center))
                            }
                        } else {
                            TmdbError { moviesState.retry() }
                        }
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun ScreenPreview() {
    TmdbTheme {
        val movies = MutableStateFlow(
            PagingData.from(
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
        )

        ScreenContent(
            movies = movies,
            onSearchClicked = { },
            onFavoritesClicked = { },
            onToWatchClicked = { },
            onMovieClicked = { }
        )
    }
}
