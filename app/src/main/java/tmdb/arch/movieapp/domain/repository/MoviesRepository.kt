package tmdb.arch.movieapp.domain.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import tmdb.arch.movieapp.domain.local.MoviesDao
import tmdb.arch.movieapp.domain.model.Movie
import tmdb.arch.movieapp.domain.model.local.FavoriteEntity
import tmdb.arch.movieapp.domain.model.local.ToWatchEntity
import tmdb.arch.movieapp.domain.model.remote.MovieDto
import tmdb.arch.movieapp.domain.model.remote.toModel
import tmdb.arch.movieapp.domain.remote.MoviesService

class MoviesRepository(
    private val remoteService: MoviesService,
    private val moviesDao: MoviesDao
) {

    fun getLatestMovies() = Pager(config = PagingConfig(pageSize = 50)) {
        LatestMoviesPagingSource(remoteService)
    }.flow

    suspend fun findMovies(query: String) = remoteService.findMovies(query)
        .movieDtos
        .map(MovieDto::toModel)

    suspend fun getMovieDetails(id: Long) = remoteService.getMovieDetails(id).toModel()

    fun getFavoritesIds(): Flow<List<Long>> = moviesDao.getAllFavorites()
        .map { items -> items.map { entity -> entity.movieId } }

    suspend fun insertFavorite(movieId: Long) =
        moviesDao.insertFavorite(FavoriteEntity(movieId))

    suspend fun deleteFavorite(movieId: Long) =
        moviesDao.deleteFavorite(FavoriteEntity(movieId))

    fun getToWatchIds(): Flow<List<Long>> = moviesDao.getAllToWatch()
        .map { items -> items.map { entity -> entity.movieId } }

    suspend fun insertToWatch(movie: Movie) =
        moviesDao.insertToWatch(
            ToWatchEntity(
                movieId = movie.id,
                title = movie.title,
                originalTitle = movie.originalTitle,
                releaseDate = movie.releaseDate,
                voteAverage = movie.voteAverage,
                posterPath = movie.posterPath,
                overview = movie.overview,
                genres = movie.genres,
                runTime = movie.runTime
            )
        )

    suspend fun deleteToWatch(movie: Movie) =
        moviesDao.deleteToWatch(movie.id)
}
