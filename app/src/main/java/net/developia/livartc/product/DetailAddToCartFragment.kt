package net.developia.livartc.product

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import net.developia.livartc.databinding.FragmentDetailAddToCartBinding
import net.developia.livartc.databinding.FragmentDetailBinding
import net.developia.livartc.db.AppDatabase
import net.developia.livartc.db.CartDao
import net.developia.livartc.db.CartEntity

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

    var product_cnt = 1

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentDetailAddToCartBinding.inflate(inflater, container, false)

        db = AppDatabase.getInstance(requireContext())!!
        cartDao = db.getCartDao()

        binding.addToCartBtn.setOnClickListener {
            // 장바구니에 추가
            insertCart()
        }

        binding.cartProductPlus.setOnClickListener {
            // 수량 증가
            product_cnt++
            binding.cartProductCnt.text = product_cnt.toString()
        }

        binding.cartProductMinus.setOnClickListener {
            // 수량 감소 (0 이하로 내려가지 않음)
            if (product_cnt > 1) {
                product_cnt--
                binding.cartProductCnt.text = product_cnt.toString()
            }
        }


        return binding.root
    }

    private fun insertCart() {
        var product_id = arguments?.getInt("product_id")
        var name = arguments?.getString("name")
        var price = arguments?.getInt("price")
        var image = arguments?.getString("image")

        Thread {
            val existingCartEntity = cartDao.getCartEntity(product_id ?: 0)

            if (existingCartEntity != null) {
                // 장바구니에 이미 있는 상품일 경우, 수량을 증가시킵니다.
                existingCartEntity.product_cnt = existingCartEntity.product_cnt?.plus(product_cnt)
                cartDao.update(existingCartEntity)
            } else {
                // 장바구니에 없는 상품일 경우, 새로운 CartEntity를 삽입합니다.
                cartDao.insert(
                    CartEntity(
                        1,
                        product_id,
                        name,
                        price,
                        product_cnt,
                        image
                    )
                )
            }

            requireActivity().runOnUiThread {
                Toast.makeText(requireContext(), "장바구니에 추가되었습니다.", Toast.LENGTH_SHORT).show()
                requireActivity().finish()
            }
        }.start()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

    }
}