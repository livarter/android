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
import net.developia.livartc.adapter.ReviewAdapter
import net.developia.livartc.databinding.FragmentDetailBinding
import net.developia.livartc.retrofit.RetrofitInstance
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import net.developia.livartc.model.Reply

/**
 * LIVARTC
 * Created by 변형준
 * Date: 1/19/24
 * Time: 17:21
 */
class DetailFragment : Fragment() {
    lateinit var binding : FragmentDetailBinding
    var reviewList : Reply? = null
    private lateinit var productActivity: ProductActivity
    private lateinit var replyAdapter: ReviewAdapter
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
        getAllReview()
    }

    //베스트 상품 조회 관련(Retrofit 연동 후 recycler view 뿌림)
    private fun getAllReview() {
        RetrofitInstance.api.getReview().enqueue(object : Callback<Reply> {
            override fun onResponse(call: Call<Reply>, response: Response<Reply>) {
                reviewList = response.body()
                Log.d("hschoi", "?????$reviewList")
                setReviewRecyclerView()
            }

            override fun onFailure(call: Call<Reply>, t: Throwable) {
                Log.d("hschoi", "리뷰 스프링 연결 실패!!!!")
            }
        })
    }

    private fun setReviewRecyclerView() {
        productActivity.runOnUiThread {
            reviewList?.let { if (it.size > 0) binding.noReview.isVisible = false }
            replyAdapter = ReviewAdapter(reviewList, this@DetailFragment)
            binding.reviewRecyclerView.adapter = replyAdapter
            binding.reviewRecyclerView.layoutManager = LinearLayoutManager(requireContext())
        }
    }


}