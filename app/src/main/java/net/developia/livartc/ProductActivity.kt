package net.developia.livartc

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import android.util.Log
import net.developia.livartc.databinding.ActivityProductBinding
import net.developia.livartc.product.BrandCategoryFragment
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

        when (type) {
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

