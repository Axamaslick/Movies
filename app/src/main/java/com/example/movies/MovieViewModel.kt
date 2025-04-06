package com.example.movies

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MovieViewModel(private val movieDao: MovieDao) : ViewModel() {
    val allMovies: LiveData<List<Movie>> = movieDao.getAllMovies()

    fun deleteMovie(movie: Movie) {
        viewModelScope.launch(Dispatchers.IO) {
            movieDao.deleteMovie(movie)
        }
    }

    fun deleteSelectedMovies(movies: List<Movie>) {
        viewModelScope.launch(Dispatchers.IO) {
            movieDao.deleteMovies(movies)
        }
    }

    fun insertMovie(movie: Movie) {
        viewModelScope.launch(Dispatchers.IO) {
            movieDao.insertMovie(movie)
        }
    }
}