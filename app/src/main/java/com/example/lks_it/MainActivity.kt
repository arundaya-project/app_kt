package com.example.lks_it

import android.os.Bundle
import android.widget.SearchView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import org.json.JSONObject
import java.net.HttpURLConnection
import java.net.URL

class MainActivity : AppCompatActivity() {
    private lateinit var filmAdapter: FilmAdapter
    private val films = mutableListOf<Film>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val recyclerView = findViewById<RecyclerView>(R.id.recyclerView)
        val searchView = findViewById<SearchView>(R.id.searchView)

        filmAdapter = FilmAdapter(films)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = filmAdapter

        fetchFilms()

        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                filterFilms(query)
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                filterFilms(newText)
                return true
            }
        })
    }

    private fun fetchFilms() {
        Thread {
            val url = URL("http://api.ptraazxtt.my.id/api/v1/film")
            val connection = url.openConnection() as HttpURLConnection
            connection.requestMethod = "GET"

            val response = connection.inputStream.bufferedReader().use { it.readText() }
            val jsonResponse = JSONObject(response)
            val filmArray = jsonResponse.getJSONArray("films")

            for (i in 0 until filmArray.length()) {
                val filmObject = filmArray.getJSONObject(i)
                val film = Film(
                    id = filmObject.getInt("id"),
                    title = filmObject.getString("title"),
                    imageUrl = "http://api.ptraazxtt.my.id/storage/image/" + filmObject.getString("image")
                )
                films.add(film)
            }

            runOnUiThread {
                filmAdapter.notifyDataSetChanged()
            }
        }.start()
    }

    private fun filterFilms(query: String?) {
        val filtered = if (query.isNullOrEmpty()) {
            films
        } else {
            films.filter { it.title.contains(query, ignoreCase = true) }
        }
        filmAdapter.updateData(filtered)
    }
}