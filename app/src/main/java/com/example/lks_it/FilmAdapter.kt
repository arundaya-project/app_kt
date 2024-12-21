package com.example.lks_it

import android.graphics.BitmapFactory
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import java.net.URL
import kotlin.concurrent.thread

data class Film(val id: Int, val title: String, val imageUrl: String)

class FilmAdapter(private var films: List<Film>) :
    RecyclerView.Adapter<FilmAdapter.FilmViewHolder>() {

    inner class FilmViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val title: TextView = itemView.findViewById(R.id.title)
        val image: ImageView = itemView.findViewById(R.id.image)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FilmViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_film, parent, false)
        return FilmViewHolder(view)
    }

    override fun onBindViewHolder(holder: FilmViewHolder, position: Int) {
        val film = films[position]
        holder.title.text = film.title
        loadImage(film.imageUrl, holder.image)
    }

    override fun getItemCount(): Int = films.size

    fun updateData(newFilms: List<Film>) {
        this.films = newFilms
        notifyDataSetChanged()
    }

    private fun loadImage(url: String, imageView: ImageView) {
        thread {
            try {
                val bitmap = BitmapFactory.decodeStream(URL(url).openStream())
                imageView.post {
                    imageView.setImageBitmap(bitmap)
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
}