package net.developia.livartc.main

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import net.developia.livartc.MainActivity
import net.developia.livartc.R
import net.developia.livartc.databinding.FragmentCategoryBinding

class CategoryFragment : Fragment(), View.OnClickListener {
    lateinit var binding: FragmentCategoryBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentCategoryBinding.inflate(inflater,container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.bedBtn.setOnClickListener(this)
        binding.chairBtn.setOnClickListener(this)
        binding.deskBtn.setOnClickListener(this)
        binding.cabinetBtn.setOnClickListener(this)
        binding.decoBtn.setOnClickListener(this)
    }

    //카테고리별 이동
    override fun onClick(v: View?) {
        when(v?.id) {
            R.id.bed_btn -> (activity as MainActivity).startProductActivity("침대")
            R.id.chair_btn -> (activity as MainActivity).startProductActivity("의자")
            R.id.desk_btn -> (activity as MainActivity).startProductActivity("데스크")
            R.id.cabinet_btn -> (activity as MainActivity).startProductActivity("수납장")
            R.id.deco_btn -> (activity as MainActivity).startProductActivity("홈데코")
        }
    }
}