package com.example.movies

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class SearchActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: SearchAdapter
    private lateinit var movieDatabase: MovieDatabase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)

        recyclerView = findViewById(R.id.searchResultsList)
        movieDatabase = MovieDatabase.getDatabase(this)

        adapter = SearchAdapter(this, emptyList()) { movie ->
            addMovieToDatabase(movie)
        }
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(this)

        val searchQuery = intent.getStringExtra("searchQuery") ?: ""

        if (searchQuery.isNotEmpty()) {
            searchMovies(searchQuery)
        } else {
            Toast.makeText(this, "Введите название фильма", Toast.LENGTH_SHORT).show()
        }
    }

    private fun searchMovies(query: String) {
        val retrofit = Retrofit.Builder()
            .baseUrl("https://www.omdbapi.com/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val apiService = retrofit.create(ApiService::class.java)
        val apiKey = "a34ca3cc"

        val call = apiService.searchMovies(query, apiKey)

        call.enqueue(object : Callback<MovieResponse> {
            override fun onResponse(call: Call<MovieResponse>, response: Response<MovieResponse>) {
                if (response.isSuccessful) {
                    val movies = response.body()?.Search ?: emptyList()
                    adapter.updateMovies(movies)
                } else {
                    Toast.makeText(this@SearchActivity, "Ошибка при выполнении запроса", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<MovieResponse>, t: Throwable) {
                Toast.makeText(this@SearchActivity, "Ошибка сети: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun addMovieToDatabase(movie: MovieItem) {
        lifecycleScope.launch {
            val movieEntity = Movie(
                id = 0,
                title = movie.Title,
                year = movie.Year,
                posterUrl = movie.Poster
            )
            movieDatabase.movieDao().insertMovie(movieEntity)

            // Redirect to MainActivity after adding the movie
            startActivity(Intent(this@SearchActivity, MainActivity::class.java))
            finish()
        }
    }
}