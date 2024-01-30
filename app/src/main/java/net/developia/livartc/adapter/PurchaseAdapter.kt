package net.developia.livartc.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import net.developia.livartc.databinding.ItemPurchaseBinding
import net.developia.livartc.model.Product
import net.developia.livartc.model.PurchaseHistory
import java.text.NumberFormat
import java.util.Locale

/**
 * LIVARTC
 * Created by 변형준
 * Date: 1/19/24
 * 작업 내용: 구매내역 목록 조회
 */

class PurchaseAdapter(
    private val purchaseList: List<PurchaseHistory>,
    private val onItemClicked: (PurchaseHistory) -> Unit
) : RecyclerView.Adapter<PurchaseAdapter.MyViewHolder>() {
    inner class MyViewHolder(binding: ItemPurchaseBinding) :
        RecyclerView.ViewHolder(binding.root) {
            val productName = binding.productName
            val productPrice = binding.productPrice
            val productCnt = binding.productCnt
            val productImage = binding.productImage
            val createdAt = binding.createdAt

            val root = binding.root
            val replyWriteBtn = binding.replyWriteBtn
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
        holder.productName.text = purchaseData.productName
        val numberFormat = NumberFormat.getNumberInstance(Locale.US)
        val formattedPrice = numberFormat.format(purchaseData.productPrice)

        holder.productPrice.text = formattedPrice + "원"
        holder.productCnt.text = purchaseData.productCnt.toString()
        holder.createdAt.text = purchaseData.createdAt
        purchaseData.productImage?.let {
            Glide.with(holder.itemView.context)
                .load(it)
                .into(holder.productImage)
        }
        holder.replyWriteBtn.setOnClickListener {
            onItemClicked(purchaseData)
        }
    }

}