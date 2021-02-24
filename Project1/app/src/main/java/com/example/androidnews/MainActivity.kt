package com.example.androidnews

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.Button

import android.content.Intent
import android.widget.EditText

class MainActivity : AppCompatActivity() {

    private lateinit var search: Button
    private lateinit var searchBar: EditText
    private lateinit var maps:  Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Get buttons on main activity
        search = findViewById(R.id.search)
        searchBar = findViewById(R.id.searchBar)
        maps = findViewById(R.id.viewMap)
        search.setEnabled(false)

        // Go to sources activity
        search.setOnClickListener {
            val intent = Intent(this, SourceActivity::class.java)
            intent.putExtra("TERM", searchBar.getText().toString())
            startActivity(intent)
        }
        searchBar.addTextChangedListener(TextWatcher)

        // Go to maps activity
        maps.setOnClickListener {
            val intent = Intent(this, MapsActivity:: class.java)
            startActivity(intent)
        }
    }
    // Detect when search bar has input
    private val TextWatcher: TextWatcher = object : TextWatcher {
        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
        }
        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            val inputtedSearch = searchBar.getText().toString()
            val enabledButton = inputtedSearch.isNotEmpty()
            search.setEnabled(enabledButton)
        }
        override fun afterTextChanged(s: Editable?) {
        }

    }
}