package net.developia.livartc.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import net.developia.livartc.databinding.ItemPurchaseBinding
import net.developia.livartc.model.PurchaseHistory

/**
 * LIVARTC
 * Created by 변형준
 * Date: 1/19/24
 * Time: 22:54
 */

class PurchaseAdapter(
    private val purchaseList: List<PurchaseHistory>,
) : RecyclerView.Adapter<PurchaseAdapter.MyViewHolder>() {
    inner class MyViewHolder(binding: ItemPurchaseBinding) :
        RecyclerView.ViewHolder(binding.root) {
            val product_name = binding.productName
            val product_price = binding.productPrice
            val product_cnt = binding.productCnt
            val product_image = binding.productImage
            val created_at = binding.createdAt

            val root = binding.root
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val binding : ItemPurchaseBinding = ItemPurchaseBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return purchaseList.size
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val purchaseData = purchaseList[position]
        holder.product_name.text = purchaseData.productName
        holder.product_price.text = purchaseData.productPrice.toString()
        holder.product_cnt.text = purchaseData.productCnt.toString()
        holder.created_at.text = purchaseData.createdAt
//        purchaseData.productImage?.let {
//            holder.product_image.setImageResource(it.toInt())
//        }
    }

}