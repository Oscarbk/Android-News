package com.example.androidnews

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.Button

import android.content.Intent
import android.util.Log
import android.widget.EditText
import android.widget.ProgressBar

class MainActivity : AppCompatActivity() {

    private lateinit var search: Button
    private lateinit var searchBar: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        search = findViewById(R.id.search)
        searchBar = findViewById(R.id.searchBar)

        search.setEnabled(false)
        search.setOnClickListener {
            startActivity(intent)
        }
        searchBar.addTextChangedListener(TextWatcher)
    }
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