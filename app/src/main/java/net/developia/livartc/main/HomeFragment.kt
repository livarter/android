package net.developia.livartc.main

import android.content.Context
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
import net.developia.livartc.R
import net.developia.livartc.adapter.BestProductAdapter
import net.developia.livartc.adapter.HomeBannerAdapter
import net.developia.livartc.databinding.FragmentHomeBinding
import net.developia.livartc.main.banner.BannerFragment01
import net.developia.livartc.main.banner.BannerFragment02
import net.developia.livartc.main.banner.BannerFragment03
import net.developia.livartc.model.BestProduct
import net.developia.livartc.model.Product

import net.developia.livartc.retrofit.RetrofitInstance
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class HomeFragment : Fragment() {
    lateinit var binding: FragmentHomeBinding
    private var currentPage = 0
    private var bestList: List<Product>? = null
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

        //움직이는 메인 배너 만들기
        makeBanner()
        //베스트 상품 버튼 누르면 인기순 조회페이지 이동
        binding.bestBtn.setOnClickListener {
            (activity as MainActivity).startProductActivity("베스트 상품")
        }
        //베스트 상품 4개 조회
        getAllBestProduct()
        //카테고리 버튼 눌렀을때 카테고리 Fragment 이동
        binding.categoryBtn.setOnClickListener {
            parentFragmentManager.beginTransaction().apply {
                replace(R.id.main_container, CategoryFragment())
                addToBackStack(null)
                commit()
            }
        }
    }


    //메인 배너 관련
    override fun onAttach(context: Context) {
        super.onAttach(context)
        mainActivity = context as MainActivity
    }
    override fun onPause() {
        super.onPause()
        stopBannerAutoScroll()
    }

    override fun onDestroy() {
        super.onDestroy()
        stopBannerAutoScroll()
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

        isAutoScrolling = true
        handler.postDelayed(PagerRunnable(), 2500)
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
        if (currentPage == 3) currentPage = 0
        binding.viewPager2Container.setCurrentItem(currentPage, true)
        currentPage += 1
    }



    //베스트 상품 조회 관련(Retrofit 연동 후 recycler view 뿌림)

        private fun getAllBestProduct() {
            RetrofitInstance.api.searchProducts("", "", "", "", 6, 4, 1)
                    .enqueue(object : Callback<List<Product>> {
                    override fun onResponse(
                        call: Call<List<Product>>,
                        response: Response<List<Product>>
                    ) {
                        bestList = response.body()
                        Log.d("hschoi", "$bestList")
                        setBestRecyclerView()
                    }

                    override fun onFailure(call: Call<List<Product>>, t: Throwable) {
                        Log.d("hschoi", "스프링 연결 실패!!!!")
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

}