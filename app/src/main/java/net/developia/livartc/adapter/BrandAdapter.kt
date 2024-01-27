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

    override fun onBindViewHolder(holder: BrandViewHolder, position: Int) {
        val brand = brands[position]
        holder.brandText.text = brand

        // 선택된 아이템에 대한 스타일 변경
        holder.brandText.setTextColor(if (position == selectedPosition) Color.parseColor("#000000") else Color.parseColor("#495057"))
        holder.brandText.setTypeface(null, if (position == selectedPosition) Typeface.BOLD else Typeface.NORMAL)

        holder.itemView.setOnClickListener {
            val previousSelectedPosition = selectedPosition
            if (selectedPosition == position) {
                selectedPosition = RecyclerView.NO_POSITION
                onClick(null) // 선택 해제
            } else {
                selectedPosition = position
                onClick(brand)
            }
            notifyItemChanged(previousSelectedPosition)
            notifyItemChanged(selectedPosition)
        }
    }

    override fun getItemCount(): Int = brands.size
}

