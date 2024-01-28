package net.developia.livartc
import android.content.Intent
import android.icu.lang.UCharacter.GraphemeClusterBreak.T
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import net.developia.livartc.product.BrandCategoryFragment
import androidx.core.content.ContentProviderCompat.requireContext
import net.developia.livartc.databinding.ActivityProductBinding
import net.developia.livartc.model.Product
import net.developia.livartc.product.DetailFragment
import net.developia.livartc.product.FilteredProductFragment
import net.developia.livartc.product.SearchFragment

class ProductActivity : AppCompatActivity() {
    private lateinit var binding: ActivityProductBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProductBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val type = intent.getStringExtra("type")
        val name = intent.getStringExtra("name") ?: ""

        var title = intent.getStringExtra("title")
        val bundle = Bundle() // 번들 생성(fragment로 값 전달할땐 번들!! 액티비티론 intent)
        bundle.putString("title", title)

        when(type) {
            "Search" -> {
                Log.d("ProductActivity", "Launching SearchFragment")
                val searchFragment = SearchFragment()
                supportFragmentManager.beginTransaction()
                    .replace(R.id.product_container, searchFragment)
                    .commit()
            }
            "brand", "category" -> {
                Log.d("ProductActivity", "Launching BrandCategoryFragment - Type: $type, Name: $name")
                val brandCategoryFragment = BrandCategoryFragment.newInstance(type, name)
                supportFragmentManager.beginTransaction()
                    .replace(R.id.product_container, brandCategoryFragment)
                    .commit()
            }
            "detail" -> {
                val bundle = Bundle().apply {
                    putString("backMode", "finish")
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
                Log.d("ProductActivity", "Launching FilteredProductFragment")
                val filteredProductFragment = FilteredProductFragment().apply {
                }
                supportFragmentManager.beginTransaction()
                    .replace(R.id.product_container, filteredProductFragment)
                    .commit()
            }
        }
    }
}

