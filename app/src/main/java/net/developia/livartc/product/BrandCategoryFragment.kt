package net.developia.livartc.product

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ImageView
import android.widget.Spinner
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import net.developia.livartc.R
import net.developia.livartc.adapter.ProductAdapter
import net.developia.livartc.databinding.FragmentBrandCategoryPageBinding
import net.developia.livartc.model.Product
import net.developia.livartc.retrofit.RetrofitInstance
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

/**
 * LIVARTC
 * Created by 오수영
 * Date: 1/28/24
 * Time: 3:38 AM
 */
class BrandCategoryFragment : Fragment() {
    private lateinit var binding: FragmentBrandCategoryPageBinding
    private var type: String? = null // "brand" 또는 "category"
    private var name: String? = null // 브랜드명 또는 카테고리명
    private lateinit var sortingSpinner: Spinner
    private var selectedSortOption: Int = 4


    companion object {
        private const val TAG = "BrandCategoryFragment"

        fun newInstance(type: String, name: String): BrandCategoryFragment {
            Log.d(TAG, "newInstance - type: $type, name: $name") // newInstance 호출 로그

            val fragment = BrandCategoryFragment()
            val args = Bundle().apply {
                putString("type", type)
                putString("name", name)
            }
            fragment.arguments = args
            return fragment
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        Log.d(TAG, "onCreateView - type: $type, name: $name") // onCreateView 호출 로그

        binding = FragmentBrandCategoryPageBinding.inflate(inflater, container, false)
        arguments?.let {
            type = it.getString("type")
            name = it.getString("name")
        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Log.d(TAG, "onViewCreated - type: $type, name: $name") // onViewCreated 호출 로그

        val backButton = view.findViewById<ImageView>(R.id.back_arrow)
        backButton?.setOnClickListener {
            activity?.onBackPressed()
        }

        // 브랜드와 카테고리를 구분
        when (type) {
            "brand" -> setupForBrand(name)
            "category" -> setupForCategory(name)
        }
        setupSpinner()

    }

    private fun setupForBrand(brandName: String?) {
        brandName?.let { name ->
            Log.d(TAG, "setupForBrand - name: $name") // setupForBrand 호출 로그

            val headerTitle = view?.findViewById<TextView>(R.id.headerTitle)
            headerTitle?.text = name // 헤더변경

            // 브랜드 이미지 설정
            val resourceName = "inner_" + if (name == "리바트 세계가구") "levart" else name.replace("\\s+".toRegex(), "_").toLowerCase()
            val imageResId = resources.getIdentifier(resourceName, "drawable", requireContext().packageName)
            binding.brandImage.setImageResource(imageResId)

            // 브랜드 설명 설정
            val descriptionResId = resources.getIdentifier(if (name == "리바트 세계가구") "levart" else name, "string", requireContext().packageName)
            Log.d(TAG, "setupForBrand - resourceName: $resourceName, imageResId: $imageResId ,descriptionResId: $descriptionResId") // setupForBrand 호출 로그
            if (descriptionResId != 0) {
                binding.brandDescription.setText(descriptionResId)
            } else {
                // 리소스가 없는 경우 대체 텍스트 설정
                binding.brandDescription.text = "리소스가 없습니다"
            }

            loadProductsForBrand(name,selectedSortOption)
        }
    }

    private fun setupForCategory(categoryName: String?) {
        categoryName?.let { name ->
            Log.d(TAG, "setupForCategory - name: $name")

            val headerTitle = view?.findViewById<TextView>(R.id.headerTitle)
            headerTitle?.text = name

            binding.brandImage.visibility = View.GONE
            binding.brandDescription.visibility = View.GONE

            val layoutParams = binding.nestedScrollView.layoutParams as ConstraintLayout.LayoutParams
            layoutParams.topToTop = R.id.guidelineTitle
            binding.nestedScrollView.layoutParams = layoutParams

            loadProductsForCategory(name, selectedSortOption)
        }
    }


    private fun loadProductsForBrand(brandName: String, sortOption: Int) {
        // Brand 검색 API 호출
        RetrofitInstance.api.searchProducts(
            category = "",
            brand = brandName,
            hashtag = "",
            title = "",
            sortOption = sortOption,
            pageSize = 8,
            pageNumber = 1
        ).enqueue(object : Callback<List<Product>> {
            override fun onResponse(call: Call<List<Product>>, response: Response<List<Product>>) {
                if (response.isSuccessful) {
                    val products = response.body() ?: emptyList()
                    displayProducts(products)
                } else {
                    Log.e(TAG, "Failed to load products for brand: $brandName")
                }
            }

            override fun onFailure(call: Call<List<Product>>, t: Throwable) {
                Log.e(TAG, "Error loading products for brand: $brandName", t)
            }
        })
    }

    private fun loadProductsForCategory(categoryName: String, sortOption: Int) {
        // 카테고리 검색 API 호출
        RetrofitInstance.api.searchProducts(
            category = categoryName,
            brand = "",
            hashtag = "",
            title = "", // 타이틀 검색이 아니므로 비워둡니다.
            sortOption = sortOption, // 정렬 옵션, 필요에 따라 설정
            pageSize = 8,
            pageNumber = 1
        ).enqueue(object : Callback<List<Product>> {
            override fun onResponse(call: Call<List<Product>>, response: Response<List<Product>>) {
                if (response.isSuccessful) {
                    val products = response.body() ?: emptyList()
                    displayProducts(products)
                } else {
                    Log.e(TAG, "Failed to load products for category: $categoryName")
                }
            }

            override fun onFailure(call: Call<List<Product>>, t: Throwable) {
                Log.e(TAG, "Error loading products for category: $categoryName", t)
            }
        })
    }

    private fun setupSpinner() {
        sortingSpinner = binding.root.findViewById(R.id.spinnerSortOptions)
        sortingSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View?, position: Int, id: Long) {
                val selectedOption = parent.getItemAtPosition(position).toString()
                selectedSortOption = when (selectedOption) {
                    "최신순" -> 4
                    "이름순" -> 3
                    "낮은가격순" -> 1
                    "높은가격순" -> 2
                    "판매량순" -> 5
                    "조회수순" -> 6
                    else -> 4
                }

                Log.d(TAG, "Selected sort option: $selectedOption ($selectedSortOption)")

                if (type == "brand") {
                    Log.d(TAG, "Loading products for brand: $name, $selectedSortOption")
                    loadProductsForBrand(name ?: "", selectedSortOption)
                } else if (type == "category") {
                    Log.d(TAG, "Loading products for category: $name, $selectedSortOption")
                    loadProductsForCategory(name ?: "", selectedSortOption)
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>) {
                Log.d(TAG, "No sort option selected")
            }
        }
    }


    private fun displayProducts(products: List<Product>) {
        binding.productRecyclerView.apply {
            layoutManager = GridLayoutManager(context, 2) // Using a grid layout with 2 columns
            adapter = ProductAdapter(products) { product ->
                Log.d(TAG, "Product clicked: ${product.productName}")
                showProductDetail(product)
            }
        }
    }
    private fun showProductDetail(product: Product) {
        Log.d("BrandCategoryFragment to DetailFragment","$product")
        val bundle = Bundle().apply {
            putSerializable("product",product)
        }
        val detailFragment = DetailFragment().apply {
            arguments = bundle
        }

        fragmentManager?.beginTransaction()?.replace(R.id.product_container, detailFragment)?.addToBackStack(null)?.commit()

    }
}
