package net.developia.livartc

import android.os.Bundle
import android.util.Log
import android.view.WindowInsetsAnimation
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import net.developia.livartc.adapter.StraggeredAdapter
import net.developia.livartc.databinding.ActivityStraggeredBinding
import net.developia.livartc.model.Product
import net.developia.livartc.retrofit.RetrofitInstance
import retrofit2.Callback
import retrofit2.Call
import retrofit2.Response

/**
 * LIVARTC
 * Created by 오수영
 * Date: 1/29/24
 * Time: 3:39 AM
 */
class StraggeredActivity : AppCompatActivity() {
    private lateinit var binding: ActivityStraggeredBinding
    private val hashTags = listOf("#튼튼한", "#가벼운", "#포근한", "#푹신한", "#소프트",
        "#시원한", "#시크한", "#차분한", "#차분한", "#따뜻한", "#빈티지", "#엔티크", "#트렌디", "#럭셔리")
    private val productsMap = mutableMapOf<String, List<Product>>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityStraggeredBinding.inflate(layoutInflater)
        setContentView(binding.root)

        Log.d("StraggeredActivity", "onCreate: 시작")

        loadProductsForHashTags()
    }

    private fun loadProductsForHashTags() {
        val loadedTags = mutableListOf<String>() // 이미 로딩된 해시태그를 추적하기 위한 리스트

        hashTags.forEach { hashTag ->
            RetrofitInstance.api.searchProducts("", "", hashTag, "", 4, 200, 1)
                .enqueue(object : Callback<List<Product>> {
                    override fun onResponse(call: Call<List<Product>>, response: Response<List<Product>>) {
                        if (response.isSuccessful) {
                            val products = response.body() ?: emptyList()
                            productsMap[hashTag] = products
                            Log.d("StraggeredActivity", "API 응답: 해시태그 = $hashTag, 제품 수 = ${products.size}")

                            loadedTags.add(hashTag) // 해당 해시태그 데이터 로딩 완료

                            // 모든 해시태그에 대한 데이터 로딩이 완료되면 displayProducts 호출
                            if (loadedTags.size == hashTags.size) {
                                Log.d("StraggeredActivity", "모든 데이터 로드 완료")
                                runOnUiThread {
                                    displayProducts()
                                }
                            }
                        } else {
                            Log.e("StraggeredActivity", "응답 실패: ${response.errorBody()?.string() ?: "Unknown error"}")
                        }
                    }

                    override fun onFailure(call: Call<List<Product>>, t: Throwable) {
                        Log.e("StraggeredActivity", "API 요청 실패: $t")
                    }
                })
        }
    }


    private fun displayProducts() {
        val layoutManager = StaggeredGridLayoutManager(3, StaggeredGridLayoutManager.VERTICAL)
        binding.straggeredRecyclerView.layoutManager = layoutManager

        val adapter = StraggeredAdapter(hashTags, productsMap)
        binding.straggeredRecyclerView.adapter = adapter
        adapter.notifyDataSetChanged() // 어댑터에 데이터 변경 알림

        Log.d("StraggeredActivity", "UI 업데이트: 제품 표시")
    }

}