package net.developia.livartc

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import net.developia.livartc.databinding.ActivityMainBinding
import net.developia.livartc.main.CartFragment
import net.developia.livartc.main.CollectionsFragment
import net.developia.livartc.main.HomeFragment
import net.developia.livartc.main.MyPageFragment
/**
 * LIVARTC
 * Created by 최현서
 * Date: 2024-01-19
 * Time: 14:32
 * 작업 내용: 메인 화면 하단 네비게이션 바에 의한 프레그먼트/액티비티 전환 설정
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
            when (intent.getStringExtra("startFragment")) {
                "HomeFragment" -> binding.bottomNavigationView.selectedItemId = R.id.fragment_home
                "MyPageFragment" -> binding.bottomNavigationView.selectedItemId = R.id.fragment_mypage
                else -> binding.bottomNavigationView.selectedItemId = R.id.fragment_home
            }
        }
    }

    override fun onBackPressed() {

        when (supportFragmentManager.findFragmentById(R.id.main_container)) {
            is CollectionsFragment, is CartFragment, is MyPageFragment -> {
                binding.bottomNavigationView.selectedItemId = R.id.fragment_home
            }
            is HomeFragment -> {
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