package net.developia.livartc.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import net.developia.livartc.R

/**
 * LIVARTC
 * Created by 오수영
 * Date: 1/24/24
 * Time: 5:22 PM
 */
class HashTagAdapter(private val hashTags: List<String>) :
    RecyclerView.Adapter<HashTagAdapter.HashTagViewHolder>() {

    class HashTagViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val hashTagText: TextView = view.findViewById(R.id.hashTagText)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HashTagViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.hashtag_name, parent, false)
        return HashTagViewHolder(view)
    }

    override fun onBindViewHolder(holder: HashTagViewHolder, position: Int) {
        holder.hashTagText.text = hashTags[position]
    }

    override fun getItemCount() = hashTags.size
}