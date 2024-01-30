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
import android.widget.TextView
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import net.developia.livartc.R
import net.developia.livartc.databinding.FragmentMembershipBinding
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
    private lateinit var membershipCardPointView: TextView
    lateinit var binding: FragmentMembershipBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_membership, container, false)
        linearLayout = view.findViewById(R.id.profile_membership)
        membershipCardImageView = view.findViewById(R.id.membership_card)
        membershipCardPointView = view.findViewById(R.id.membership_card_point)

        GlobalScope.launch(Dispatchers.Main) {
            val result = fetchDataFromServer()
            //updateUI(result)
            if (result != null) {
                Log.d("MemberGradeDto point", result.curPoint.toString())
                var point = result.curPoint.toString() + "p"
                membershipCardPointView.setText(point)
            } else {
                membershipCardPointView.setText("1000p")
            }
            Log.d("MemberGradeDto image", result.image.toString())
//            Glide.with(requireContext())
//                .load(result.image)
//                .into(membershipCardImageView)
        }

        binding = FragmentMembershipBinding.inflate(inflater, container, false)

        // 1. my_info 버튼 클릭시 새로운 액티비티로 이동
        val myInfoLayout = view.findViewById<LinearLayout>(R.id.myinfo_tab)
        myInfoLayout.setOnClickListener {
            startActivity(Intent(requireContext(), MyInfoActivity::class.java))
        }

        // 2. my_review 버튼 클릭시 새로운 액티비티로 이동
        val myReviewLayout = view.findViewById<LinearLayout>(R.id.myreview_tab)
        myReviewLayout.setOnClickListener {
            startActivity(Intent(requireContext(), MyReviewActivity::class.java))
        }

        // 3. my_review 버튼 클릭시 새로운 액티비티로 이동
        val myOrderLayout = view.findViewById<LinearLayout>(R.id.myorder_tab)
        myOrderLayout.setOnClickListener {
            startActivity(Intent(requireContext(), MyOrderActivity::class.java))
        }

        // 4. logout 버튼 클릭시 => 로그아웃하고 처음 화면으로 이동하기
        // 일단 팝업 뜨는 버튼으로!!
        val logoutLayout = view.findViewById<LinearLayout>(R.id.logout_tab)
        logoutLayout.setOnClickListener {
            // startActivity(Intent(requireContext(), LogoutActivity::class.java))
            // 팝업 뜨는 예시
            val tmp = PopUpDto (
                "Commenter",
                "https://github.com/livarter/android/assets/77563814/a346484d-31df-477e-a43c-690aec02c63f",
                "축하합니다! 뱃지를 발급하였습니다!"
            )
            PopUp().show(tmp, requireContext())
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

    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // 클릭시, 나의 주문 내역 관련 연결
//        binding.myorderTab.setOnClickListener {
//            parentFragmentManager.beginTransaction().apply{
//                replace(R.id.main_container, MyPurchaseFragment())
//                addToBackStack(null)
//                commit()
//            }
//        }
    }
}