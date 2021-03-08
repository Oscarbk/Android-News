package com.example.androidnews

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.*
import android.widget.AdapterView.OnItemSelectedListener
import android.widget.ArrayAdapter
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.logging.HttpLoggingInterceptor
import org.jetbrains.anko.doAsync
import org.json.JSONObject
import org.w3c.dom.Text


class ResultsActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var categories: Spinner
    private lateinit var categoryLabel: TextView
    private lateinit var sourceLabel: TextView

    // OkHttp is a library used to make network calls
    private val okHttpClient: OkHttpClient
    init {
        val builder = OkHttpClient.Builder()

        // This causes all network traffic to be logged to the console
        val logging = HttpLoggingInterceptor()
        logging.level = HttpLoggingInterceptor.Level.BODY
        builder.addInterceptor(logging)

        okHttpClient = builder.build()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_source)

        val intent = getIntent()
        val source: String = intent.getStringExtra("SOURCE")!!
        val term: String = intent.getStringExtra("TERM")!!
        setTitle("$source results for $term")

        recyclerView = findViewById(R.id.recyclerView)
        categories = findViewById(R.id.spinner)
        categoryLabel = findViewById(R.id.category)
        sourceLabel = findViewById(R.id.sourceBox)

        categories.visibility = View.GONE
        categoryLabel.visibility = View.GONE
        sourceLabel.visibility = View.GONE

        doAsync {
            val sources = retrieveSources(source, term)
            runOnUiThread {
                val adapter = SourcesAdapter(sources)
                recyclerView.adapter = adapter
                recyclerView.layoutManager = LinearLayoutManager(this@ResultsActivity)
            }
        }
    }
    /*fun getFakeSources(): List<Source> {
        return listOf(
                Source(
                        username = "iaculis nunc",
                        content = "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Eu ultrices vitae auctor eu augue ut lectus. Sollicitudin tempor id eu nisl. Diam volutpat commodo sed egestas egestas fringilla.",
                        source = "placeholder",
                        url = "google",

                        ),
                Source(
                        username = "aliquet porttitor",
                        content = "Odio ut enim blandit volutpat maecenas volutpat blandit.",
                        source = "placeholder",
                        url = "google",
                ),
                Source(
                        username = "tincidunt tortor",
                        content = "Luctus accumsan tortor posuere ac ut consequat semper viverra.",
                        source = "placeholder",
                        url = "google",
                ),
                Source(
                        username = "tellus elementum",
                        content = "Urna condimentum mattis pellentesque id nibh. Sollicitudin aliquam ultrices sagittis orci a scelerisque. Egestas integer eget aliquet nibh praesent tristique magna sit amet.",
                        source = "placeholder",
                        url = "google",
                ),
                Source(
                        username = "ante in",
                        content = "Cras adipiscing enim eu turpis egestas pretium.",
                        source = "placeholder",
                        url = "google",
                ),
                Source(
                        username = "sociis natoque",
                        content = "Purus semper eget duis at tellus at urna condimentum. Urna condimentum mattis pellentesque id nibh tortor id.",
                        source = "placeholder",
                        url = "google",
                ),
                Source(
                        username = "lorem ipsum",
                        content = "Adipiscing bibendum est ultricies integer quis auctor elit sed vulputate.",
                        source = "placeholder",
                        url = "google",
                ),
                Source(
                        username = "aliquam etiam",
                        content = "Elementum sagittis vitae et leo duis ut diam quam.",
                        source = "placeholder",
                        url = "google",
                ),
                Source(
                        username = "euismod nisi",
                        content = "Proin sagittis nisl rhoncus mattis rhoncus urna. Vitae tortor condimentum lacinia quis vel eros donec ac odio.",
                        source = "placeholder",
                        url = "google",
                ),
                Source(
                        username = "quisque id",
                        content = "Dignissim sodales ut eu sem integer vitae justo.",
                        source = "placeholder",
                        url = "google",
                )
        )
    }*/
    fun retrieveSources(category: String, term: String): List<Source>
    {

        val apiKey = getString(R.string.api_key)

        // Building the request
        val request = Request.Builder()
                .url("https://newsapi.org/v2/everything?q=$term&source=$category&apiKey=$apiKey")
                .build()

        Log.d("key", "My url: https://newsapi.org/v2/everything?q=$term&source=$category&apiKey=$apiKey")
        // Actually makes the API call, blocking the thread until it completes
        val response = okHttpClient.newCall(request).execute()

        // Empty list of articles that we'll build up from the response
        val sources = mutableListOf<Source>()

        // Get the JSON string body, if there was one
        val responseString = response.body?.string()

        // Make sure the server responded successfully, and with some JSON data
        if (response.isSuccessful && !responseString.isNullOrEmpty()) {
            // Represents the JSON from the root level
            val json = JSONObject(responseString)

            // Grab the "articles" array from the root level
            val articles = json.getJSONArray("articles")

            // Loop over the sources
            for (i in 0 until articles.length()) {
                // Grab the current article
                val curr = articles.getJSONObject(i)

                val source = curr.getJSONObject("source").getString("name")


                // Get the title of the article
                val title = curr.getString("title")

                // Get the description
                val description = curr.getString("description")

                val url = curr.getString("url")

                // TODO: Get the thumbnail on check-in 3
                val urlImage = curr.getString("urlToImage")
                sources.add(
                        Source(
                                username = title,
                                content = description,
                                source = source,
                                url = url,
                                term = intent.getStringExtra("TERM")!!,
                                iconUrl = urlImage
                        )
                )
            }
        }
        return sources
    }
}