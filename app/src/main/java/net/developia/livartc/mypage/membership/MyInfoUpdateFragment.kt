package net.developia.livartc.mypage.membership

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.hyundai.loginapptest.domain.MemberResDto
import net.developia.livartc.databinding.FragmentMyInfoUpdateBinding
import net.developia.livartc.login.TokenManager
import net.developia.livartc.mypage.dto.MemperUpdateReqDto
import net.developia.livartc.retrofit.MyApplication
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MyInfoUpdateFragment : Fragment() {
    private var _binding: FragmentMyInfoUpdateBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentMyInfoUpdateBinding.inflate(inflater, container, false)
        val view = binding.root
        Log.d("MyInfoUpdateFragment", "수정 페이지로 이동")

        // "회원 정보 수정하기" 버튼 클릭 리스너 설정
        binding.btnEdit.setOnClickListener {
            // EditText에서 텍스트 가져오기
            Log.d("수정하기 버튼 클릭", "버튼 클릭")

            val nameValue = if (binding.etName.text.isNotBlank()) binding.etName.text.toString() else null
            val emailValue = if (binding.etEmail.text.isNotBlank()) binding.etEmail.text.toString() else null
            val nicknameValue = if (binding.etNickname.text.isNotBlank()) binding.etNickname.text.toString() else null
            val phoneValue = if (binding.etPhone.text.isNotBlank()) binding.etPhone.text.toString() else null
            val addressValue = if (binding.etAddress.text.isNotBlank()) binding.etAddress.text.toString() else null
            val birthValue = if (binding.etBirth.text.isNotBlank()) binding.etBirth.text.toString() else null
            if (nameValue != null) {
                Log.d("nameValue", nameValue)
            }

            // 로그에 찍기
            Log.d("EditTextValues", "Name: $nameValue, Email: $emailValue, Nickname: $nicknameValue, " +
                    "Phone: $phoneValue, Address: $addressValue, Birth: $birthValue")

            var memperUpdateReqDto = MemperUpdateReqDto()
            memperUpdateReqDto.name = nameValue
//            memperUpdateReqDto.email = emailValue
            memperUpdateReqDto.nickname = nicknameValue
            memperUpdateReqDto.address = addressValue
            memperUpdateReqDto.name = nameValue

            updateMemberInfo(memperUpdateReqDto)

            activity?.supportFragmentManager?.popBackStack()
        }
        return view
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun updateMemberInfo(memperUpdateReqDto : MemperUpdateReqDto) {
        val networkService = (MyApplication.instance).networkService
        val jwtToken = TokenManager.getToken(MyApplication.instance)!!

        val updateInfoCall = networkService.updateMember(jwtToken, memperUpdateReqDto)
        Log.d("회원 수정할 DTO", updateInfoCall.toString())

        updateInfoCall.enqueue(object : Callback<MemberResDto> {
            override fun onResponse(call: Call<MemberResDto>, response: Response<MemberResDto>) {
                if (response.isSuccessful) {
                    Log.d("회원 수정 API 성공", response.body().toString())
                } else {
                    Log.d("회원 수정 API 실패", "서버 응답 실패")
                }
            }
            override fun onFailure(call: Call<MemberResDto>, t: Throwable) {
                Log.d("회원 수정 API 실패", "네트워크 오류: $t")
            }
        })
    }

}