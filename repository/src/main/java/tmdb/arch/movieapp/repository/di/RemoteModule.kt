package tmdb.arch.movieapp.repository.di

import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import tmdb.arch.movieapp.repository.remote.MoviesService
import tmdb.arch.movieapp.repository.remote.interceptors.AuthInterceptor
import tmdb.arch.movieapp.repository.remote.interceptors.LanguageInterceptor
import java.util.concurrent.TimeUnit

private val httpClient
    get() = OkHttpClient.Builder()
        .addInterceptor(AuthInterceptor())
        .addInterceptor(LanguageInterceptor())
        .callTimeout(30L, TimeUnit.SECONDS)
        .build()

private val retrofit
    get() = Retrofit.Builder()
        .addConverterFactory(GsonConverterFactory.create())
        .client(httpClient)
        .baseUrl("https://api.themoviedb.org/3/")
        .build()

internal val moviesService get() = retrofit.create(MoviesService::class.java)
