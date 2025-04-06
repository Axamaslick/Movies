package com.example.movies

import retrofit2.Call
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {

    @GET("/")
    fun searchMovies(
        @Query("s") query: String,
        @Query("apikey") apiKey: String
    ): Call<MovieResponse>

    @GET("/")
    fun getMovieDetails(
        @Query("i") imdbId: String,
        @Query("apikey") apiKey: String
    ): Call<MovieDetailsResponse>

    @GET("/")
    suspend fun searchMovieDetails(
        @Query("t") title: String,
        @Query("y") year: String?,
        @Query("apikey") apiKey: String
    ): Response<MovieDetailsResponse>
}