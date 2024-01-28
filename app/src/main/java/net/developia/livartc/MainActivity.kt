package net.developia.livartc

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import net.developia.livartc.databinding.ActivityMainBinding
import net.developia.livartc.main.CartFragment
import net.developia.livartc.main.CollectionsFragment
import net.developia.livartc.main.HomeFragment
import net.developia.livartc.main.MyPageFragment
/**
 * LIVARTC
 * Created by 최현서
 * Date: 2024-01-19
 * Time: 오후 2:32
 */
class MainActivity : AppCompatActivity() {
    lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        setBottomNavigationView()
        // 앱 초기 실행 시 홈화면으로 설정
        if (savedInstanceState == null) {
            val startFragment = intent.getStringExtra("startFragment")
            when (startFragment) {
                "HomeFragment" -> binding.bottomNavigationView.selectedItemId = R.id.fragment_home
                "MyPageFragment" -> binding.bottomNavigationView.selectedItemId = R.id.fragment_mypage
                else -> binding.bottomNavigationView.selectedItemId = R.id.fragment_home
            }
        }
    }

    override fun onBackPressed() {

        when (supportFragmentManager.findFragmentById(R.id.main_container)) {
            is CollectionsFragment, is CartFragment, is MyPageFragment -> {
                // Replace with HomeFragment when pressing back from other fragments
                binding.bottomNavigationView.selectedItemId = R.id.fragment_home
            }
            is HomeFragment -> {
                // If the current fragment is HomeFragment, perform default back behavior
                super.onBackPressed()
            }
            else -> super.onBackPressed()
        }
    }

    private fun setBottomNavigationView() {
        binding.bottomNavigationView.setOnItemSelectedListener {item ->
            when(item.itemId) {

                R.id.fragment_home -> {
                    binding.mainTitle.text = "LIVΛRTC"
                    supportFragmentManager.beginTransaction().replace(R.id.main_container, HomeFragment()).commit()
                    true
                }
                R.id.fragment_collections -> {
                    binding.mainTitle.text = "Collections"
                    supportFragmentManager.beginTransaction().replace(R.id.main_container, CollectionsFragment()).commit()
                    true
                }
                R.id.fragment_search -> {
                    binding.mainTitle.text = "Search"
                    val intent = Intent(this, ProductActivity::class.java)
                    intent.putExtra("type", "Search")
                    startActivity(intent)
                    false
                }
                R.id.fragment_cart -> {
                    binding.mainTitle.text = "MyCart"
                    supportFragmentManager.beginTransaction().replace(R.id.main_container, CartFragment()).commit()
                    true
                }
                R.id.fragment_mypage -> {
                    binding.mainTitle.text = "Profile"
                    supportFragmentManager.beginTransaction().replace(R.id.main_container, MyPageFragment()).commit()
                    true
                }
                else -> false
            }
        }
    }

    fun startProductActivity(title: String) {
        val intent = Intent(this, ProductActivity::class.java)
        intent.putExtra("title", title)
        startActivity(intent)
    }
    fun startPurchaseActivity(userid: String) {
        val intent = Intent(this, PurchaseActivity::class.java)
        intent.putExtra("userid", userid)
        startActivity(intent)
    }

    fun startProductActivityWithType(name: String, type: String) {
        val intent = Intent(this, ProductActivity::class.java).apply {
            putExtra("name", name)
            putExtra("type", type)
        }
        startActivity(intent)
    }

}