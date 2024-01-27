package net.developia.livartc.mypage.membership

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import net.developia.livartc.adapter.PurchaseAdapter
import net.developia.livartc.databinding.ActivityMyOrderBinding
import net.developia.livartc.login.TokenManager
import net.developia.livartc.model.PurchaseHistory
import net.developia.livartc.retrofit.MyApplication
import net.developia.livartc.retrofit.RetrofitInstance
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

/**
 * LIVARTC
 * Created by 변형준
 * Date: 1/19/24
 * 작업내용: 주문내역 리사이클러뷰 조회
 */
class MyOrderActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMyOrderBinding
    private lateinit var purchaseList : List<PurchaseHistory>
    private lateinit var purchaseAdapter : PurchaseAdapter

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
                        purchaseList = response.body()!!
                        setRecyclerView()
                    }
                }
                override fun onFailure(call: retrofit2.Call<List<PurchaseHistory>>, t: Throwable) {
                    t.printStackTrace()
                }
            })
    }

    private fun setRecyclerView() {
        runOnUiThread {
            purchaseAdapter = PurchaseAdapter(purchaseList)
            binding.purchaseListView.adapter = purchaseAdapter
            binding.purchaseListView.layoutManager = LinearLayoutManager(this)
        }
    }

    override fun onResume() {
        super.onResume()
        getAllPurchaseList()
    }
}
