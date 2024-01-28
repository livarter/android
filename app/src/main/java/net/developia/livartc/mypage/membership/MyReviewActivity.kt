package net.developia.livartc.mypage.membership

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import net.developia.livartc.R
import net.developia.livartc.adapter.MyReplyAdapter
import net.developia.livartc.adapter.PurchaseAdapter
import net.developia.livartc.databinding.ActivityMyReviewBinding
import net.developia.livartc.databinding.ActivityPurchaseBinding
import net.developia.livartc.login.TokenManager
import net.developia.livartc.model.MyReply
import net.developia.livartc.model.PurchaseHistory
import net.developia.livartc.retrofit.MyApplication
import net.developia.livartc.retrofit.RetrofitInstance
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

/**
 * LIVARTC
 * Created by 최현서
 * Date: 1/28/24
 * 작업내용: 나의 리뷰 조회
 */
class MyReviewActivity : AppCompatActivity() {
    lateinit var binding: ActivityMyReviewBinding
    private lateinit var myReplyList : List<MyReply>
    private lateinit var myReplyAdapter : MyReplyAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMyReviewBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.backButton.setOnClickListener {
            finish()
        }
        getAllMyReviewList()
    }

    private fun getAllMyReviewList() {
        val jwtToken = TokenManager.getToken(MyApplication.instance)!!
        RetrofitInstance.api.getMyReview(jwtToken)
            .enqueue(object : Callback<List<MyReply>> {
                override fun onResponse(
                    call: Call<List<MyReply>>,
                    response: Response<List<MyReply>>
                ) {
                    if (response.isSuccessful) {
                        myReplyList = response.body()?: emptyList()
                        Handler(Looper.getMainLooper()).postDelayed({
                            binding.progressBar.visibility = View.GONE
                            setRecyclerView()
                        }, 1000)
                    }
                }
                override fun onFailure(call: retrofit2.Call<List<MyReply>>, t: Throwable) {
                    t.printStackTrace()
                }
            })
    }
    private fun setRecyclerView() {
        runOnUiThread {
            if (myReplyList.isEmpty()) binding.emptyMsg.visibility = View.VISIBLE
            else {
                myReplyAdapter = MyReplyAdapter(myReplyList)
                binding.reviewListView.adapter = myReplyAdapter
                binding.reviewListView.layoutManager = LinearLayoutManager(this)
            }
        }
    }
}

