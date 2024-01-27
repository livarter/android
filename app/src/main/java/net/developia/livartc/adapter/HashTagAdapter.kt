package net.developia.livartc.adapter

import android.annotation.SuppressLint
import android.graphics.Color
import android.graphics.Typeface
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
class HashTagAdapter(private val hashTags: List<String>,
                     private val onClick: (String) -> Unit) :
    RecyclerView.Adapter<HashTagAdapter.HashTagViewHolder>() {

    private var selectedPosition = RecyclerView.NO_POSITION


    class HashTagViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val hashTagText: TextView = view.findViewById(R.id.hashTagText)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HashTagViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.hashtag_name, parent, false)
        return HashTagViewHolder(view)
    }

    @SuppressLint("NotifyDataSetChanged")
    override fun onBindViewHolder(holder: HashTagViewHolder, @SuppressLint("RecyclerView") position: Int) {
        val hashTag = hashTags[position]
        holder.hashTagText.text = hashTag

        // 선택된 해시태그 스타일 변경
        if (position == selectedPosition) {
            holder.hashTagText.setTextColor(Color.parseColor("#000000")) // 선택된 해시태그 색상
            holder.hashTagText.setTypeface(null, Typeface.BOLD) // Bold 스타일 적용
        } else {
            holder.hashTagText.setTextColor(Color.parseColor("#495057")) // 기본 해시태그 색상
            holder.hashTagText.setTypeface(null, Typeface.NORMAL) // 기본 스타일 적용
        }

        holder.itemView.setOnClickListener {
            if(selectedPosition == position) {
                selectedPosition = RecyclerView.NO_POSITION
                onClick("")
            } else {
                selectedPosition = position
                onClick(hashTag)
            }
            notifyDataSetChanged()
        }
    }


    override fun getItemCount() = hashTags.size
}