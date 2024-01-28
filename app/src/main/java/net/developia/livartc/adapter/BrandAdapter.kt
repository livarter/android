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
 * Time: 4:33 PM
 */

class BrandAdapter(
    private val brands: List<String>,
    private val onClick: (String?) -> Unit
) : RecyclerView.Adapter<BrandAdapter.BrandViewHolder>() {

    var selectedPosition = RecyclerView.NO_POSITION

    class BrandViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val brandText: TextView = view.findViewById(R.id.brandText)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BrandViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.brand_name, parent, false)
        return BrandViewHolder(view)
    }

    @SuppressLint("NotifyDataSetChanged")
    override fun onBindViewHolder(holder: BrandViewHolder, @SuppressLint("RecyclerView") position: Int) {
        val brand = brands[position]
        holder.brandText.text = brand

        if (position == selectedPosition) {
            holder.brandText.setTextColor(Color.parseColor("#000000")) // 선택된 해시태그 색상
            holder.brandText.setTypeface(null, Typeface.BOLD) // Bold 스타일 적용
        } else {
            holder.brandText.setTextColor(Color.parseColor("#495057")) // 기본 해시태그 색상
            holder.brandText.setTypeface(null, Typeface.NORMAL) // 기본 스타일 적용
        }

        holder.itemView.setOnClickListener {
            if (selectedPosition == position) {
                selectedPosition = RecyclerView.NO_POSITION
                onClick("") // 선택 해제
            } else {
                selectedPosition = position
                onClick(brand)
            }
            notifyDataSetChanged()
        }
    }

    override fun getItemCount(): Int = brands.size
}

