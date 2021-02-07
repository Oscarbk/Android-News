package com.example.androidnews

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView

class SourcesAdapter(val sources: List<Source>) : RecyclerView.Adapter<SourcesAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        // Need to render a new row -- inflate (load) the XML file and return a ViewHolder
        // Need to:
        // 1. Read in the XML file for the row type
        // 2. Use the new row to build a ViewHolder to return

        // Step 1
        val layoutInflater: LayoutInflater = LayoutInflater.from(parent.context)
        val itemView: View = layoutInflater.inflate(R.layout.row_source, parent, false)

        // Step 2
        return ViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        // Load data into a new row
        // The RecyclerView is ready to display a new (or recycled) row on the screen
        // for position indicated -- override the UI elements with the correct data
        val currentSource = sources[position]
        holder.username.text = currentSource.username
        holder.handle.text = currentSource.handle
        holder.content.text = currentSource.content

        // TODO -- load the image icon url
    }

    override fun getItemCount(): Int {
        // Return number of (total) rows to render
        return sources.size
    }

    class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        val username: TextView = itemView.findViewById(R.id.username)
        val handle: TextView = itemView.findViewById(R.id.handle)
        val content: TextView = itemView.findViewById(R.id.tweet_content)
        val icon: ImageView = itemView.findViewById(R.id.icon)
    }
}