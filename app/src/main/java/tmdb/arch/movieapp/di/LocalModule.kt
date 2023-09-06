package tmdb.arch.movieapp.di

import android.content.Context
import androidx.room.Room
import org.koin.dsl.module
import tmdb.arch.movieapp.domain.local.MoviesDatabase
import tmdb.arch.movieapp.domain.local.converters.StringListConverter

val localModule
    get() = module {
        single { createMoviesDataBase(get<Context>()) }
    }

private fun createMoviesDataBase(context: Context) =
    Room.databaseBuilder(context, MoviesDatabase::class.java, "movies_db")
        .addTypeConverter(StringListConverter())
        .build()
        .movies
