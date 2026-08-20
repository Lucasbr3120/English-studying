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

interface YouTubeApiService {

    @GET("search")
    suspend fun searchVideos(
        @Query("part") part: String = "snippet",
        @Query("type") type: String = "video",
        @Query("videoEmbeddable") videoEmbeddable: String = "true",
        @Query("videoCaption") videoCaption: String = "closedCaption",
        @Query("relevanceLanguage") relevanceLanguage: String = "en",
        @Query("safeSearch") safeSearch: String = "strict",
        @Query("videoLicense") videoLicense: String? = null,
        @Query("q") query: String,
        @Query("maxResults") maxResults: Int = 15,
        @Query("key") apiKey: String
    ): Response<YouTubeSearchResponse>

    @GET("videos")
    suspend fun getVideoDetails(
        @Query("part") part: String = "snippet,contentDetails,status",
        @Query("id") videoIds: String,
        @Query("key") apiKey: String
    ): Response<YouTubeVideoListResponse>

    companion object {
        private const val BASE_URL = "https://www.googleapis.com/youtube/v3/"

        fun create(): YouTubeApiService {
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
                .baseUrl(BASE_URL)
                .client(client)
                .addConverterFactory(MoshiConverterFactory.create(moshi))
                .build()
                .create(YouTubeApiService::class.java)
        }
    }
}
