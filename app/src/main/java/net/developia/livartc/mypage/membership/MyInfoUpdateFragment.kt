package net.developia.livartc.mypage.membership

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.hyundai.loginapptest.domain.MemberResDto
import net.developia.livartc.R
import net.developia.livartc.databinding.FragmentMyInfoUpdateBinding
import net.developia.livartc.login.TokenManager
import net.developia.livartc.mypage.dto.BirthDate
import net.developia.livartc.mypage.dto.MemperUpdateReqDto
import net.developia.livartc.mypage.dto.toFormattedString
import net.developia.livartc.retrofit.MyApplication
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

/**
 * 작성자 : 황수영
 * 내용 : 멤버십 탭 중 나의 정보 페이지에서 수정
 */
class MyInfoUpdateFragment : Fragment() {
    private var binding: FragmentMyInfoUpdateBinding? = null
    private lateinit var view: View

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentMyInfoUpdateBinding.inflate(inflater, container, false)
        Log.d("MyInfoUpdateFragment", "수정 페이지로 이동")
        view = inflater.inflate(R.layout.fragment_my_info_update, container, false)

        val memberResDto = arguments?.getSerializable("member") as? MemberResDto
        Log.d("MemberResDto from Activity", memberResDto?.toString() ?: "No data")

        updateTextViewValues(memberResDto)
        binding?.etName?.setText(memberResDto?.name)
        binding?.etNickname?.setText(memberResDto?.nickname)
        binding?.etPhone?.setText(memberResDto?.phone)
        binding?.etZipCode?.setText(memberResDto?.zipCode)
        binding?.etAddress?.setText(memberResDto?.address)
        binding?.etBirth?.setText(memberResDto?.birthDate?.toFormattedString())

        binding?.btnEdit?.setOnClickListener {
            val updatedName = binding?.etName?.text.toString()
            val updatedNickname = binding?.etNickname?.text.toString()
            val updatedPhone = binding?.etPhone?.text.toString()
            val updatedZipCode = binding?.etZipCode?.text.toString()
            val updatedAddress = binding?.etAddress?.text.toString()
            val updatedBirth = binding?.etBirth?.text.toString()

            val updatedMemberResDto = MemperUpdateReqDto(
                name = updatedName,
                nickname = updatedNickname,
                phone = updatedPhone,
                address = updatedAddress,
                zipCode = updatedZipCode,
                birthDate = updatedBirth
            )

            // 서버로 정보 업데이트 요청
            updateMemberInfo(updatedMemberResDto)
            requireActivity().supportFragmentManager.popBackStack()

            val intent = Intent(requireContext(), MyInfoActivity::class.java)
            startActivity(intent)
            requireActivity().finish()
        }
        return binding?.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }

    private fun updateMemberInfo(memperUpdateReqDto : MemperUpdateReqDto) : MemberResDto{

        val networkService = (MyApplication.instance).networkService
        val jwtToken = TokenManager.getToken(MyApplication.instance)!!

        val updateInfoCall = networkService.updateMember(jwtToken, memperUpdateReqDto)
        Log.d("회원 수정할 DTO", updateInfoCall.toString())

        val memberResDto = MemberResDto()

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
        return memberResDto
    }

    private fun updateTextViewValues(memberResDto: MemberResDto?) {
        with(binding) {
            val profileImage = view.findViewById<ImageView>(R.id.profile_image)
            Glide.with(view)
                .load(memberResDto?.image)
                .into(profileImage)

            this?.textEmailValue?.hint = memberResDto?.email ?: ""
            this?.etName?.hint = memberResDto?.name ?: "이름을 입력해주세요."
            this?.etNickname?.hint = memberResDto?.nickname ?: "닉네임을 입력해주세요."
            this?.etPhone?.hint = memberResDto?.phone ?: "번호를 입력해주세요."
            this?.etZipCode?.hint = memberResDto?.zipCode ?: "우편 번호를 입력해주세요."
            this?.etAddress?.hint = memberResDto?.address ?: "주소를 입력해주세요."
            this?.etBirth?.hint = memberResDto?.birthDate?.toFormattedString() ?: "생년월일을 입력해주세요."
        }
    }
}