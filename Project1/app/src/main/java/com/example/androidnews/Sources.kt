package com.example.androidnews
import android.widget.Button
import okhttp3.EventListener

data class Source(
    val username: String,
    val content: String,
    val source: String,
    val url: String,
)