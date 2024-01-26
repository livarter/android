package net.developia.livartc

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import net.developia.livartc.databinding.ActivityPurchaseBinding
import net.developia.livartc.purchase.BillsFragment
/**
 * LIVARTC
 * Created by 최현서
 * Enhanced by 변형준
 * Date: 2024-01-19
 * Time: 오후 2:32
 */
class PurchaseActivity : AppCompatActivity() {
    lateinit var binding: ActivityPurchaseBinding

    // PurchaseActivity에서 BillsFragment로 totalPrice를 전달
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPurchaseBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        val totalPrice = intent.getLongExtra("totalPrice", 0)
        val bundle = Bundle().apply { putLong("totalPrice", totalPrice) }
        val billsFragment = BillsFragment().apply { arguments = bundle }
        val backButton: ImageView = findViewById(R.id.back_btn)

        // 클릭 이벤트 리스너를 설정합니다.
        backButton.setOnClickListener {
            onBackPressed()
        }
        supportFragmentManager.beginTransaction().replace(R.id.purchase_container, billsFragment).commit()
    }

    override fun onBackPressed() {
        super.onBackPressed()
        finish()
    }

}