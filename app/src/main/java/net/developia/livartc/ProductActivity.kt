package net.developia.livartc

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.core.content.ContentProviderCompat.requireContext
import net.developia.livartc.databinding.ActivityProductBinding
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
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProductBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        var title = intent.getStringExtra("title")

        val bundle = Bundle() // 번들 생성(fragment로 값 전달할땐 번들!! 액티비티론 intent)
        bundle.putString("title", title)
        Log.d("hschoi", title?:"????")
        when(title) {
            "Search" -> {
                val searchFragment = SearchFragment()
                searchFragment.arguments = bundle
                supportFragmentManager.beginTransaction().replace(R.id.product_container, searchFragment).commit()
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