package net.developia.livartc.mypage.membership

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import com.bumptech.glide.Glide
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import net.developia.livartc.R
import net.developia.livartc.login.TokenManager
import net.developia.livartc.mypage.PopUp
import net.developia.livartc.mypage.dto.MemberGradeDto
import net.developia.livartc.mypage.dto.PopUpDto
import net.developia.livartc.retrofit.MyApplication
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

/**
 * 작성자 : 황수영
 * 내용 : 멤버십 탭
 */
class MemberShipFragment : Fragment() {
    private lateinit var linearLayout: LinearLayout
    private lateinit var membershipCardImageView: ImageView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_membership, container, false)
        linearLayout = view.findViewById(R.id.profile_membership)
        membershipCardImageView = view.findViewById(R.id.membership_card)

        GlobalScope.launch(Dispatchers.Main) {
            val result = fetchDataFromServer()
            updateUI(result)
        }
        return view
    }

    private suspend fun fetchDataFromServer(): MemberGradeDto {
        return try {
            val jwtToken = TokenManager.getToken(MyApplication.instance)!!
            val networkService = (context?.applicationContext as MyApplication).networkService

            suspendCoroutine<MemberGradeDto> { continuation ->
                val getMemberGrade = networkService.getMemberGrade(jwtToken)
                getMemberGrade.enqueue(object : Callback<MemberGradeDto> {
                    override fun onResponse(call: Call<MemberGradeDto>, response: Response<MemberGradeDto>) {
                        Log.d("멤버십 조회 API 성공 response", response.toString())
                        val resDto = response.body()
                        Log.d("멤버십 조회 API 성공 response", resDto.toString())
                        resDto?.let {
                            continuation.resume(it)
                        }
                    }

                    override fun onFailure(call: Call<MemberGradeDto>, t: Throwable) {
                        Log.d("멤버십 조회 API 실패", t.toString())
                        continuation.resume(MemberGradeDto(0, "", "", 0))
                    }
                })
            }
        } catch (e: Exception) {
            Log.d("멤버십 조회 API 예외", e.toString())
            MemberGradeDto(0, "", "", 0)
        }
    }
    private fun updateUI(result: MemberGradeDto) {
        Glide.with(requireContext())
            .load(result.image)
            .into(membershipCardImageView)
    }
}