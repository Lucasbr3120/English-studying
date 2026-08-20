package com.example.data.remote

import com.example.data.model.YouTubeSearchResponse
import com.example.data.model.YouTubeVideoListResponse
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query
import java.util.concurrent.TimeUnit

interface YouTubeBackendApiService {

    @GET("api/youtube/search")
    suspend fun searchVideos(
        @Query("q") query: String,
        @Query("category") category: String? = null,
        @Query("level") level: String? = null,
        @Query("creativeCommonsOnly") creativeCommonsOnly: Boolean = false,
        @Query("maxResults") maxResults: Int = 15
    ): Response<YouTubeSearchResponse>

    @GET("api/youtube/videos")
    suspend fun getVideoDetails(
        @Query("id") videoIds: String
    ): Response<YouTubeVideoListResponse>

    companion object {
        fun create(baseUrl: String): YouTubeBackendApiService {
            val validUrl = if (baseUrl.endsWith("/")) baseUrl else "$baseUrl/"
            val logging = HttpLoggingInterceptor().apply {
                level = HttpLoggingInterceptor.Level.BASIC
            }

            val client = OkHttpClient.Builder()
                .addInterceptor(logging)
                .connectTimeout(15, TimeUnit.SECONDS)
                .readTimeout(15, TimeUnit.SECONDS)
                .build()

            val moshi = Moshi.Builder()
                .add(KotlinJsonAdapterFactory())
                .build()

            return Retrofit.Builder()
                .baseUrl(validUrl)
                .client(client)
                .addConverterFactory(MoshiConverterFactory.create(moshi))
                .build()
                .create(YouTubeBackendApiService::class.java)
        }
    }
}
