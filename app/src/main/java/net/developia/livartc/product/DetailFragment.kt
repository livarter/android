package net.developia.livartc.product

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import net.developia.livartc.ProductActivity
import net.developia.livartc.PurchaseActivity
import net.developia.livartc.ReplyWriteActivity
import net.developia.livartc.adapter.ReplyAdapter
import net.developia.livartc.databinding.FragmentDetailBinding
import net.developia.livartc.model.Product
import net.developia.livartc.model.Reply
import net.developia.livartc.retrofit.RetrofitInstance
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

/**
 * LIVARTC
 * Created by 변형준
 * Enhanced by 최현서
 * Date: 1/19/24
 * Time: 17:21
 */
class DetailFragment : Fragment() {
    lateinit var binding: FragmentDetailBinding
    private lateinit var replyList: List<Reply>
    private lateinit var productActivity: ProductActivity
    private lateinit var replyAdapter: ReplyAdapter
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentDetailBinding.inflate(inflater, container, false)
        binding.addToCartBtn.setOnClickListener {
            val detailAddToCartFragment = DetailAddToCartFragment()
            detailAddToCartFragment.show(requireFragmentManager(), detailAddToCartFragment.tag)
        }
        return binding.root
    }
    override fun onAttach(context: Context) {
        super.onAttach(context)
        productActivity = context as ProductActivity
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        getAllReply()
        binding.replyWriteBtn.setOnClickListener {
            val intent = Intent(activity, ReplyWriteActivity::class.java)
            startActivity(intent)
        }
        val product = arguments?.getSerializable("product") as? Product
        if (product != null) {
            Glide.with(binding.productImg.context)
                .load(product.productImage)
                .into(binding.productImg)
            binding.productName.text = product.productName
            binding.productDesc.text = product.productDescription
            binding.productPrice.text = "${product.productPrice}"
        }
    }

    override fun onResume() {
        super.onResume()
        getAllReply()
    }

    //베스트 상품 조회 관련(Retrofit 연동 후 recycler view 뿌림)
    private fun getAllReply() {
        RetrofitInstance.api.getReview(1)
            .enqueue(object : Callback<List<Reply>> {
                override fun onResponse(
                    call: Call<List<Reply>>,
                    response: Response<List<Reply>>
                ) {
                    if (response.isSuccessful) {
                        replyList = response.body() ?: emptyList()
                        setReplyRecyclerView()
                        Log.d("Reply 조회: 상품1", "검색 결과: $replyList")
                    } else {
                        Log.e("Reply 조회: 상품1", "응답 실패: ${response.errorBody()}")
                    }
                }

                override fun onFailure(call: Call<List<Reply>>, t: Throwable) {
                    Log.d("Best List 조회", "API 호출 실패: $t")
                }
            })
    }

    //
    private fun setReplyRecyclerView() {
        productActivity.runOnUiThread {
            if (replyList.isNotEmpty()) binding.noReply.isVisible = false
        }
        replyAdapter = ReplyAdapter(replyList)
        binding.replyRecyclerView.adapter = replyAdapter
        binding.replyRecyclerView.layoutManager = LinearLayoutManager(requireContext())
    }
}


