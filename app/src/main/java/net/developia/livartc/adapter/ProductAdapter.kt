package net.developia.livartc.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import net.developia.livartc.databinding.FragmentSearchItemsBinding
import net.developia.livartc.model.Product

/**
 * LIVARTC
 * Created by 오수영
 * Date: 1/24/24
 * Time: 2:44 AM
 */
class ProductAdapter(private val products: List<Product>) :
    RecyclerView.Adapter<ProductAdapter.ProductViewHolder>() {

    class ProductViewHolder(val binding: FragmentSearchItemsBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductViewHolder {
        val binding = FragmentSearchItemsBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ProductViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ProductViewHolder, position: Int) {
        val product = products[position]
        holder.binding.productName.text = product.productName
        holder.binding.productPrice.text = "${product.productPrice}"

        // 이미지 로딩은 Glide 라이브러리를 사용
        Glide.with(holder.binding.productImage.context)
            .load(product.productImage)
            .into(holder.binding.productImage)
    }

    override fun getItemCount() = products.size
}