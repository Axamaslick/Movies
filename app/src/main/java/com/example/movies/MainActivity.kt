package com.example.movies

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import androidx.lifecycle.Observer
import androidx.activity.viewModels

class MainActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var fabAddMovie: FloatingActionButton
    private lateinit var btnDeleteSelected: Button
    private lateinit var adapter: MovieAdapter

    private val viewModel: MovieViewModel by viewModels {
        MovieViewModelFactory(MovieDatabase.getDatabase(this).movieDao())
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        recyclerView = findViewById(R.id.moviesList)
        fabAddMovie = findViewById(R.id.fabAddMovie)
        btnDeleteSelected = findViewById(R.id.btnDeleteSelected)

        adapter = MovieAdapter { movie -> viewModel.deleteMovie(movie) }
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(this)

        fabAddMovie.setOnClickListener {
            startActivity(Intent(this, AddActivity::class.java))
        }

        btnDeleteSelected.setOnClickListener {
            viewModel.deleteSelectedMovies(adapter.getSelectedMovies())
        }

        viewModel.allMovies.observe(this, Observer { movies ->
            adapter.submitList(movies)
        })

    }
}