package net.developia.livartc.main

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import net.developia.livartc.MainActivity
import net.developia.livartc.R
import net.developia.livartc.databinding.FragmentHomeBinding

class HomeFragment : Fragment() {
    lateinit var binding: FragmentHomeBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        //category버튼 누르면 CategoryFragment로 변경
        binding.categoryBtn.setOnClickListener {
            parentFragmentManager.beginTransaction().apply{
                replace(R.id.main_container, CategoryFragment())
                //addToBackStack(null)메서드는, 프레그먼트를 replace했을때, 뒤로가기를 가능하게 하는 함수
                addToBackStack(null)
                commit()
            }
        }
        val bundle = Bundle()
        bundle.putString("title", "베스트상품")
            //베스트 조회 눌렀을 때 ProductActivity 생성(베스트순으로 필터링된 조회창으로 이동)
            binding.bestBtn.setOnClickListener {
                (activity as MainActivity).startProductActivity("베스트 상품")
            }

    }


}