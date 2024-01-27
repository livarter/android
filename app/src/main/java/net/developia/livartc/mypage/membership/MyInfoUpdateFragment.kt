package net.developia.livartc.mypage.membership

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

/**
 * 작성자 : 황수영
 * 내용 : 멤버십 탭 중 나의 정보 페이지에서 수정
 */
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

        // 수정 화면에 보여주기
        val memberResDto = getMemberInfo()
        Log.d("getMemberInfo() 이후 memberResDto", getMemberInfo().toString())

        binding.etName.hint = memberResDto.name
        binding.etEmail.hint = memberResDto.email
        binding.etNickname.hint = memberResDto.nickname
        binding.etPhone.hint = memberResDto.phone
        binding.etZipCode.hint = memberResDto.zipCode
        binding.etAddress.hint = memberResDto.address
        binding.etBirth.hint = memberResDto.birthDate

        // 데이터가 정상적으로 가져오면 해당 데이터를 View에 설정
        if (memberResDto != null) {
            binding.etName.setText(memberResDto.name)
            binding.etNickname.setText(memberResDto.nickname)
            binding.etPhone.setText(memberResDto.phone)
            binding.etAddress.setText(memberResDto.address)
            binding.etBirth.setText(memberResDto.birthDate)
            binding.etZipCode.setText(memberResDto.zipCode)
        } else {
            // 데이터가 없는 경우 처리
            Log.d("MyInfoUpdateFragment", "회원 정보를 가져오지 못했습니다.")
        }

        // "회원 정보 수정하기" 버튼 클릭 리스너 설정
        binding.btnEdit.setOnClickListener {
            // EditText에서 텍스트 가져오기
            Log.d("수정하기 버튼 클릭", "버튼 클릭")

            val nameValue = if (binding.etName.text.isNotBlank()) binding.etName.text.toString() else null
            val nicknameValue = if (binding.etNickname.text.isNotBlank()) binding.etNickname.text.toString() else null
            val phoneValue = if (binding.etPhone.text.isNotBlank()) binding.etPhone.text.toString() else null
            val addressValue = if (binding.etAddress.text.isNotBlank()) binding.etAddress.text.toString() else null
            val zipCodeValue = if (binding.etAddress.text.isNotBlank()) binding.etZipCode.text.toString() else null
            val birthValue = if (binding.etBirth.text.isNotBlank()) binding.etBirth.text.toString() else null

            // 로그에 찍기
            Log.d("EditTextValues", "Name: $nameValue, Nickname: $nicknameValue, "
                    + "Phone: $phoneValue, Address: $addressValue, Birth: $birthValue")

            var memperUpdateReqDto = MemperUpdateReqDto()
            memperUpdateReqDto.name = nameValue
            memperUpdateReqDto.nickname = nicknameValue
            memperUpdateReqDto.phone = phoneValue
            memperUpdateReqDto.address = addressValue
            memperUpdateReqDto.birthDate = birthValue
            memperUpdateReqDto.zipCode = zipCodeValue
            // memperUpdateReqDto.image = imageValue

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

    fun getMemberInfo() : MemberResDto {
        var memberResDto = MemberResDto()
        val networkService = (MyApplication.instance).networkService
        val jwtToken = TokenManager.getToken(MyApplication.instance)!!

        val getMemberInfo = networkService.getMemberInfo(jwtToken)
        Log.d("회원 정보 조회 API 시작", getMemberInfo.toString())

        getMemberInfo.enqueue(object : Callback<MemberResDto> {
            override fun onResponse(call: Call<MemberResDto>, response: Response<MemberResDto>) {
                if (response.isSuccessful) {
                    Log.d("회원 정보 조회 API 성공", response.body().toString())
                    memberResDto = response.body()!!

                } else {
                    Log.d("회원 정보 조회 API 실패", "서버 응답 실패")
                }
            }
            override fun onFailure(call: Call<MemberResDto>, t: Throwable) {
                Log.d("회원 정보 조회 API 실패", "네트워크 오류" + t.toString())
            }
        })

        Log.d("memberResDto", memberResDto.toString())

        return memberResDto
    }


}