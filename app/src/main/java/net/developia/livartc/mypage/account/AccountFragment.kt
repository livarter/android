package net.developia.livartc.mypage.account

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.fragment.app.Fragment
import net.developia.livartc.R
import net.developia.livartc.databinding.FragmentAccountBinding
import net.developia.livartc.databinding.FragmentMembershipBinding
import net.developia.livartc.main.MyPurchaseFragment
import net.developia.livartc.mypage.PopUp
import net.developia.livartc.mypage.dto.PopUpDto
import net.developia.livartc.mypage.membership.MyInfoActivity
import net.developia.livartc.mypage.membership.MyOrderActivity
import net.developia.livartc.mypage.membership.MyReviewActivity

class AccountFragment : Fragment() {

    lateinit var accountBinding: FragmentAccountBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val view = inflater.inflate(R.layout.fragment_account, container, false)
        accountBinding = FragmentAccountBinding.inflate(inflater, container, false)

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

        return inflater.inflate(R.layout.fragment_account, container, false)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        accountBinding.myorderTab.setOnClickListener {
            parentFragmentManager.beginTransaction().apply{
                replace(R.id.main_container, MyPurchaseFragment())
                addToBackStack(null)
                commit()
            }
        }
    }
}