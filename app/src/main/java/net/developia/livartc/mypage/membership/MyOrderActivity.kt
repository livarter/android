package net.developia.livartc.mypage.membership

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.View
import androidx.core.content.ContextCompat.startActivity
import androidx.recyclerview.widget.LinearLayoutManager
import net.developia.livartc.R
import net.developia.livartc.ReplyWriteActivity
import net.developia.livartc.adapter.PurchaseAdapter
import net.developia.livartc.databinding.ActivityMyOrderBinding
import net.developia.livartc.login.TokenManager
import net.developia.livartc.model.Product
import net.developia.livartc.model.PurchaseHistory
import net.developia.livartc.product.DetailFragment
import net.developia.livartc.retrofit.MyApplication
import net.developia.livartc.retrofit.RetrofitInstance
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.Collections

/**
 * LIVARTC
 * Created by 변형준
 * Date: 1/19/24
 * 작업내용: 주문내역 리사이클러뷰 조회
 */
class MyOrderActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMyOrderBinding
    private lateinit var purchaseList: List<PurchaseHistory>
    private lateinit var purchaseAdapter: PurchaseAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMyOrderBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.backButton.setOnClickListener {
            onBackPressed()
        }
        getAllPurchaseList()
    }

    private fun getAllPurchaseList() {
        val jwtToken = TokenManager.getToken(MyApplication.instance)!!
        RetrofitInstance.api.getPurchaseHistory(jwtToken)
            .enqueue(object : Callback<List<PurchaseHistory>> {
                override fun onResponse(
                    call: Call<List<PurchaseHistory>>,
                    response: Response<List<PurchaseHistory>>
                ) {
                    if (response.isSuccessful) {
                        purchaseList = response.body() ?: emptyList()
                        Log.d("MyOrderActivity:purchaseList", purchaseList.toString())
                        Handler(Looper.getMainLooper()).postDelayed({
                            binding.progressBar.visibility = View.GONE
                            setRecyclerView()
                        }, 1000)
                    }
                }

                override fun onFailure(call: retrofit2.Call<List<PurchaseHistory>>, t: Throwable) {
                    t.printStackTrace()
                }
            })
    }

    private fun setRecyclerView() {
        runOnUiThread {
            if (purchaseList.isEmpty()) binding.emptyMsg.visibility = View.VISIBLE
            else {
                binding.purchaseListView.adapter = PurchaseAdapter(purchaseList) { purchase ->
                    showReplyWrite(purchase)
                }
                binding.purchaseListView.layoutManager = LinearLayoutManager(this)
            }
        }
    }

    private fun showReplyWrite(purchaseProduct: PurchaseHistory) {
        val replyIntent = Intent(this, ReplyWriteActivity::class.java)
        replyIntent.putExtra("productImage", purchaseProduct.productImage)
        replyIntent.putExtra("productName", purchaseProduct.productName)
        replyIntent.putExtra("brandName", purchaseProduct.productBrand)
        replyIntent.putExtra("productId", purchaseProduct.productId.toLong())
        replyIntent.putExtra("productDesc", purchaseProduct.productDesc)
        replyIntent.putExtra("productPrice", purchaseProduct.productPrice)
        startActivity(replyIntent)
    }

    override fun onResume() {
        super.onResume()
        getAllPurchaseList()
    }
}
