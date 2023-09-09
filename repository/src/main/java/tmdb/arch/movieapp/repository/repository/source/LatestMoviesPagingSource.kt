package tmdb.arch.movieapp.repository.repository.source

import androidx.paging.PagingSource
import androidx.paging.PagingState
import kotlinx.coroutines.delay
import tmdb.arch.movieapp.repository.models.Movie
import tmdb.arch.movieapp.repository.remote.MoviesService
import tmdb.arch.movieapp.repository.remote.model.MovieDto
import tmdb.arch.movieapp.repository.remote.model.toModel

internal class LatestMoviesPagingSource(
    private val service: MoviesService
) : PagingSource<Int, Movie>() {

    override fun getRefreshKey(state: PagingState<Int, Movie>) = 1

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Movie> {
        val key = params.key ?: 1

        return try {
            val response = service.getLatestMovies(key)
                .movieDtos
                .map(MovieDto::toModel)

            delay(5000)

            LoadResult.Page(
                data = response,
                prevKey = null,
                nextKey = key + 1
            )
        } catch (ex: Exception) {
            LoadResult.Error(ex)
        }
    }
}
