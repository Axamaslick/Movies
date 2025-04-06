package com.example.movies

import com.google.gson.annotations.SerializedName

data class MovieDetailsResponse(
    @SerializedName("Title") val title: String?,
    @SerializedName("Year") val year: String?,
    @SerializedName("Poster") val poster: String?
)