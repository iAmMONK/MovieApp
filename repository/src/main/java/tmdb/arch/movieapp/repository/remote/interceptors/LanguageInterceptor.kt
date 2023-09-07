package tmdb.arch.movieapp.repository.remote.interceptors

import okhttp3.Interceptor
import okhttp3.Response
import java.util.Locale

internal class LanguageInterceptor : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val original = chain.request()
        val originalHttpUrl = original.url

        val url = originalHttpUrl.newBuilder()
            .addQueryParameter("language", Locale.getDefault().toString())
            .build()

        return chain.proceed(original.newBuilder().url(url).build())
    }
}
