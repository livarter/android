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
 * Time: 4:33 PM
 */

class BrandAdapter(private val brands: List<String>) :
    RecyclerView.Adapter<BrandAdapter.BrandViewHolder>() {

    class BrandViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val brandText: TextView = view.findViewById(R.id.brandText)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BrandViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.brand_name, parent, false)
        return BrandViewHolder(view)
    }

    override fun onBindViewHolder(holder: BrandViewHolder, position: Int) {
        holder.brandText.text = brands[position]
    }

    override fun getItemCount() = brands.size
}