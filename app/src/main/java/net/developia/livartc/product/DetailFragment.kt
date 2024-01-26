package net.developia.livartc.product

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import net.developia.livartc.databinding.FragmentDetailBinding
import net.developia.livartc.model.Product

/**
 * LIVARTC
 * Created by 변형준
 * Date: 1/19/24
 * Time: 17:21
 */
class DetailFragment : Fragment() {
    lateinit var binding : FragmentDetailBinding
//    var reviewList : Reply? = null
//    private lateinit var productActivity: ProductActivity
//    private lateinit var replyAdapter: ReviewAdapter
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

//    override fun onAttach(context: Context) {
//        super.onAttach(context)
//        productActivity = context as ProductActivity
//    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val product = arguments?.getSerializable("product") as? Product

        product?.let {
            binding.addToCartBtn.setOnClickListener {
                product?.let { product ->
                    showAddToCartFragment(product)
                }
            }
        }

//        getAllReview()
    }

    //베스트 상품 조회 관련(Retrofit 연동 후 recycler view 뿌림)
//    private fun getAllReview() {
//        RetrofitInstance.api.getReview().enqueue(object : Callback<Reply> {
//            override fun onResponse(call: Call<Reply>, response: Response<Reply>) {
//                reviewList = response.body()
//                Log.d("hschoi", "?????$reviewList")
//                setReviewRecyclerView()
//            }
//
//            override fun onFailure(call: Call<Reply>, t: Throwable) {
//                Log.d("hschoi", "리뷰 스프링 연결 실패!!!!")
//            }
//        })
//    }
//
//    private fun setReviewRecyclerView() {
//        productActivity.runOnUiThread {
//            reviewList?.let { if (it.size > 0) binding.noReview.isVisible = false }
//            replyAdapter = ReviewAdapter(reviewList, this@DetailFragment)
//            binding.reviewRecyclerView.adapter = replyAdapter
//            binding.reviewRecyclerView.layoutManager = LinearLayoutManager(requireContext())
//        }
//    }

    private fun showAddToCartFragment(product: Product) {
        val bundle = Bundle().apply {
            putSerializable("product", product)
        }
        val addToCartFragment = DetailAddToCartFragment().apply {
            arguments = bundle
        }
        addToCartFragment.show(requireFragmentManager(), addToCartFragment.tag)
    }


}