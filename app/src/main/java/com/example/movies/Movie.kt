package com.example.movies

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "movies")
data class Movie(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val title: String,
    val year: String,
    val posterUrl: String, // Field for storing the poster URL
    var isSelected: Boolean = false
)