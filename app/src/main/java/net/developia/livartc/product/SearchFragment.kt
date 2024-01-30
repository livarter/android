package net.developia.livartc.product

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Spinner
import android.widget.AdapterView
import android.widget.ImageView
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

/**
 * LIVARTC
 * Created by 오수영
 * Date: 1/29/24
 * Time: 11:10 AM
 * Function: 제품 검색 페이지의 사용자 인터페이스와 상호작용을 관리하는 프래그먼트
 */

class SearchFragment : Fragment() {

    private lateinit var binding: FragmentSearchPageBinding // 검색 페이지 바인딩
    private lateinit var searchBarBinding: FragmentSearchSearchbarBinding // 검색바 바인딩
    private var selectedBrand: String? = null // 선택된 브랜드
    private var selectedHashTag: String? = null // 선택된 해시태그
    private var isHashTagSearch: Boolean = false // 해시태그 검색 여부
    private lateinit var products: List<Product> // 제품 목록
    private var selectedSortOption: Int = 4 // 선택된 정렬 옵션 (기본값은 예시로 '최신순')
    private lateinit var sortingSpinner: Spinner // 정렬 옵션을 선택하는 스피너
    private var currentPage = 1 // 현재 페이지 번호
    private var isLoading = false // 로딩 상태


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentSearchPageBinding.inflate(inflater, container, false)
        searchBarBinding = FragmentSearchSearchbarBinding.bind(binding.root.findViewById(R.id.search_searchbar))

        binding.root.findViewById<ImageView>(R.id.back_arrow).setOnClickListener {
            activity?.finish()      // 뒤로가기 클릭 시 액티비티 종료
        }

        initSearchView()        // 검색 뷰 초기화
        setupSpinner()          // 스피너 설정
        loadInitialProducts()   // 초기 제품 로드

        return binding.root
    }

    private fun initSearchView() {
        searchBarBinding.searchViews.isSubmitButtonEnabled = true       // 검색 제출 버튼 활성화
        searchBarBinding.searchViews.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                // 검색어 변경 시 브랜드와 해시태그 초기화
                selectedHashTag = null
                isHashTagSearch = false
                searchProducts(query ?: "", selectedBrand, selectedHashTag, selectedSortOption) // 제품 검색 실행
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                // 검색어 변경 시 실행되는 로직
                return true
            }
        })
    }

    private fun displaySearchResults(products: List<Product>) {
        // 검색 결과를 표시하는 함수
        this.products = products
        val recyclerView = binding.productRecyclerView
        recyclerView.layoutManager = GridLayoutManager(context, 2)  // 그리드 레이아웃 사용
        recyclerView.adapter = ProductAdapter(products) { product ->
            showProductDetail(product) // 제품 클릭 시 상세 페이지로 이동
        }
    }

    // 프래그먼트의 뷰가 생성될 때 호출되는 함수
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // 정렬 옵션 선택을 위한 스피너 초기화
        sortingSpinner = view.findViewById(R.id.spinnerSortOptions) // 여기서 ID는 spinner의 ID와 일치해야 합니다.
        setupSpinner()  // 스피너 설정 함수 호출

        loadInitialProducts()   // 초기 제품 데이터 로드 함수 호출

        // 브랜드 이름 배열을 리소스에서 가져와 리스트로 변환
        val brandNames = resources.getStringArray(R.array.brand_names).toList()
        // 브랜드 선택을 위한 어댑터 설정 및 리사이클러뷰 적용
        val brandAdapter = BrandAdapter(brandNames) { brand ->
            // 선택된 브랜드 처리 로직
            selectedBrand = if (selectedBrand == brand) null else brand
            selectedHashTag = null      // 해시태그 선택 초기화
            isHashTagSearch = false     // 해시태그 검색 상태 초기화
            displaySelectedBrandHashTags(selectedBrand)     // 선택 브랜드에 따른 해시태그
            searchBarBinding.searchViews.query?.toString()?.let { query ->
                searchProducts(query, selectedBrand,"",selectedSortOption)  // 제품 검색
            }
        }
        // 브랜드 선택을 위한 리사이클러뷰 설정
        binding.searchBrandhashtag.brandsRecyclerView.adapter = brandAdapter
        binding.searchBrandhashtag.brandsRecyclerView.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)

        restoreSearchState()    // 이전 검색 상태 복원
    }

    // 스피너 설정 함수
    private fun setupSpinner() {
        sortingSpinner = binding.root.findViewById(R.id.spinnerSortOptions)
        // 스피너 아이템 선택 이벤트 리스너 설정
        sortingSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            // 아이템이 선택될 때 호출되는 함수
            override fun onItemSelected(parent: AdapterView<*>, view: View?, position: Int, id: Long) {
                // 선택된 정렬 옵션에 따라 변수값 변경
                val selectedSortOptionString = parent.getItemAtPosition(position) as String
                selectedSortOption = when (selectedSortOptionString) {
                    "최신순" -> 4
                    "이름순" -> 3
                    "낮은가격순" -> 1
                    "높은가격순" -> 2
                    "판매량순" -> 5
                    "조회수순" -> 6
                    else -> 4
                }
                // 변경된 정렬 옵션으로 제품 검색
                searchProducts(searchBarBinding.searchViews.query?.toString() ?: "", selectedBrand, selectedHashTag, selectedSortOption)
            }
            override fun onNothingSelected(parent: AdapterView<*>) {}
        }
    }

    // 초기 제품 데이터 로드 함수
    private fun loadInitialProducts() {
        // 공유된 프레퍼런스에서 이전 검색 상태 로드
        val sharedPreferences = requireActivity().getSharedPreferences("SearchPreferences", Context.MODE_PRIVATE)
        val savedQuery = sharedPreferences.getString("query", null)
        val savedBrand = sharedPreferences.getString("brand", null)
        val savedHashTag = sharedPreferences.getString("hashTag", null)
        val savedSortOption = sharedPreferences.getInt("sortOption", selectedSortOption)

        // 저장된 검색 상태를 바탕으로 제품 검색 실행
        searchProducts(savedQuery ?: "", savedBrand, savedHashTag, savedSortOption)
    }


    // 제품 상세 페이지 표시 함수
    private fun showProductDetail(product: Product) {
        Log.d("SearchFragment to DetailFragment","$product")
        // 제품 정보를 Bundle에 담아 상세 프래그먼트로 전달
        val bundle = Bundle().apply {
            putSerializable("product",product)
        }
        // 상세 프래그먼트 생성 및 번들 전달
        val detailFragment = DetailFragment().apply {
            arguments = bundle
        }

        // 프래그먼트 매니저를 사용해 상세 프래그먼트로 전환
        fragmentManager?.beginTransaction()?.replace(R.id.product_container, detailFragment)?.addToBackStack(null)?.commit()
    }

    // 선택된 브랜드에 따른 해시태그 표시 함수
    private fun displaySelectedBrandHashTags(selectedBrand: String?) {
        val hashTagRecyclerView = binding.searchBrandhashtag.hashTagRecyclerView
        if (selectedBrand != null) {
            // 선택된 브랜드에 해당하는 제품 목록 필터링
            val brandProducts = products.filter { it.brandName == selectedBrand }
            if (brandProducts.isNotEmpty()) {
                // 첫번째 제품의 해시태그 리스트 추출 및 해시태그 어대버에 전달
                val hashTags = brandProducts.first().allHashtags.split(", ").take(6)
                val hashTagAdapter = HashTagAdapter(hashTags) { hashTag ->
                    // 해시태그 선택시의 로직
                    selectedHashTag = if (selectedHashTag == hashTag) null else hashTag
                    isHashTagSearch = selectedHashTag != null
                    searchProducts(searchBarBinding.searchViews.query?.toString() ?: "", selectedBrand, selectedHashTag, selectedSortOption)
                }
                hashTagRecyclerView.adapter = hashTagAdapter
                hashTagRecyclerView.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
                hashTagRecyclerView.visibility = View.VISIBLE
            } else {
                hashTagRecyclerView.visibility = View.GONE
            }
        } else {
            hashTagRecyclerView.visibility = View.GONE
        }
    }

    // 동적 해시태그 표시
    private fun displayDynamicHashTags(hashTags: List<String>) {
        val hashTagAdapter = HashTagAdapter(hashTags) { hashTag ->
            // 해시태그 선택 시 로직
            selectedHashTag = if (selectedHashTag == hashTag) null else hashTag
            isHashTagSearch = selectedHashTag != null
            searchProducts(searchBarBinding.searchViews.query?.toString() ?: "", selectedBrand, selectedHashTag, selectedSortOption)
        }
        val hashTagRecyclerView = binding.searchBrandhashtag.hashTagRecyclerView
        hashTagRecyclerView.adapter = hashTagAdapter
        hashTagRecyclerView.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        hashTagRecyclerView.visibility = if (hashTags.isNotEmpty()) View.VISIBLE else View.GONE
    }

    // 검색 상태 저장
    private fun saveSearchState(query: String?, brand: String?, hashTag: String?, sortOption: Int) {
        val sharedPreferences = requireActivity().getSharedPreferences("SearchPreferences", Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.putString("query", query)
        editor.putString("brand", brand)
        editor.putString("hashTag", hashTag)
        editor.putInt("sortOption", sortOption)
        editor.apply()
    }

    // 검색 상태 복원
    private fun restoreSearchState() {
        val sharedPreferences = requireActivity().getSharedPreferences("SearchPreferences", Context.MODE_PRIVATE)
        val query = sharedPreferences.getString("query", null)
        val brand = sharedPreferences.getString("brand", null)
        val hashTag = sharedPreferences.getString("hashTag", null)
        val sortOption = sharedPreferences.getInt("sortOption", 4)

        searchProducts(query, brand, hashTag, sortOption)
    }



    // 제품 검색 실행
    private fun searchProducts(query: String?, brand: String?, hashTag: String?, sortOption: Int) {
        // Retrofit 인스턴스를 사용하여 API 호출 및 응답 처리
        RetrofitInstance.api.searchProducts("", brand ?: "", hashTag ?: "", query ?: "", sortOption, 30, 1)
            .enqueue(object : Callback<List<Product>> {
                override fun onResponse(call: Call<List<Product>>, response: Response<List<Product>>) {
                    if (response.isSuccessful) {
                        // API 응답 성공 시 제품 목록 업데이트 및 표시
                        products = response.body() ?: emptyList()
                        displaySearchResults(products)
                        // 해시태그 검색이 아니고 제품 목록이 비어있지 않은 경우 동적 해시태그 표시
                        if (!isHashTagSearch && products.isNotEmpty()) {
                            displayDynamicHashTags(products.first().allHashtags.split(", ").take(6))
                        }
                    } else {
                        Log.e("SearchFragment", "응답 실패: ${response.errorBody()}")
                    }
                }

                override fun onFailure(call: Call<List<Product>>, t: Throwable) {
                    Log.e("SearchFragment", "API 호출 실패: $t")
                }
            })
        // 검색 상태 저장
        saveSearchState(query, brand, hashTag, sortOption)
    }
}


