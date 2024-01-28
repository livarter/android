package net.developia.livartc.adapter

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import net.developia.livartc.databinding.ItemBestBinding
import net.developia.livartc.main.HomeFragment
import net.developia.livartc.model.Product
import java.text.NumberFormat
import java.util.Locale

/**
 * LIVARTC
 * Created by 최현서
 * Date: 2024-01-22
 * Time: 오후 12:54
 */
class BestProductAdapter(private val bestList : List<Product>, private val clickListener: (Product) -> Unit)
    : RecyclerView.Adapter<BestProductAdapter.BestProductViewHolder>() {
    class BestProductViewHolder(val binding: ItemBestBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BestProductViewHolder {
        val binding: ItemBestBinding = ItemBestBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return BestProductViewHolder(binding)
    }

    override fun onBindViewHolder(holder: BestProductViewHolder, position: Int) {
        val bestData = bestList[position]

        holder.binding.productName.text = bestData.productName
        val numberFormat = NumberFormat.getNumberInstance(Locale.US)
        val formattedPrice = numberFormat.format(bestData.productPrice)
        holder.binding.productPrice.text = "${formattedPrice} 원"
        Glide.with(holder.binding.productImage.context)
            .load(bestData.productImage)
            .into(holder.binding.productImage)
        holder.binding.root.setOnClickListener{
            clickListener(bestData)
        }
    }

    override fun getItemCount(): Int {
        return bestList.size
    }
}