package net.developia.livartc.product

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import net.developia.livartc.ProductActivity
import net.developia.livartc.adapter.ReplyAdapter
import net.developia.livartc.databinding.FragmentDetailBinding
import net.developia.livartc.model.Reply
import net.developia.livartc.retrofit.RetrofitInstance
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

/**
 * LIVARTC
 * Created by 변형준
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


    /**
     * LIVARTC
     * Added by 최현서
     * Date: 1/25/24
     * Time: 11:42
     */
    override fun onAttach(context: Context) {
        super.onAttach(context)
        productActivity = context as ProductActivity
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
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


