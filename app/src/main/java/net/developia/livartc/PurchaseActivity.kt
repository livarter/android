package net.developia.livartc

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import net.developia.livartc.databinding.ActivityPurchaseBinding
import net.developia.livartc.purchase.BillsFragment
/**
 * LIVARTC
 * Created by 최현서
 * Date: 2024-01-19
 * Time: 오후 2:32
 */
class PurchaseActivity : AppCompatActivity() {
    lateinit var binding: ActivityPurchaseBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPurchaseBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        supportFragmentManager.beginTransaction().replace(R.id.purchase_container, BillsFragment()).commit()
    }
}