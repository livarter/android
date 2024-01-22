package net.developia.livartc.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import net.developia.livartc.databinding.ItemCartBinding
import net.developia.livartc.db.CartEntity

/**
 * LIVARTC
 * Created by 변형준
 * Date: 1/19/24
 * Time: 22:54
 */

class CartRecyclerViewAdapter(private val cartList : ArrayList<CartEntity>) : RecyclerView.Adapter<CartRecyclerViewAdapter.MyViewHolder>() {
    inner class MyViewHolder(binding : ItemCartBinding) :
        RecyclerView.ViewHolder(binding.root) {
            val cart_product_name = binding.cartProductName
            val cart_product_price = binding.cartProductPrice
            val cart_product_cnt = binding.cartProductCnt
            val cart_product_image = binding.cartProductImage

            val root = binding.root
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val binding : ItemCartBinding = ItemCartBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return cartList.size
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val cartData = cartList[position]
        holder.cart_product_name.text = cartData.name
        holder.cart_product_price.text = cartData.price.toString()
        holder.cart_product_cnt.text = cartData.product_cnt.toString()
        cartData.image?.let {
            holder.cart_product_image.setImageResource(it.toInt())
        }
    }

}