package com.example.movies

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import android.widget.TextView
import android.widget.ImageView

class SearchAdapter(
    private val context: Context,
    private var movies: List<MovieItem>,
    private val onItemClick: (MovieItem) -> Unit // Лямбда для обработки кликов
) : RecyclerView.Adapter<SearchAdapter.ViewHolder>() {

    fun updateMovies(newMovies: List<MovieItem>) {
        movies = newMovies
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.search_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(movies[position])
        holder.itemView.setOnClickListener { onItemClick(movies[position]) }
    }

    override fun getItemCount(): Int = movies.size

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val title: TextView = itemView.findViewById(R.id.tvTitle)
        private val year: TextView = itemView.findViewById(R.id.tvYear)
        private val poster: ImageView = itemView.findViewById(R.id.ivPoster)

        fun bind(movie: MovieItem) {
            title.text = movie.Title
            year.text = movie.Year
            Glide.with(context)
                .load(movie.Poster)
                .placeholder(R.drawable.placeholder)
                .into(poster)
            itemView.setOnClickListener {
                onItemClick(movie) // Вызов лямбды при клике
            }
        }
    }
}