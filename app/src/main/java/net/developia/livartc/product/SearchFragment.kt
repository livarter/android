package net.developia.livartc.product

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.SearchView
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import net.developia.livartc.R
import net.developia.livartc.adapter.ProductAdapter
import net.developia.livartc.databinding.FragmentSearchPageBinding
import net.developia.livartc.databinding.FragmentSearchSearchbarBinding
import net.developia.livartc.model.Product
import net.developia.livartc.retrofit.RetrofitInstance
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SearchFragment : Fragment() {
    lateinit var binding: FragmentSearchPageBinding
    lateinit var searchBarBinding: FragmentSearchSearchbarBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSearchPageBinding.inflate(inflater, container, false)
        searchBarBinding = FragmentSearchSearchbarBinding.bind(binding.root.findViewById(R.id.search_searchbar))

        initSearchView()

        return binding.root
    }

    private fun initSearchView() {
        searchBarBinding.searchViews.isSubmitButtonEnabled = true
        searchBarBinding.searchViews.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                query?.let {
                    searchProducts(it)
                }
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                // 필요한 경우 이곳에서 검색어 변경에 대한 처리를 구현
                return true
            }
        })
    }

    private fun searchProducts(searchQuery: String) {
        RetrofitInstance.api.searchProducts("", "", "", searchQuery, 1, 10, 1)
            .enqueue(object : Callback<List<Product>> {
                override fun onResponse(call: Call<List<Product>>, response: Response<List<Product>>) {
                    if (response.isSuccessful) {
                        val products = response.body() ?: emptyList()
                        displaySearchResults(products)

                        // 로그에 결과 출력
                        Log.d("SearchFragment", "검색 결과: $products")
                    } else {
                        Log.e("SearchFragment", "응답 실패: ${response.errorBody()}")
                    }
                }

                override fun onFailure(call: Call<List<Product>>, t: Throwable) {
                    Log.e("SearchFragment", "API 호출 실패: $t")
                }
            })
    }

    private fun displaySearchResults(products: List<Product>) {
        val recyclerView = binding.productRecyclerView
        recyclerView.layoutManager = GridLayoutManager(context, 2) // 2열 그리드 레이아웃 설정
        recyclerView.adapter = ProductAdapter(products)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        // 테스트 데이터 사용
        val testProducts = listOf(
            Product(
                productId = 1,
                productName = "마메 라운지체어 (2색)",
                productPrice = 8339000,
                productDescription = null,
                productImage = "https://static.hyundailivart.co.kr/upload_mall/goods/P200164153/GM43194682_img.jpg/dims/resize/x250/cropcenter/250x250/autorotate/on/optimize",
                brandName = "Fogia",
                hashtags = "특징2, 특징3, 특징5",
                allHashtags = "특징1, 특징2, 특징3, 특징4, 특징5"
            ),
            Product(
                productId = 2,
                productName = "리라 오토만 (3종)",
                productPrice = 3592000,
                productDescription = null,
                productImage= "https://static.hyundailivart.co.kr/upload_mall/goods/P200164152/GM43194674_img.jpg/dims/resize/x250/cropcenter/250x250/autorotate/on/optimize",
                brandName = "Fogia",
                hashtags = "특징2, 특징3, 특징5",
                allHashtags = "특징1, 특징2, 특징3, 특징4, 특징5"
            ),
            Product(
                productId = 1,
                productName = "마메 라운지체어 (2색)",
                productPrice = 8339000,
                productDescription = null,
                productImage = "https://static.hyundailivart.co.kr/upload_mall/goods/P200164153/GM43194682_img.jpg/dims/resize/x250/cropcenter/250x250/autorotate/on/optimize",
                brandName = "Fogia",
                hashtags = "특징2, 특징3, 특징5",
                allHashtags = "특징1, 특징2, 특징3, 특징4, 특징5"
            ),
            Product(
                productId = 2,
                productName = "리라 오토만 (3종)",
                productPrice = 3592000,
                productDescription = null,
                productImage= "https://static.hyundailivart.co.kr/upload_mall/goods/P200164152/GM43194674_img.jpg/dims/resize/x250/cropcenter/250x250/autorotate/on/optimize",
                brandName = "Fogia",
                hashtags = "특징2, 특징3, 특징5",
                allHashtags = "특징1, 특징2, 특징3, 특징4, 특징5"
            ),
            Product(
                productId = 1,
                productName = "마메 라운지체어 (2색)",
                productPrice = 8339000,
                productDescription = null,
                productImage = "https://static.hyundailivart.co.kr/upload_mall/goods/P200164153/GM43194682_img.jpg/dims/resize/x250/cropcenter/250x250/autorotate/on/optimize",
                brandName = "Fogia",
                hashtags = "특징2, 특징3, 특징5",
                allHashtags = "특징1, 특징2, 특징3, 특징4, 특징5"
            ),
            Product(
                productId = 2,
                productName = "리라 오토만 (3종)",
                productPrice = 3592000,
                productDescription = null,
                productImage= "https://static.hyundailivart.co.kr/upload_mall/goods/P200164152/GM43194674_img.jpg/dims/resize/x250/cropcenter/250x250/autorotate/on/optimize",
                brandName = "Fogia",
                hashtags = "특징2, 특징3, 특징5",
                allHashtags = "특징1, 특징2, 특징3, 특징4, 특징5"
            )
        )
        displaySearchResults(testProducts)
    }
}


