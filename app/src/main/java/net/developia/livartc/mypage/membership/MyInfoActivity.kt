package net.developia.livartc.mypage.membership

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import com.hyundai.loginapptest.domain.MemberResDto
import net.developia.livartc.R
import net.developia.livartc.login.TokenManager
import net.developia.livartc.mypage.dto.toFormattedString
import net.developia.livartc.retrofit.MyApplication
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

/**
 * 작성자 : 황수영
 * 내용 : 멤버십 탭 중 나의 정보 페이지 구현
 */
class MyInfoActivity : AppCompatActivity(), OnUpdateListener {
    lateinit var memberResDto : MemberResDto
    override fun onUpdate(memberResDto: MemberResDto) {
        setupMemberInfo(memberResDto)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        getMemberInfo()
    }

    private fun getMemberInfo() {
        val networkService = (MyApplication.instance).networkService
        val jwtToken = TokenManager.getToken(MyApplication.instance)!!

        val getMemberInfo = networkService.getMemberInfo(jwtToken)
        Log.d("조회 화면) 회원 정보 조회 API 시작", getMemberInfo.toString())

        getMemberInfo.enqueue(object : Callback<MemberResDto> {
            override fun onResponse(call: Call<MemberResDto>, response: Response<MemberResDto>) {
                if (response.isSuccessful) {
                    Log.d("조회 화면) 회원 정보 조회 API 성공", "서버 응답 성공")
                    memberResDto = response.body()!!
                    setupMemberInfo(memberResDto)
                    Log.d("조회 화면) 회원 정보 조회 API res", memberResDto.toString())
                } else {
                    Log.d("조회 화면) 회원 정보 조회 API 실패", "서버 응답 실패")
                }
            }
            override fun onFailure(call: Call<MemberResDto>, t: Throwable) {
                Log.d("조회 화면) 회원 정보 조회 API 실패", "네트워크 오류" + t.toString())
            }
        })

        setContentView(R.layout.activity_my_info)

        val btnEdit = findViewById<Button>(R.id.btnEdit)
        btnEdit.setOnClickListener {
            Log.d("수정하기 버튼 클릭", "이동 전")

            val fragment = MyInfoUpdateFragment()

            val bundle = Bundle()
            bundle.putSerializable("member", memberResDto)

            fragment.arguments = bundle
            supportFragmentManager.beginTransaction()
                .replace(R.id.fragmentContainer, fragment)
                .addToBackStack(null)
                .commit()

            Log.d("수정하기 버튼 클릭", "이동 후")
        }

    }

    private fun setupMemberInfo(memberResDto: MemberResDto) {
        val profileImage = findViewById<ImageView>(R.id.profile_image)
        Glide.with(this)
            .load(memberResDto.image)
            .into(profileImage)

        findViewById<TextView>(R.id.textNameValue).setTextOrPlaceholder(memberResDto.name)
        findViewById<TextView>(R.id.textEmailValue).setTextOrPlaceholder(memberResDto.email)
        findViewById<TextView>(R.id.textNicknameValue).setTextOrPlaceholder(memberResDto.nickname)
        findViewById<TextView>(R.id.textPhoneValue).setTextOrPlaceholder(memberResDto.phone)
        findViewById<TextView>(R.id.textZipCodeValue).setTextOrPlaceholder(memberResDto.zipCode)
        findViewById<TextView>(R.id.textAddressValue).setTextOrPlaceholder(memberResDto.address)
        findViewById<TextView>(R.id.textBirthValue).setTextOrPlaceholder(memberResDto.birthDate.toFormattedString())
    }

    private fun TextView.setTextOrPlaceholder(value: String?, placeholder: String = "아직 등록되지 않았습니다.") {
        text = value ?: placeholder
    }
}