package net.developia.livartc.mypage.membership

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.TextView
import com.hyundai.loginapptest.domain.MemberResDto
import net.developia.livartc.R
import net.developia.livartc.login.TokenManager
import net.developia.livartc.retrofit.MyApplication
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

/**
 * 작성자 : 황수영
 * 내용 : 나의 정보 페이지 구현
 */

class MyInfoActivity : AppCompatActivity(), OnUpdateListener {
    lateinit var memberResDto : MemberResDto

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        getMemberInfo()
    }

    override fun onUpdate() {
        getMemberInfo()
    }

    private fun getMemberInfo() {
        val networkService = (MyApplication.instance).networkService
        val jwtToken = TokenManager.getToken(MyApplication.instance)!!

        val getMemberInfo = networkService.getMemberInfo(jwtToken)
        Log.d("회원 정보 조회 API 시작", getMemberInfo.toString())

        getMemberInfo.enqueue(object : Callback<MemberResDto> {
            override fun onResponse(call: Call<MemberResDto>, response: Response<MemberResDto>) {
                if (response.isSuccessful) {
                    memberResDto = response.body()!!
                    setupMemberInfo(memberResDto)
                } else {
                    Log.d("회원 정보 조회 API 실패", "서버 응답 실패")
                }
            }
            override fun onFailure(call: Call<MemberResDto>, t: Throwable) {
                Log.d("회원 정보 조회 API 실패", "네트워크 오류" + t.toString())
            }
        })

        setContentView(R.layout.activity_my_info)

        // 수정하는 버튼
        val btnEdit = findViewById<Button>(R.id.btnEdit)
        btnEdit.setOnClickListener {
            Log.d("수정하기 버튼 클릭", "이동 전")

            // Begin the transaction
            supportFragmentManager.beginTransaction()
                .replace(R.id.fragmentContainer, MyInfoUpdateFragment()) // R.id.fragmentContainer is the container in your activity layout
                .addToBackStack(null)  // Optional: Add the transaction to the back stack
                .commit()

            Log.d("수정하기 버튼 클릭", "이동 후")
        }
    }


    private fun setupMemberInfo(memberResDto: MemberResDto) {
        // 이름 설정
        val textNameValue = findViewById<TextView>(R.id.textNameValue)
        textNameValue.text = memberResDto.name ?: "아직 등록되지 않았습니다."

        // 이메일 설정
        val textEmailValue = findViewById<TextView>(R.id.textEmailValue)
        textEmailValue.text = memberResDto.email ?: "아직 등록되지 않았습니다."

        // 닉네임 설정
        val textNicknameValue = findViewById<TextView>(R.id.textNicknameValue)
        textNicknameValue.text = memberResDto.nickname ?: "아직 등록되지 않았습니다."

        // 전화 번호 설정
        val textPhoneValue = findViewById<TextView>(R.id.textPhoneValue)
        textPhoneValue.text = memberResDto.name ?: "010-1234-5678"

        // 우편 번호 설정
        val textZipCodeValue = findViewById<TextView>(R.id.textZipCodeValue)
        textZipCodeValue.text = memberResDto.zipCode ?: "아직 등록되지 않았습니다."

        // 주소 설정
        val textAddressValue = findViewById<TextView>(R.id.textAddressValue)
        textAddressValue.text = memberResDto.address ?: "아직 등록되지 않았습니다."

        // 생년 월일 설정
        val textBirthValue = findViewById<TextView>(R.id.textBirthValue)
        textBirthValue.text = memberResDto.birthDate ?: "아직 등록되지 않았습니다."
    }
}