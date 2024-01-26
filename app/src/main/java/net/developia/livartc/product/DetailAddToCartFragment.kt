package net.developia.livartc.product

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.bumptech.glide.Glide
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import net.developia.livartc.databinding.FragmentDetailAddToCartBinding
import net.developia.livartc.db.AppDatabase
import net.developia.livartc.db.CartDao
import net.developia.livartc.db.CartEntity
import net.developia.livartc.model.Product
import java.text.NumberFormat
import java.util.Locale

/**
 * LIVARTC
 * Created by 변형준
 * Date: 1/21/24
 * Time: 17:12
 */
class DetailAddToCartFragment : BottomSheetDialogFragment() {

    lateinit var binding : FragmentDetailAddToCartBinding
    lateinit var db : AppDatabase
    lateinit var cartDao: CartDao

    var productCnt = 1

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentDetailAddToCartBinding.inflate(inflater, container, false)
        return binding.root
    }

    private fun insertCart(product: Product) {
        Thread {
            val existingCartEntity = cartDao.getCartEntity(product.productId)

            if (existingCartEntity != null) {
                // 장바구니에 이미 있는 상품일 경우, 수량을 증가시킵니다.
                existingCartEntity.product_cnt = existingCartEntity.product_cnt?.plus(productCnt)
                cartDao.update(existingCartEntity)
            } else {
                // 장바구니에 없는 상품일 경우, 새로운 CartEntity를 삽입합니다.
                cartDao.insert(
                    CartEntity(
                        product_id = product.productId,
                        name = product.productName,
                        price = product.productPrice,
                        product_cnt = productCnt,
                        image = product.productImage
                    )
                )
            }

            requireActivity().runOnUiThread {
                Toast.makeText(requireContext(), "장바구니에 추가되었습니다.", Toast.LENGTH_SHORT).show()
                dismiss()
            }
        }.start()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val product = arguments?.getSerializable("product") as? Product
        db = AppDatabase.getInstance(requireContext())!!
        cartDao = db.getCartDao()

        product?.let { product ->
            binding.cartProductName.text = product.productName
            val numberFormat = NumberFormat.getNumberInstance(Locale.US)
            val formattedPrice = numberFormat.format(product.productPrice)
            binding.cartProductPrice.text = formattedPrice + "원"
            binding.cartProductCnt.text = productCnt.toString()
            Glide.with(requireContext())
                .load(product.productImage)
                .into(binding.cartProductImage)
            binding.addToCartBtn.setOnClickListener {
                // 장바구니에 추가
                insertCart(product)
            }
        }

        binding.cartProductPlus.setOnClickListener {
            // 수량 증가
            productCnt++
            binding.cartProductCnt.text = productCnt.toString()
        }
        binding.cartProductMinus.setOnClickListener {
            // 수량 감소 (0 이하로 내려가지 않음)
            if (productCnt > 1) {
                productCnt--
                binding.cartProductCnt.text = productCnt.toString()
            }
        }
    }
}