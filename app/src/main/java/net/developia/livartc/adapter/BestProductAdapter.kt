package net.developia.livartc.adapter

import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition
import net.developia.livartc.databinding.ItemBestBinding
import net.developia.livartc.main.HomeFragment
import net.developia.livartc.model.BestProduct
import net.developia.livartc.model.Product

/**
 * LIVARTC
 * Created by 최현서
 * Date: 2024-01-22
 * Time: 오후 12:54
 */
class BestProductAdapter(private val bestList : List<Product>?, val context: HomeFragment)
    : RecyclerView.Adapter<BestProductAdapter.BestProductViewHolder>() {
    inner class BestProductViewHolder(binding: ItemBestBinding) : RecyclerView.ViewHolder(binding.root) {
        val nameView = binding.bestProductName
        val priceView = binding.bestProductPrice
        val photoView = binding.wearImageView
        val root = binding.root
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BestProductViewHolder {
        val binding: ItemBestBinding = ItemBestBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return BestProductViewHolder(binding)
    }

    override fun onBindViewHolder(holder: BestProductViewHolder, position: Int) {
        val bestData = bestList!![position]

        holder.nameView.text = bestData.productName
        holder.priceView.text = "￦ ${bestData.productPrice}"
        Glide.with(context)
            .asBitmap()
            .load(bestData.productImage)
            .into(object : CustomTarget<Bitmap>(2000, 2000) {
                override fun onResourceReady(
                    resource: Bitmap,
                    transition: Transition<in Bitmap>?
                ) {
                    holder.photoView.setImageBitmap(resource)
                }

                override fun onLoadCleared(placeholder: Drawable?) {
                    TODO("Not yet implemented")
                }
            })
    }

    override fun getItemCount(): Int {
        // 리사이클러뷰 아이템 개수는 할일 리스트 크기
        return bestList?.size?:0
    }


}