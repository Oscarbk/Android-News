package com.example.androidnews

import android.content.Intent
import android.net.Uri
import android.util.DisplayMetrics
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso


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
        holder.content.text = currentSource.content
        holder.source.text = currentSource.source
        holder.username.text = currentSource.username

        if (holder.username.text == "null") holder.username.visibility = View.GONE
        if (holder.content.text == "null") holder.content.visibility = View.GONE
        if (holder.source.text == "null") holder.source.visibility = View.GONE



        //holder.url.text = currentSource.url
        if (!currentSource.iconUrl.isNullOrBlank())
        {
            Picasso.get()
                .load(currentSource.iconUrl)
                .resize(0, 1024)
                .onlyScaleDown()
                .into(holder.icon)



        }
        else holder.icon.visibility = View.GONE

        val test = currentSource.url
        holder.click.setOnClickListener(){//holder.url.setOnClickListener{
            Log.d("BUTTON", "Was URL passed: ${currentSource.iconUrl}")
            // TODO: come back to this



            val url = test
            if (url != "goToResults") {
                val intent = Intent(Intent.ACTION_VIEW)
                intent.data = Uri.parse(url)
                holder.url.getContext().startActivity(intent)
            }
            else {
                val intent = Intent(holder.url.getContext(), ResultsActivity::class.java)
                intent.putExtra("SOURCE", holder.username.text)
                intent.putExtra("TERM", currentSource.term)
                intent.putExtra("SOURCEID", currentSource.source)
                holder.url.getContext().startActivity(intent)
            }
        }
    }
    override fun getItemCount(): Int {
        // Return number of (total) rows to render
        return sources.size
    }

    class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        val username: TextView = itemView.findViewById(R.id.username)
        val content: TextView = itemView.findViewById(R.id.tweet_content)
        val source: TextView = itemView.findViewById(R.id.source)
        val url: TextView = itemView.findViewById(R.id.url)
        //val url: Button = itemView.findViewById(R.id.url)
        val click: ConstraintLayout = itemView.findViewById(R.id.card_view_layout)
        val icon: ImageView = itemView.findViewById(R.id.image)
    }

}