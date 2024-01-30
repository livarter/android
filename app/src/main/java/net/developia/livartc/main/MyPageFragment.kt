package net.developia.livartc.main

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.Glide
import com.google.android.material.tabs.TabLayoutMediator
import com.hyundai.loginapptest.domain.MemberResDto
import net.developia.livartc.databinding.FragmentMyPageBinding
import net.developia.livartc.login.TokenManager
import net.developia.livartc.mypage.ViewPagerAdapter
import net.developia.livartc.retrofit.MyApplication
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

/**
 * 작성자 : 황수영
 * 내용 : 마이페이지 뷰페이저 적용
 */

class MyPageFragment : Fragment() {
    lateinit var binding: FragmentMyPageBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentMyPageBinding.inflate(inflater, container, false)
        // 서버 API 호출 및 데이터 설정 예시
        fetchProfileData()

        // 뷰페이저에 어댑터 연결
        binding.pager.adapter = ViewPagerAdapter(requireActivity())

        /* 탭과 뷰페이저를 연결, 여기서 새로운 탭을 다시 만드므로 레이아웃에서 꾸미지말고
        여기서 꾸며야함
         */
        TabLayoutMediator(binding.tabLayout, binding.pager) { tab, position ->
            when (position) {
                0 -> tab.text = "멤버십"
                1 -> tab.text = "나의 뱃지"
                2 -> tab.text = "나의 방"
            }
        }.attach()

        return binding.root
    }

    /**
     * 작성자 : 황수영
     * 내용 : 마이페이지 회원 정보 조회
     */
    private fun fetchProfileData() {
        val networkService = (context?.applicationContext as MyApplication).networkService
        val jwtToken = TokenManager.getToken(MyApplication.instance)!!

        val getMemberInfo = networkService.getMemberInfo(jwtToken)
        Log.d("회원 정보 조회 API 시작", getMemberInfo.toString())

        getMemberInfo.enqueue(object : Callback<MemberResDto> {
            override fun onResponse(call: Call<MemberResDto>, response: Response<MemberResDto>) {
                Log.d("회원 정보 조회 API 성공 response", response.toString())
                if (response.isSuccessful) {
                    val resDto = response.body()
                    Log.d("회원 정보 조회 API 성공", resDto.toString())

                    resDto?.let {
                        binding.profileGrade.text = resDto.grade
                        Glide.with(requireContext())
                            .load(resDto.image)
                            .into(binding.profileImage)
                    }
                } else {
                    Log.d("회원 정보 조회 API 실패", "서버 응답 실패")
                }
            }

            override fun onFailure(call: Call<MemberResDto>, t: Throwable) {
                Log.d("회원 정보 조회 API 실패", "네트워크 오류" + t.toString())
            }
        })
    }
}
