package com.example.movies

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import kotlinx.coroutines.launch
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class AddActivity : AppCompatActivity() {
    private lateinit var etSearch: EditText
    private lateinit var etYear: EditText
    private lateinit var btnSearch: Button
    private lateinit var btnAddMovie: Button
    private lateinit var ivPoster: ImageView
    private lateinit var movieDatabase: MovieDatabase
    private var posterUrl: String? = null
    private lateinit var searchButton: Button

    private val retrofit = Retrofit.Builder()
        .baseUrl("https://www.omdbapi.com/")
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    private val apiService = retrofit.create(ApiService::class.java)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add)

        etSearch = findViewById(R.id.etSearch)
        etYear = findViewById(R.id.etYear)
        btnSearch = findViewById(R.id.btnSearch)
        btnAddMovie = findViewById(R.id.btnAddMovie)
        ivPoster = findViewById(R.id.ivPoster)
        searchButton = findViewById(R.id.searchButton) // Initialize searchButton

        movieDatabase = MovieDatabase.getDatabase(this)

        btnSearch.setOnClickListener {
            val searchQuery = etSearch.text.toString()
            val year = etYear.text.toString()

            lifecycleScope.launch {
                try {
                    val response = apiService.searchMovieDetails(
                        title = searchQuery,
                        year = if (year.isNotEmpty()) year else null,
                        apiKey = "a34ca3cc"
                    )

                    if (response.isSuccessful) {
                        val movie = response.body()
                        posterUrl = movie?.poster

                        posterUrl?.let { url ->
                            Glide.with(this@AddActivity)
                                .load(url)
                                .into(ivPoster)
                        } ?: run {
                            ivPoster.setImageResource(R.drawable.placeholder)
                        }
                    } else {
                        showError("Фильм не найден")
                    }
                } catch (e: Exception) {
                    showError("Ошибка сети: ${e.message}")
                }
            }
        }



        btnAddMovie.setOnClickListener {
            val title = etSearch.text.toString()
            val year = etYear.text.toString()
            val currentPosterUrl = posterUrl ?: "https://example.com/placeholder.jpg"

            if (title.isEmpty()) {
                etSearch.error = "Введите название фильма"
                return@setOnClickListener
            }

            lifecycleScope.launch {
                val movie = Movie(
                    id = 0,
                    title = title,
                    year = year,
                    posterUrl = currentPosterUrl
                )
                movieDatabase.movieDao().insertMovie(movie)
                finish()
            }
        }
        searchButton.setOnClickListener {
            val query = etSearch.text.toString()
            if (query.isNotEmpty()) {
                val intent = Intent(this, SearchActivity::class.java).apply {
                    putExtra("searchQuery", query)
                }
                startActivity(intent)
            } else {
                etSearch.error = "Введите название фильма"
            }
        }
    }

    private fun showError(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
}