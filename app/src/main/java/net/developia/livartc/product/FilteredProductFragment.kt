package net.developia.livartc.product

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import net.developia.livartc.R
import net.developia.livartc.databinding.FragmentFilteredProductBinding
import net.developia.livartc.main.CategoryFragment


class FilteredProductFragment : Fragment() {
    lateinit var binding: FragmentFilteredProductBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentFilteredProductBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.productList1.setOnClickListener {
            parentFragmentManager.beginTransaction().apply{
                replace(R.id.main_container, CategoryFragment())
                //addToBackStack(null)메서드는, 프레그먼트를 replace했을때, 뒤로가기를 가능하게 하는 함수
                addToBackStack(null)
                commit()
            }
        }
        binding.mainTitle.text = arguments?.getString("title")?:"LIVΛRT"
        binding.productList1.setOnClickListener {
            moveToDetail()
        }
    }

    fun moveToDetail() {
        parentFragmentManager.beginTransaction().apply{
            replace(R.id.product_container, DetailFragment())
            //addToBackStack(null)메서드는, 프레그먼트를 replace했을때, 뒤로가기를 가능하게 하는 함수
            addToBackStack(null)
            commit()
        }
    }

}