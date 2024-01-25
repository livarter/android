package net.developia.livartc.adapter

import android.annotation.SuppressLint
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
        holder.itemView.isSelected = position == selectedPosition

        holder.itemView.setOnClickListener {
            selectedPosition = position
            notifyDataSetChanged()
            onClick(brand)
        }
    }

    override fun getItemCount() = brands.size
}