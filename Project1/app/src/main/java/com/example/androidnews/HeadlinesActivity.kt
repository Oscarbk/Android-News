package com.example.androidnews

import android.annotation.SuppressLint
import android.graphics.Color
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
import kotlin.math.max


class HeadlinesActivity : AppCompatActivity() {

    private var currentPage = 1
    private var maxPages = 0

    private lateinit var recyclerView: RecyclerView
    private lateinit var categories: Spinner
    private lateinit var prev: TextView
    private lateinit var next: TextView
    private lateinit var pageSomething: TextView

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

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_headlines)

        val intent = getIntent()
        //val term: String = intent.getStringExtra("TERM")!!
        setTitle("Top Headlines")

        recyclerView = findViewById(R.id.recyclerView)
        categories = findViewById(R.id.spinner)
        next = findViewById(R.id.next)
        prev = findViewById(R.id.prev)
        pageSomething = findViewById(R.id.currentPage)

        // TODO HERE: add parm to get articles function to select for categories
        Log.d("spin", "test")
        categories.setOnItemSelectedListener(object : OnItemSelectedListener {
            override fun onItemSelected(parentView: AdapterView<*>?, selectedItemView: View?, position: Int, id: Long) {
                // your code here
                currentPage = 1
                val text: String = categories.getSelectedItem().toString()
                Log.d("spin", text)
                displayPage(text, currentPage)
                // TODO: Fix colors
                if (currentPage == maxPages) {
                    next.setTextColor(Color.GRAY)
                }
                prev.setTextColor(Color.GRAY)
            }

            override fun onNothingSelected(parentView: AdapterView<*>?) {
                // your code here
                Log.d("spin", "2")
            }

        })
        displayPage("Business", currentPage)
        next.setTextColor(Color.BLUE)
        next.setOnClickListener {
            if (currentPage < maxPages) {
                currentPage++
                displayPage(categories.getSelectedItem().toString(), currentPage)
                if (currentPage == maxPages)
                    next.setTextColor(Color.GRAY)
                prev.setTextColor(Color.BLUE)
            }
            else
            {
                return@setOnClickListener
            }
            Log.d("page", "current page is $currentPage")
        }
        prev.setOnClickListener {
            if (currentPage > 1) {
                currentPage--
                displayPage(categories.getSelectedItem().toString(), currentPage)
                if (currentPage == 1)
                    // TODO: This can be done better
                    prev.setTextColor(Color.GRAY)
                next.setTextColor(Color.BLUE)
            }

            else
            {
                return@setOnClickListener
            }
            Log.d("page", "current page is $currentPage")
        }

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
    fun displayPage(category: String, page: Int)
    {
        doAsync {
            val sources = retrieveSources(category, page)
            runOnUiThread {
                val adapter = SourcesAdapter(sources)
                recyclerView.adapter = adapter
                recyclerView.layoutManager = LinearLayoutManager(this@HeadlinesActivity)
                val update = "$currentPage / $maxPages"
                pageSomething.text = update
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
    fun retrieveSources(category: String, page: Int): List<Source>
    {

        val apiKey = getString(R.string.api_key)

        // Building the request
        val request = Request.Builder()
                .url("https://newsapi.org/v2/top-headlines?country=US&category=$category&page=$page&apiKey=$apiKey")
                .build()

        Log.d("headlines", "My url: https://newsapi.org/v2/top-headlines?country=US&category=$category&apiKey=$apiKey")
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

            maxPages = json.getString("totalResults").toInt() / 20 + 1

            // Loop over the sources
            for (i in 0 until articles.length()) {
                // Grab the current article
                val curr = articles.getJSONObject(i)

                // Get the title of the article
                val title = curr.getString("title")

                // Get the description
                val description = curr.getString("description")

                val url = curr.getString("url")

                // TODO: Get the thumbnail on check-in 3
                val urlImage = curr.getString("urlToImage")

                // Get the source
                val source = curr.getJSONObject("source").getString("name")
                sources.add(
                        Source(
                                username = title,
                                content = description,
                                source = source,
                                url = url,
                                term = "",
                                iconUrl = urlImage
                        )
                )
            }
        }
        return sources
    }
}