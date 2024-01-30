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

class SearchFragment : Fragment() {

    private lateinit var binding: FragmentSearchPageBinding
    private lateinit var searchBarBinding: FragmentSearchSearchbarBinding
    private var selectedBrand: String? = null
    private var selectedHashTag: String? = null
    private var isHashTagSearch: Boolean = false
    private lateinit var products: List<Product>
    private var selectedSortOption: Int = 4
    private lateinit var sortingSpinner: Spinner
    private var currentPage = 1
    private var isLoading = false


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentSearchPageBinding.inflate(inflater, container, false)
        searchBarBinding = FragmentSearchSearchbarBinding.bind(binding.root.findViewById(R.id.search_searchbar))

        binding.root.findViewById<ImageView>(R.id.back_arrow).setOnClickListener {
            activity?.finish()
        }

        initSearchView()
        setupSpinner()
        loadInitialProducts()

        return binding.root
    }

    private fun initSearchView() {
        searchBarBinding.searchViews.isSubmitButtonEnabled = true
        searchBarBinding.searchViews.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                // 검색어 변경 시 브랜드와 해시태그 초기화
                selectedHashTag = null
                isHashTagSearch = false
                searchProducts(query ?: "", selectedBrand, selectedHashTag, selectedSortOption)
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                return true
            }
        })
    }

    private fun displaySearchResults(products: List<Product>) {
        this.products = products
        val recyclerView = binding.productRecyclerView
        recyclerView.layoutManager = GridLayoutManager(context, 2)
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
            selectedHashTag = null
            isHashTagSearch = false
            displaySelectedBrandHashTags(selectedBrand)
            searchBarBinding.searchViews.query?.toString()?.let { query ->
                searchProducts(query, selectedBrand,"",selectedSortOption)
            }
        }
        binding.searchBrandhashtag.brandsRecyclerView.adapter = brandAdapter
        binding.searchBrandhashtag.brandsRecyclerView.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)

        restoreSearchState()

    }

    private fun setupSpinner() {
        sortingSpinner = binding.root.findViewById(R.id.spinnerSortOptions)
        sortingSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View?, position: Int, id: Long) {
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
                searchProducts(searchBarBinding.searchViews.query?.toString() ?: "", selectedBrand, selectedHashTag, selectedSortOption)
            }

            override fun onNothingSelected(parent: AdapterView<*>) {}
        }
    }

    private fun loadInitialProducts() {
        val sharedPreferences = requireActivity().getSharedPreferences("SearchPreferences", Context.MODE_PRIVATE)
        val savedQuery = sharedPreferences.getString("query", null)
        val savedBrand = sharedPreferences.getString("brand", null)
        val savedHashTag = sharedPreferences.getString("hashTag", null)
        val savedSortOption = sharedPreferences.getInt("sortOption", selectedSortOption)

        searchProducts(savedQuery ?: "", savedBrand, savedHashTag, savedSortOption)
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
        val hashTagRecyclerView = binding.searchBrandhashtag.hashTagRecyclerView
        if (selectedBrand != null) {
            val brandProducts = products.filter { it.brandName == selectedBrand }
            if (brandProducts.isNotEmpty()) {
                val hashTags = brandProducts.first().allHashtags.split(", ").take(6)
                val hashTagAdapter = HashTagAdapter(hashTags) { hashTag ->
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

    private fun displayDynamicHashTags(hashTags: List<String>) {
        val hashTagAdapter = HashTagAdapter(hashTags) { hashTag ->
            selectedHashTag = if (selectedHashTag == hashTag) null else hashTag
            isHashTagSearch = selectedHashTag != null
            searchProducts(searchBarBinding.searchViews.query?.toString() ?: "", selectedBrand, selectedHashTag, selectedSortOption)
        }
        val hashTagRecyclerView = binding.searchBrandhashtag.hashTagRecyclerView
        hashTagRecyclerView.adapter = hashTagAdapter
        hashTagRecyclerView.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        hashTagRecyclerView.visibility = if (hashTags.isNotEmpty()) View.VISIBLE else View.GONE
    }

    private fun saveSearchState(query: String?, brand: String?, hashTag: String?, sortOption: Int) {
        val sharedPreferences = requireActivity().getSharedPreferences("SearchPreferences", Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.putString("query", query)
        editor.putString("brand", brand)
        editor.putString("hashTag", hashTag)
        editor.putInt("sortOption", sortOption)
        editor.apply()
    }

    private fun restoreSearchState() {
        val sharedPreferences = requireActivity().getSharedPreferences("SearchPreferences", Context.MODE_PRIVATE)
        val query = sharedPreferences.getString("query", null)
        val brand = sharedPreferences.getString("brand", null)
        val hashTag = sharedPreferences.getString("hashTag", null)
        val sortOption = sharedPreferences.getInt("sortOption", 4)

        searchProducts(query, brand, hashTag, sortOption)
    }



    private fun searchProducts(query: String?, brand: String?, hashTag: String?, sortOption: Int) {
        RetrofitInstance.api.searchProducts("", brand ?: "", hashTag ?: "", query ?: "", sortOption, 30, 1)
            .enqueue(object : Callback<List<Product>> {
                override fun onResponse(call: Call<List<Product>>, response: Response<List<Product>>) {
                    if (response.isSuccessful) {
                        products = response.body() ?: emptyList()
                        displaySearchResults(products)
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
        saveSearchState(query, brand, hashTag, sortOption)
    }


}


