package com.dicoding.made.data.remote.service

import com.dicoding.made.BuildConfig
import com.dicoding.made.data.local.model.Movie
import com.dicoding.made.data.local.model.MovieResponse
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface MovieApiService {
    companion object {
        operator fun invoke(): MovieApiService {
            val requestInterceptor = Interceptor { chain ->
                val url = chain.request()
                    .url()
                    .newBuilder()
                    .addQueryParameter("api_key",BuildConfig.API_KEY)
                    .build()

                val request = chain.request()
                    .newBuilder()
                    .url(url)
                    .build()

                return@Interceptor chain.proceed(request)
            }

            val okHttpClient = OkHttpClient.Builder()
                .addInterceptor(requestInterceptor)
                .build()

            return Retrofit.Builder()
                .client(okHttpClient)
                .baseUrl(BuildConfig.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(MovieApiService::class.java)
        }
    }

    @GET("discover/{type}")
    suspend fun getMovieAsync(
        @Path("type") type: String,
        @Query("language") language: String,
        @Query("page") page: Int
    ): MovieResponse

    @GET("discover/movie")
    suspend fun getReleaseAsync(
        @Query("language") language: String,
        @Query("primary_release_date.gte") gte: String,
        @Query("primary_release_date.lte") lte: String
    ): MovieResponse

    @GET("{type}/{id}")
    suspend fun getDetailMovieAsync(
        @Path("type") type: String,
        @Path("id") movie_id: String,
        @Query("language") language: String,
        @Query("append_to_response") response: String
    ): Movie

    @GET("search/{type}")
    suspend fun search(
        @Path("type") type: String,
        @Query("language") language: String,
        @Query("query") query: String,
        @Query("page") page: Int
    ): MovieResponse
}