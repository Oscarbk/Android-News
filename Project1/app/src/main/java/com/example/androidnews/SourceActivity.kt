package com.example.androidnews

import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.EditText
import android.widget.Spinner
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class SourceActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_source)

        val intent = getIntent()
        val term: String = intent.getStringExtra("TERM")!!
        setTitle("Search for $term")

        recyclerView = findViewById(R.id.recyclerView)
        val sources = getFakeSources()
        val adapter = SourcesAdapter(sources)

        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(this)

        // The following code snippet is adapted from the android developer documentation on Spinners
        val spinner: Spinner = findViewById(R.id.spinner)
        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter.createFromResource(
            this,
            R.array.sources_array,
            android.R.layout.simple_spinner_item
        ).also { adapter ->
            // Specify the layout to use when the list of choices appears
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            // Apply the adapter to the spinner
            spinner.adapter = adapter
        }
    }
    fun getFakeSources(): List<Source> {
        return listOf(
            Source(
                username = "iaculis nunc",
                content = "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Eu ultrices vitae auctor eu augue ut lectus. Sollicitudin tempor id eu nisl. Diam volutpat commodo sed egestas egestas fringilla.",
            ),
            Source(
                username = "aliquet porttitor",
                content = "Odio ut enim blandit volutpat maecenas volutpat blandit.",
            ),
            Source(
                username = "tincidunt tortor",
                content = "Luctus accumsan tortor posuere ac ut consequat semper viverra.",
            ),
            Source(
                username = "tellus elementum",
                content = "Urna condimentum mattis pellentesque id nibh. Sollicitudin aliquam ultrices sagittis orci a scelerisque. Egestas integer eget aliquet nibh praesent tristique magna sit amet.",
            ),
            Source(
                username = "ante in",
                content = "Cras adipiscing enim eu turpis egestas pretium.",
            ),
            Source(
                username = "sociis natoque",
                content = "Purus semper eget duis at tellus at urna condimentum. Urna condimentum mattis pellentesque id nibh tortor id.",
            ),
            Source(
                username = "lorem ipsum",
                content = "Adipiscing bibendum est ultricies integer quis auctor elit sed vulputate.",
            ),
            Source(
                username = "aliquam etiam",
                content = "Elementum sagittis vitae et leo duis ut diam quam.",
            ),
            Source(
                username = "euismod nisi",
                content = "Proin sagittis nisl rhoncus mattis rhoncus urna. Vitae tortor condimentum lacinia quis vel eros donec ac odio.",
            ),
            Source(
                username = "quisque id",
                content = "Dignissim sodales ut eu sem integer vitae justo.",
            )
        )
    }
}