package net.developia.livartc.main

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import net.developia.livartc.MainActivity
import net.developia.livartc.ProductActivity
import net.developia.livartc.R
import net.developia.livartc.adapter.BestProductAdapter
import net.developia.livartc.adapter.HomeBannerAdapter
import net.developia.livartc.databinding.FragmentHomeBinding
import net.developia.livartc.main.banner.BannerFragment01
import net.developia.livartc.main.banner.BannerFragment02
import net.developia.livartc.main.banner.BannerFragment03
import net.developia.livartc.model.Product

import net.developia.livartc.retrofit.RetrofitInstance
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

/**
 * LIVARTC
 * Created by 최현서
 * Date: 2024-01-22
 * Time: 오후 3:00
 */
class HomeFragment : Fragment(), View.OnClickListener {
    lateinit var binding: FragmentHomeBinding
    private var currentPage = 0
    lateinit var bestList: List<Product>
    private lateinit var mainActivity: MainActivity
    private lateinit var bestAdapter: BestProductAdapter
    private var isAutoScrolling = false
    val handler = Handler(Looper.getMainLooper()) {
        setPage()
        true
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // 움직이는 메인 배너 만들기
        makeBanner()

        // 베스트 상품 버튼 누르면 인기순 조회페이지 이동
        binding.bestBtn.setOnClickListener {
            (activity as MainActivity).startProductActivity("베스트 상품")
        }

        // 베스트 상품 4개 조회
        getAllBestProduct()

        // 카테고리 버튼 눌렀을 때 카테고리 Fragment 이동
//        binding.categoryBtn.setOnClickListener {
//            parentFragmentManager.beginTransaction().apply {
//                replace(R.id.main_container, CategoryFragment())
//                addToBackStack(null)
//                commit()
//            }
//        }
        binding.categoryBed.setOnClickListener(this)
        binding.categoryDesk.setOnClickListener(this)
        binding.categoryChair.setOnClickListener(this)
        binding.categoryCabinet.setOnClickListener(this)
        binding.categoryDeco.setOnClickListener(this)
    }

    // 카테고리별 이동
    override fun onClick(v: View?) {
        when(v?.id) {
            R.id.category_bed -> (activity as MainActivity).startProductActivityWithType("침대", "category")
            R.id.category_desk -> (activity as MainActivity).startProductActivityWithType("데스크", "category")
            R.id.category_chair -> (activity as MainActivity).startProductActivityWithType("의자", "category")
            R.id.category_cabinet -> (activity as MainActivity).startProductActivityWithType("수납장", "category")
            R.id.category_deco -> (activity as MainActivity).startProductActivityWithType("홈데코", "category")
        }
    }

    // 메인 배너 관련
    override fun onAttach(context: Context) {
        super.onAttach(context)
        mainActivity = context as MainActivity
    }

    override fun onResume() {
        super.onResume()
        startBannerAutoScroll()
    }

    override fun onPause() {
        super.onPause()
        stopBannerAutoScroll()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        stopBannerAutoScroll()
    }

    private fun startBannerAutoScroll() {
        isAutoScrolling = true
        handler.postDelayed(PagerRunnable(), 2500)
    }

    private fun stopBannerAutoScroll() {
        isAutoScrolling = false
        handler.removeCallbacksAndMessages(null)
    }

    private fun makeBanner() {
        val bannerAdapter = HomeBannerAdapter(this)
        bannerAdapter.addFragment(BannerFragment01())
        bannerAdapter.addFragment(BannerFragment02())
        bannerAdapter.addFragment(BannerFragment03())

        val viewPager: ViewPager2 = binding.viewPager2Container

        viewPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                binding.pageNum.text = "${position + 1}  /  3"
                currentPage = position
            }
        })

        binding.viewPager2Container.adapter = bannerAdapter
        binding.viewPager2Container.orientation = ViewPager2.ORIENTATION_HORIZONTAL
        val child = binding.viewPager2Container.getChildAt(0)
        (child as? RecyclerView)?.overScrollMode = View.OVER_SCROLL_NEVER
    }

    inner class PagerRunnable : Runnable {
        override fun run() {
            if (!isAutoScrolling) {
                return
            }
            setPage()
            handler.postDelayed(this, 2500)
        }
    }

    private fun setPage() {
        if (currentPage == 2) currentPage = 0
        else currentPage += 1
        binding.viewPager2Container.setCurrentItem(currentPage, true)
    }

    // 베스트 상품 조회 관련 (Retrofit 연동 후 recycler view 뿌림)
    private fun getAllBestProduct() {
        RetrofitInstance.api.searchProducts("", "", "", "", 6, 4, 1)
            .enqueue(object : Callback<List<Product>> {
                override fun onResponse(
                    call: Call<List<Product>>,
                    response: Response<List<Product>>
                ) {
                    if (response.isSuccessful) {
                        bestList = response.body() ?: emptyList()
                        setBestRecyclerView()
                        Log.d("Best List 조회", "검색 결과: $bestList")
                    } else {
                        Log.e("Best List 조회", "응답 실패: ${response.errorBody()}")
                    }
                }

                override fun onFailure(call: Call<List<Product>>, t: Throwable) {
                    Log.d("Best List 조회", "API 호출 실패: $t")
                }
            })
    }

    private fun setBestRecyclerView() {
        mainActivity.runOnUiThread {
            bestAdapter = BestProductAdapter(bestList, this@HomeFragment)
            binding.recyclerView.adapter = bestAdapter
            binding.recyclerView.layoutManager = GridLayoutManager(activity, 2)
        }
    }

    fun startProductActivityWithType(name: String, type: String) {
        val intent = Intent(requireContext(), ProductActivity::class.java)
        intent.putExtra("name", name)
        intent.putExtra("type", type)
        startActivity(intent)
    }
}