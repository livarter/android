package net.developia.livartc.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import net.developia.livartc.databinding.ItemHashTagBinding
import net.developia.livartc.model.Product
import kotlin.random.Random

/**
 * LIVARTC
 * Created by 오수영
 * Date: 1/29/24
 * Time: 3:44 AM
 */
class StraggeredAdapter(private val hashTags: List<String>, private val productsMap: Map<String, List<Product>>) : RecyclerView.Adapter<StraggeredAdapter.ViewHolder>() {


    class ViewHolder(val binding: ItemHashTagBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(hashTag: String, product: Product) {
            binding.hashTagText.text = hashTag

            Glide.with(binding.hashtageImageView.context)
                .load(product.productImage) // 상품 이미지 URL을 로드
                .into(binding.hashtageImageView)

            val layoutParams = binding.hashtageImageView.layoutParams as FrameLayout.LayoutParams
            layoutParams.height = getRandomImageHeight() // 이미지 높이를 랜덤하게 설정
            binding.hashtageImageView.layoutParams = layoutParams
        }

        private fun getRandomImageHeight(): Int {
            val heights = listOf(300, 400, 500) // 랜덤 높이 값들
            return heights[Random.nextInt(heights.size)]
        }
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemHashTagBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val hashTag = hashTags[position]
        Log.d("StraggeredAdapter", "onBindViewHolder: 해시태그 = $hashTag")

        val products = productsMap[hashTag]
        if (!products.isNullOrEmpty()) {
            val product = products[Random.nextInt(products.size)]
            Log.d("StraggeredAdapter", "제품 바인딩: ${product.productName}")

            holder.bind(hashTag, product)
        }else {
            Log.d("StraggeredAdapter", "해시태그 '$hashTag'에 대한 제품 없음")
        }
    }

    override fun getItemCount(): Int = hashTags.size
}
