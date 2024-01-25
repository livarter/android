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
import net.developia.livartc.adapter.BrandAdapter
import net.developia.livartc.adapter.HashTagAdapter
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
    private var selectedBrand: String? = null

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
                    searchProducts(it,selectedBrand)
                }
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                // 필요한 경우 이곳에서 검색어 변경에 대한 처리를 구현
                return true
            }
        })
    }

    private fun searchProducts(searchQuery: String, selectedBrand: String?) {

        val query = searchQuery.ifBlank { null }

        RetrofitInstance.api.searchProducts("", selectedBrand ?: "","", query ?:"", 1, 10, 1)
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

        loadInitialProducts()
        // 테스트 데이터 사용

        //브랜드 부분 목록 초기화 부분

        val brandNames = resources.getStringArray(R.array.brand_names).toList()
        val brandAdapter = BrandAdapter(brandNames) { brand ->
            selectedBrand = brand
            // selectedBrand의 null 여부를 확인하고, null이 아닌 경우에만 함수 호출
            searchBarBinding.searchViews.query?.toString()?.let { query ->
                searchProducts(query, selectedBrand)
            }
        }
        binding.searchBrandhashtag.brandsRecyclerView.adapter = brandAdapter
        binding.searchBrandhashtag.brandsRecyclerView.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)

        val hashTags = listOf("해시태그1", "해시태그2", "해시태그3") // 임시 데이터
        val hashTagAdapter = HashTagAdapter(hashTags)
        binding.searchBrandhashtag.hashTagRecyclerView.adapter = hashTagAdapter
        binding.searchBrandhashtag.hashTagRecyclerView.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)


    }

    private fun loadInitialProducts() {
        searchProducts("",selectedBrand)
    }
}


