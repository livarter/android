package net.developia.livartc

import android.content.Intent
import android.icu.lang.UCharacter.GraphemeClusterBreak.T
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.core.content.ContentProviderCompat.requireContext
import net.developia.livartc.databinding.ActivityProductBinding
import net.developia.livartc.model.Product
import net.developia.livartc.product.DetailFragment
import net.developia.livartc.product.FilteredProductFragment
import net.developia.livartc.product.SearchFragment
/**
 * LIVARTC
 * Created by 최현서
 * Date: 2024-01-19
 * Time: 오후 2:32
 */
class ProductActivity : AppCompatActivity() {
    lateinit var binding: ActivityProductBinding
    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProductBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        var title = intent.getStringExtra("title")

        val bundle = Bundle() // 번들 생성(fragment로 값 전달할땐 번들!! 액티비티론 intent)
        bundle.putString("title", title)

        when(title) {
            "Search" -> {
                val searchFragment = SearchFragment()
                searchFragment.arguments = bundle
                supportFragmentManager.beginTransaction().replace(R.id.product_container, searchFragment).commit()
                true
            }
            "bestProductDetail" -> {
                val bundle = Bundle().apply {
                    putString("best", "best")
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                        putSerializable("product", intent.getSerializableExtra("product",Product::class.java))
                    } else {
                        putSerializable("product", intent.getSerializableExtra("product") as Product?)
                    }
                }
                val detailFragment = DetailFragment().apply {
                    arguments = bundle
                }
                supportFragmentManager.beginTransaction().replace(R.id.product_container, detailFragment).commit()
                true
            }
            else -> {
                val filteredProductFragment = FilteredProductFragment()
                filteredProductFragment.arguments = bundle
                supportFragmentManager.beginTransaction().replace(R.id.product_container, filteredProductFragment).commit()
                true
            }
        }

    }
}