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
import androidx.recyclerview.widget.LinearLayoutManager
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
import net.developia.livartc.model.Product

class HomeFragment : Fragment() {
    lateinit var binding: FragmentHomeBinding
    private var currentPage = 0
    private lateinit var bestList: ArrayList<Product>
    lateinit var mainActivity: MainActivity
    lateinit var bestAdapter: BestProductAdapter

    val handler = Handler(Looper.getMainLooper()) {
        setPage()
        true
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)

        // 2. Context를 Activity로 형변환하여 할당
        mainActivity = context as MainActivity
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        //배너 뷰페이저2 세팅하는 함수
        makeBanner()

        getAllBestProduct()


        //category버튼 누르면 CategoryFragment로 변경
        binding.categoryBtn.setOnClickListener {
            parentFragmentManager.beginTransaction().apply {
                replace(R.id.main_container, CategoryFragment())
                //addToBackStack(null)메서드는, 프레그먼트를 replace했을때, 뒤로가기를 가능하게 하는 함수
                addToBackStack(null)
                commit()
            }
        }
        val bundle = Bundle()
        bundle.putString("title", "베스트상품")
        //베스트 조회 눌렀을 때 ProductActivity 생성(베스트순으로 필터링된 조회창으로 이동)
        binding.bestBtn.setOnClickListener {
            (activity as MainActivity).startProductActivity("베스트 상품")
        }
    }

    private fun getAllBestProduct() {
        Thread {
            bestList = arrayListOf<Product>()
            bestList.add(
                Product(
                    "베스트상품1",
                    10000,
                    "https://image.newdaily.co.kr/site/data/img/2019/03/06/2019030600071_1.jpg"
                )
            )
            bestList.add(
                Product(
                    "베스트상품2",
                    15000,
                    "https://cdn-pro-web-219-123.cdn-nhncommerce.com/decolitr0605_godomall_com/data/goods/23/05/20/6153/register_main_027.jpg"
                )
            )
            bestList.add(
                Product(
                    "베스트상품3",
                    20000,
                    "https://www.jangin.com/data/product/b_file_1564962731jgwk7bws9h.jpg"
                )
            )
            bestList.add(
                Product(
                    "베스트상품4",
                    17000,
                    "https://m.warehouse-lab.com/web/product/medium/202112/e0ae4c482932f3fd0323632643718687.jpg"
                )
            )
            setBestRecyclerView()
        }.start()

    }

    private fun setBestRecyclerView() {
        mainActivity.runOnUiThread{
            bestAdapter = BestProductAdapter(bestList, this@HomeFragment)
            binding.recyclerView.adapter = bestAdapter
            binding.recyclerView.layoutManager = GridLayoutManager(activity, 2)
        }
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
            // 페이지가 선택될 때마다 페이지 번호 업데이트
            binding.pageNum.text = "${position + 1}  /  3"
            currentPage = position
        }
    })

    binding.viewPager2Container.adapter = bannerAdapter
    binding.viewPager2Container.orientation = ViewPager2.ORIENTATION_HORIZONTAL
    val child = binding.viewPager2Container.getChildAt(0)
    //스크롤 넘길 때, 효과 나타 나지 않도록
    (child as? RecyclerView)?.overScrollMode = View.OVER_SCROLL_NEVER

    val thread = Thread(PagerRunnable())
    thread.start()
}

private fun setPage() {
    if (currentPage == 3) currentPage = 0
    binding.viewPager2Container.setCurrentItem(currentPage, true)
    currentPage += 1
}

inner class PagerRunnable : Runnable {
    override fun run() {
        while (true) {
            try {
                Thread.sleep(2500)
                handler.sendEmptyMessage(0)
            } catch (e: InterruptedException) {
                Log.d("interupt", "interupt발생")
            }
        }
    }
}


}