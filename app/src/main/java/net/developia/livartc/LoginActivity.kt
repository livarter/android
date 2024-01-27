package net.developia.livartc

import android.app.Activity
import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Base64
import android.util.Log
import android.view.View
import com.hyundai.loginapptest.domain.LoginReqDto
import com.hyundai.loginapptest.domain.LoginResDto
import com.kakao.sdk.auth.model.OAuthToken
import com.kakao.sdk.common.model.ClientError
import com.kakao.sdk.common.model.ClientErrorCause
import com.kakao.sdk.user.UserApiClient
import net.developia.livartc.databinding.ActivityLoginBinding
import net.developia.livartc.login.TokenManager
import net.developia.livartc.retrofit.MyApplication
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.security.MessageDigest
import java.security.NoSuchAlgorithmException

/**
 * LIVARTC
 * Created by 최현서
 * Date: 2024-01-19
 * Time: 오후 2:32
 */
class LoginActivity : AppCompatActivity() {
    lateinit var binding: ActivityLoginBinding

    /**
     * 작성자 : 황수영
     * 내용 : 카카오 서버측에 로그인
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        binding.progressBar.visibility = View.GONE
        Log.d("KAKAO 키 해시 발급", "" + getKeyHash(this));

        val intent = Intent(this, MainActivity::class.java)

        val callback: (OAuthToken?, Throwable?) -> Unit = { token, error ->
            if (error != null) {
                Log.d(ContentValues.TAG, "카카오 계정으로 로그인 실패", error)
                binding.progressBar.visibility = View.GONE
                binding.kakaoBtn.visibility = View.VISIBLE
            } else if (token != null) {
                Log.d(ContentValues.TAG, "카카오 계정으로 로그인 성공 access token ${token.accessToken}")
                // 백단에 API 요청
                loginWithKakao(this, token.accessToken)
            }
        }

        binding.kakaoBtn.setOnClickListener{
            Log.d("카카오 로그인 버튼 클릭", "클릭")

            // 로그인 버튼을 누르면 로딩 화면을 표시하고, 로그인 버튼을 숨깁니다.
            binding.progressBar.visibility = View.VISIBLE
            binding.kakaoBtn.visibility = View.GONE

            if (UserApiClient.instance.isKakaoTalkLoginAvailable(this)) {
                UserApiClient.instance.loginWithKakaoTalk(this) { token, error ->
                    if (error != null) {
                        Log.d(ContentValues.TAG, "카카오톡으로 로그인 실패", error)
                        if (error is ClientError && error.reason == ClientErrorCause.Cancelled) {
                            return@loginWithKakaoTalk
                        }
                        UserApiClient.instance.loginWithKakaoAccount(this, callback = callback)
                        binding.progressBar.visibility = View.GONE
                        binding.kakaoBtn.visibility = View.VISIBLE
                    } else if (token != null) {
                        Log.d(ContentValues.TAG, "카카오톡으로 로그인 성공 ${token.accessToken}")
                        // 백단에 API 요청
                        loginWithKakao(this, token.accessToken)
                    }
                }
            } else {
                UserApiClient.instance.loginWithKakaoAccount(this, callback = callback)
            }
        }
    }

    /**
     * 작성자 : 황수영
     * 내용 : 카카오 access token으로 서버 측 로그인
     */
    private fun loginWithKakao(context : Context, accessToken : String) : String{
        val loginReqDto = LoginReqDto(loginToken = accessToken)
        var jwtToken = "" // 서버의 JWT 토큰

        Thread {
            val networkService = (context?.applicationContext as MyApplication).networkService
            var memberResDto = networkService.kakaoLogin(loginReqDto)
            Log.d("API 시작", loginReqDto.toString())

            memberResDto.enqueue(object : Callback<LoginResDto> {
                override fun onResponse(call: Call<LoginResDto>, response: Response<LoginResDto>) {
                    var loginResDto = response.body()!!

                    if (loginResDto != null) {
                        Log.d("로그인 API 테스트 성공", loginResDto.toString())
                        Log.d("자체 JWT 토큰 발급 성공", loginResDto.accessToken)
                        jwtToken = loginResDto.accessToken
                        TokenManager.saveToken(MyApplication.instance, jwtToken)

                        val intent = Intent(context, MainActivity::class.java)
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP)
                        startActivity(intent)
                        (context as Activity).finish()

                    }
                }
                override fun onFailure(call: Call<LoginResDto>, t: Throwable) {
                    Log.d("로그인 API 테스트 실패", t.toString())
                }
            })
        }.start()

        return jwtToken
    }

    /**
     * 작성자 : 황수영
     * 내용 : 카카오 로그인 등록용 키 해시 발급
     */
    fun getKeyHash(context: Context): String? {
        val pm = context.packageManager
        try {
            val packageInfo = pm.getPackageInfo(context.packageName, PackageManager.GET_SIGNATURES)
                ?: return null
            for (signature in packageInfo.signatures) {
                try {
                    val md = MessageDigest.getInstance("SHA")
                    md.update(signature.toByteArray())
                    return Base64.encodeToString(md.digest(), Base64.NO_WRAP)
                } catch (e: NoSuchAlgorithmException) {
                    e.printStackTrace()
                }
            }
        } catch (e: PackageManager.NameNotFoundException) {
            e.printStackTrace()
        }
        return null
    }
}