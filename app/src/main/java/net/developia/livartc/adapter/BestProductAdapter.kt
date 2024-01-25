package net.developia.livartc.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import net.developia.livartc.databinding.ItemBestBinding
import net.developia.livartc.main.HomeFragment
import net.developia.livartc.model.Product

/**
 * LIVARTC
 * Created by 최현서
 * Date: 2024-01-22
 * Time: 오후 12:54
 */
class BestProductAdapter(private val bestList : List<Product>, val context: HomeFragment)
    : RecyclerView.Adapter<BestProductAdapter.BestProductViewHolder>() {
    class BestProductViewHolder(val binding: ItemBestBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BestProductViewHolder {
        val binding: ItemBestBinding = ItemBestBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return BestProductViewHolder(binding)
    }

    override fun onBindViewHolder(holder: BestProductViewHolder, position: Int) {
        val bestData = bestList[position]

        holder.binding.productName.text = bestData.productName
        holder.binding.productPrice.text = "￦ ${bestData.productPrice}"
        Glide.with(holder.binding.productImage.context)
            .load(bestData.productImage)
            .into(holder.binding.productImage)
    }

    override fun getItemCount(): Int {
        return bestList.size
    }
}