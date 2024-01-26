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
    private val onClick: (String) -> Unit) :
    RecyclerView.Adapter<BrandAdapter.BrandViewHolder>() {

    private var selectedPosition = RecyclerView.NO_POSITION

    class BrandViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val brandText: TextView = view.findViewById(R.id.brandText)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BrandViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.brand_name, parent, false)
        return BrandViewHolder(view)
    }

    override fun onBindViewHolder(holder: BrandViewHolder, @SuppressLint("RecyclerView") position: Int) {
        val brand = brands[position]
        holder.brandText.text = brand

        // 선택된 아이템 강조
        if (position == selectedPosition) {
            holder.brandText.setTextColor(Color.parseColor("#000000"))
            holder.brandText.setTypeface(null, Typeface.BOLD)
        } else {
            holder.brandText.setTextColor(Color.parseColor("#495057"))
            holder.brandText.setTypeface(null, Typeface.NORMAL)
        }

        holder.itemView.setOnClickListener {
            if(selectedPosition == position) {
                selectedPosition = RecyclerView.NO_POSITION
                onClick("")
            } else {
                selectedPosition = position
                onClick(brand)
            }
            notifyDataSetChanged()
        }
    }

    override fun getItemCount() = brands.size
}