package net.developia.livartc.main

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import net.developia.livartc.ProductActivity
import net.developia.livartc.R
import net.developia.livartc.StraggeredActivity
import net.developia.livartc.databinding.FragmentCollectionsBinding

class CollectionsFragment : Fragment() {
    lateinit var binding: FragmentCollectionsBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentCollectionsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupBrandClickListeners()

        binding.brand12.setOnClickListener {
            // StraggeredActivity 시작
            val intent = Intent(activity, StraggeredActivity::class.java)
            startActivity(intent)
        }
    }

    private fun setupBrandClickListeners() {
        val brandNames = resources.getStringArray(R.array.brand_names)
        val brandImageViews = listOf(
            binding.brand1, binding.brand2, binding.brand3, binding.brand4,
            binding.brand5, binding.brand6, binding.brand7, binding.brand8,
            binding.brand9, binding.brand10, binding.brand11, binding.brand12
        )

        brandImageViews.forEachIndexed { index, imageView ->
            val brandName = brandNames.getOrElse(index) { "" }
            imageView.setOnClickListener {
                val intent = Intent(activity, ProductActivity::class.java).apply {
                    putExtra("name", brandName)
                    putExtra("type", "brand") // "brand" 유형을 나타내는 추가 정보
                }
                startActivity(intent)
            }
        }
    }
}
