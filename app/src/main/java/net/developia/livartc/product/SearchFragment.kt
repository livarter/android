package net.developia.livartc.product

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Spinner
import android.widget.AdapterView
import androidx.appcompat.widget.SearchView
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import net.developia.livartc.R
import net.developia.livartc.adapter.BrandAdapter
import net.developia.livartc.adapter.HashTagAdapter
import net.developia.livartc.adapter.ProductAdapter
import net.developia.livartc.databinding.FragmentSearchPageBinding
import net.developia.livartc.databinding.FragmentSearchSearchbarBinding
import net.developia.livartc.databinding.FragmentSearchSortingdropdownBinding
import net.developia.livartc.model.Product
import net.developia.livartc.retrofit.RetrofitInstance
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SearchFragment : Fragment() {
    lateinit var binding: FragmentSearchPageBinding
    lateinit var searchBarBinding: FragmentSearchSearchbarBinding
    private var selectedBrand: String? = null
    private var selectedHashTag: String? = null
    private lateinit var products: List<Product>
    private var selectedSortOption: Int = 4
    private lateinit var sortingSpinner: Spinner



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
                searchProducts(query ?: "", selectedBrand, selectedHashTag, selectedSortOption)
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                // 필요한 경우 이곳에서 검색어 변경에 대한 처리를 구현
                // 추후에 새로운 검색어 검색시 선택된 브랜드와 해시태그 초기화 필요
                return true
            }
        })
    }

    private fun searchProducts(searchQuery: String, selectedBrand: String?, hashTag: String?=null, sortOption: Int) {

        val query = searchQuery.ifBlank { "" }

        RetrofitInstance.api.searchProducts("", selectedBrand ?: "", hashTag ?: "", query ?: "", sortOption, 10, 1)
            .enqueue(object : Callback<List<Product>> {
                override fun onResponse(call: Call<List<Product>>, response: Response<List<Product>>) {
                    if (response.isSuccessful) {
                        val products = response.body() ?: emptyList()
                        this@SearchFragment.products = products
                        displaySearchResults(products)
                        displaySelectedBrandHashTags(selectedBrand)

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
        this.products = products

        val recyclerView = binding.productRecyclerView
        recyclerView.layoutManager = GridLayoutManager(context, 2) // 2열 그리드 레이아웃 설정
        recyclerView.adapter = ProductAdapter(products) { product ->
            showProductDetail(product)
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        sortingSpinner = view.findViewById(R.id.spinnerSortOptions) // 여기서 ID는 spinner의 ID와 일치해야 합니다.
        setupSpinner()

        loadInitialProducts()

        val brandNames = resources.getStringArray(R.array.brand_names).toList()
        val brandAdapter = BrandAdapter(brandNames) { brand ->
            selectedBrand = if (selectedBrand == brand) null else brand
            displaySelectedBrandHashTags(selectedBrand)
            searchBarBinding.searchViews.query?.toString()?.let { query ->
                searchProducts(query, selectedBrand,"",selectedSortOption)
            }
        }
        binding.searchBrandhashtag.brandsRecyclerView.adapter = brandAdapter
        binding.searchBrandhashtag.brandsRecyclerView.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)

    }

    private fun setupSpinner() {
        sortingSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View, position: Int, id: Long) {
                val selectedSortOptionString = parent.getItemAtPosition(position) as String
                selectedSortOption = when (selectedSortOptionString) {
                    "최신순" -> 4
                    "이름순" -> 3
                    "낮은가격순" -> 1
                    "높은가격순" -> 2
                    "판매량순" -> 5
                    "조회수순" -> 6
                    else -> 4 // 기본값
                }
                Log.d("SearchFragment", "선택된 정렬 옵션: $selectedSortOption")
                // 선택된 정렬 옵션에 따른 추가 작업
                searchProducts("", selectedBrand, selectedHashTag, selectedSortOption)
            }

            override fun onNothingSelected(parent: AdapterView<*>) {
                // 선택되지 않았을 때의 동작
            }
        }
    }


    private fun loadInitialProducts() {
        searchProducts("",selectedBrand,"",selectedSortOption)
        displaySelectedBrandHashTags(selectedBrand)
    }

    private fun showProductDetail(product: Product) {
        Log.d("SearchFragment to DetailFragment","$product")
        val bundle = Bundle().apply {
            putSerializable("product",product)
        }
        val detailFragment = DetailFragment().apply {
            arguments = bundle
        }

        fragmentManager?.beginTransaction()?.replace(R.id.product_container, detailFragment)?.addToBackStack(null)?.commit()

    }

    private fun displaySelectedBrandHashTags(selectedBrand: String?) {
        if (selectedBrand != null) {
            val brandProducts = products.filter { it.brandName == selectedBrand }
            if (brandProducts.isNotEmpty()) {
                val hashTags = brandProducts.first().allHashtags.split(", ").take(6)
                val hashTagAdapter = HashTagAdapter(hashTags) { hashTag ->
                    // 해시태그 클릭시 처리될 로직
                    searchProducts("", selectedBrand, hashTag,selectedSortOption)
                    Log.d("hashTags", "$hashTags")
                }
                binding.searchBrandhashtag.hashTagRecyclerView.adapter = hashTagAdapter
                binding.searchBrandhashtag.hashTagRecyclerView.layoutManager =
                    LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
                binding.searchBrandhashtag.hashTagRecyclerView.visibility = View.VISIBLE
            } else {
                binding.searchBrandhashtag.hashTagRecyclerView.visibility = View.GONE
            }
        } else {
            binding.searchBrandhashtag.hashTagRecyclerView.visibility = View.GONE
        }
    }

}


