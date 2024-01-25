package net.developia.livartc.main

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import net.developia.livartc.MainActivity
import net.developia.livartc.PurchaseActivity
import net.developia.livartc.adapter.CartRecyclerViewAdapter
import net.developia.livartc.databinding.FragmentCartBinding
import net.developia.livartc.db.AppDatabase
import net.developia.livartc.db.CartEntity
import net.developia.livartc.db.CartDao


/**
 * LIVARTC
 * Created by 변형준
 * Date: 1/19/24
 * Time: 17:21
 */
class CartFragment : Fragment() {
    lateinit var binding: FragmentCartBinding

    private lateinit var db :AppDatabase
    private lateinit var cartDao: CartDao
    private lateinit var cartList : ArrayList<CartEntity>
    private lateinit var cartAdapter : CartRecyclerViewAdapter

    private var totalPrice = 0

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentCartBinding.inflate(inflater, container, false)

        db = AppDatabase.getInstance(requireContext())!!
        cartDao = db.getCartDao()

        getAllCartList()

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.purchaseBtn.setOnClickListener {
            val intent = Intent(activity, PurchaseActivity::class.java)
            intent.putExtra("totalPrice", totalPrice)
            startActivity(intent)
        }
    }

    private fun getAllCartList() {
        Thread {
            cartList = ArrayList(cartDao.getAll())
            totalPrice = 0
            for (cart in cartList) {
                val price = cart.price ?: 0
                val productCnt = cart.product_cnt ?: 0
                totalPrice += price * productCnt
            }
            requireActivity().runOnUiThread {
                if (cartList.isEmpty()) {
                    // 장바구니가 비어 있는 경우
                    binding.emptyCartView.visibility = View.VISIBLE
                    binding.cartView.visibility = View.GONE
                } else {
                    // 장바구니에 상품이 있는 경우
                    binding.emptyCartView.visibility = View.GONE
                    binding.cartView.visibility = View.VISIBLE

                    setTotalPrice(totalPrice)
                    setRecyclerView()
                }
            }


            setRecyclerView()
            setTotalPrice(totalPrice)
        }.start()
    }

    private fun setTotalPrice(totalPrice: Int) {
        requireActivity().runOnUiThread {
            binding.originPrice.text = "₩$totalPrice"
            binding.totalPrice.text = "₩$totalPrice" // 총 결제 금액을 화면에 표시
        }
    }

    private fun setRecyclerView() {
        requireActivity().runOnUiThread {
            cartAdapter = CartRecyclerViewAdapter(cartList, { cartEntity ->
                deleteCart(cartEntity) // 아이템 삭제
            }, { cartEntity ->
                updateCart(cartEntity) // 아이템 업데이트
            })
            binding.cartRecyclerView.adapter = cartAdapter
            binding.cartRecyclerView.layoutManager = LinearLayoutManager(requireContext())
        }
    }
    private fun deleteCart(cartEntity: CartEntity) {
        Thread {
            cartDao.delete(cartEntity) // DB에서 아이템 삭제
            getAllCartList() // 장바구니 목록 갱신
        }.start()
    }
    private fun updateCart(cartEntity: CartEntity) {
        Thread {
            cartDao.update(cartEntity) // DB에서 아이템 업데이트
            getAllCartList() // 장바구니 목록 갱신
        }.start()
    }
    override fun onResume() {
        super.onResume()
        getAllCartList()
    }


}