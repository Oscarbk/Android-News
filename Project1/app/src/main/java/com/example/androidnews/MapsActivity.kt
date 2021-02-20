package com.example.androidnews

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions

import android.location.Address
import android.location.Geocoder
import android.util.Log
import android.widget.ArrayAdapter
import android.widget.HorizontalScrollView
import android.widget.Spinner
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import org.jetbrains.anko.doAsync
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.logging.HttpLoggingInterceptor
import org.json.JSONObject

class MapsActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap
    private lateinit var recyclerView: RecyclerView

    // OkHttp is a library used to make network calls
  //  val okHttpClient: OkHttpClient = OkHttpClient.Builder().build()
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
        setContentView(R.layout.activity_maps)
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)

        recyclerView = findViewById(R.id.recyclerView)
        /*val sources = getFakeSources()
        val adapter = SourcesAdapter(sources)

        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(this)*/
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap

        mMap.setOnMapLongClickListener { coords: LatLng ->
            mMap.clear()

            doAsync {
                // Geocoding should be done on a background thread - it involves networking
                // and has the potential to cause the app to freeze (Application Not Responding error)
                // if done on the UI Thread and it takes too long.
                val geocoder: Geocoder = Geocoder(this@MapsActivity)

                // In Kotlin, you can assign the result of a try-catch block. Both the "try" and
                // "catch" clauses need to yield a valid value to assign.
                val results: List<Address> = try {
                    geocoder.getFromLocation(
                        coords.latitude,
                        coords.longitude,
                        10
                    )
                } catch (e: Exception) {
                    Log.e("MapsActivity", "Geocoder failed", e)
                    listOf<Address>()
                }

                // Move back to the UI Thread now that we have some results to show.
                // The UI can only be updated from the UI Thread.
                runOnUiThread {
                    if (results.isNotEmpty()) {
                        // Potentially, we could show all results to the user to choose from,
                        // but for our usage it's sufficient enough to just use the first result
                        val firstResult = results.first()
                        val postalAddress = firstResult.getAddressLine(0)

                        setTitle("Search for ${firstResult.adminArea}")

                        val toast = Toast.makeText(
                            this@MapsActivity,
                            "You clicked: $postalAddress",
                            Toast.LENGTH_LONG
                        )

                        // Network call needs to be on another thread
                        doAsync {
                            val sources = retrieveSources(firstResult.adminArea)
                            runOnUiThread {
                                val adapter = sources?.let { SourcesAdapter(it) }
                                recyclerView.adapter = adapter
                                recyclerView.layoutManager = LinearLayoutManager(this@MapsActivity, LinearLayoutManager.HORIZONTAL, false)

                            }
                        }
                        toast.show()

                        // Add a map marker where the user tapped and pan the camera over
                        mMap.addMarker(MarkerOptions().position(coords).title(postalAddress))
                        mMap.moveCamera(CameraUpdateFactory.newLatLng(coords))
                    } else {
                        Log.d("MapsActivity", "No results from geocoder!")
                        val toast = Toast.makeText(
                            this@MapsActivity,
                            "No results for location!",
                            Toast.LENGTH_LONG
                        )
                        toast.show()
                    }
                }
            }
        }
    }
    /*fun getFakeSources(): List<Source> {
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
    }*/

    // This function must be called from a background thread since it will be doing some networking
    fun retrieveSources(location: String): List<Source>?
    {
        // Building the request
        val request = Request.Builder()
                .url("https://newsapi.org/v2/everything?q=$location&sortBy=popularity&apiKey=bdc116a1bab1437fbd328fe64fe80558")
                .build()
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

            // Loop over the articles
            for (i in 0 until articles.length()) {
                // Grab the current article
                val curr = articles.getJSONObject(i)

                // Get the title of the article
                val title = curr.getString("title")

                // Get the source
                val source = curr.getJSONObject("source").getString("name")

                // Get the description
                val description = curr.getString("description")

                // TODO: Get the thumbnail on check-in 3

                sources.add(
                    Source(
                        username = title,
                        content = description,
                        source = source,
                    )
                )
            }
        }
        return sources
    }
}