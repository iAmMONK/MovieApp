package tmdb.arch.movieapp.domain.model

data class Movie(
    val adult: Boolean,
    val backdropPath: String?,
    val genres: List<String>?,
    val id: Long,
    val originalLanguage: String,
    val originalTitle: String,
    val overview: String,
    val runTime: Int?,
    val popularity: Double,
    val posterPath: String?,
    val releaseDate: String?,
    val title: String,
    val video: Boolean,
    val voteAverage: String?,
    val voteCount: Int,
    var isFavored: Boolean = false,
    var isToWatch: Boolean = false
)
