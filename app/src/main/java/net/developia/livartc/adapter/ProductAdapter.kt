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
 * Function: 제품 목록을 보여주기 위한 RecyclerView 어댑터 구현
 */
// 제품 데이터를 표시하기 위한 RecyclerView 어댑터 클래스
class ProductAdapter(
    private val products: List<Product>,            // 표시할 제품 목록
    private val onItemClicked: (Product) -> Unit    // 아이템 클릭 시 호출될 콜백 함수
    ) :
    RecyclerView.Adapter<ProductAdapter.ProductViewHolder>() {

    // 제품 정보를 표시할 ViewHolder 클래스
    class ProductViewHolder(val binding: FragmentSearchItemsBinding) : RecyclerView.ViewHolder(binding.root)

    // ViewHolder 생성 시 호출, 레이아웃을 Infalte하여 ViewHodler을 생성한다.
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductViewHolder {
        val binding = FragmentSearchItemsBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ProductViewHolder(binding)
    }

    // ViewHolder와 데이터를 바인딩할 때 호출됨. 각 아이템의 내용을 설정
    override fun onBindViewHolder(holder: ProductViewHolder, position: Int) {
        val product = products[position]    // 현재 위치의 제품
        holder.binding.productName.text = product.productName   // 제품 이름 설정
        holder.binding.productPrice.text = "${String.format("%,d 원", product.productPrice)}" // 가격 포멧팅


        // 이미지 로딩은 Glide 라이브러리를 사용
        Glide.with(holder.binding.productImage.context)
            .load(product.productImage) // 제품 이미지 로드
            .into(holder.binding.productImage)  // InageView에 이미지 설정

        // 아이템 클릭 이벤트 처리
        holder.itemView.setOnClickListener {
            onItemClicked(product)  // 클릭된 ㅈ베품에 대한 콜백 함수 호출
        }
    }

    // 전체 아이템 개수 반환
    override fun getItemCount() = products.size
}