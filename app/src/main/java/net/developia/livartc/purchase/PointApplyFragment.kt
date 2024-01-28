package net.developia.livartc.purchase

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import net.developia.livartc.R
import net.developia.livartc.databinding.FragmentPointApplyBinding
import net.developia.livartc.login.TokenManager
import net.developia.livartc.mypage.dto.MemberGradeDto
import net.developia.livartc.retrofit.MyApplication
import net.developia.livartc.retrofit.RetrofitInstance
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

/**
 * LIVARTC
 * Created by 변형준
 * Date: 1/26/24
 * 작업내용: 포인트 적용하기
 */
class PointApplyFragment : DialogFragment(){
    lateinit var binding: FragmentPointApplyBinding

    private var point = 0L

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentPointApplyBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        getPointByMember()
        binding.findPoint.text = point.toString()
        binding.applyPoint.setText("0")  // 기본값 설정
        val totalPrice = arguments?.getLong("totalPrice")
        binding.applyPointText.setOnClickListener {
            val applyPoint = binding.applyPoint.text.toString().toLongOrNull() ?: 0

            if (totalPrice != null) {
                if(totalPrice < applyPoint) {
                    customToast("적용할 수 있는 포인트를 초과하였습니다.")
                    return@setOnClickListener
                }else{
                    if (applyPoint in 0..point) {
                        // 포인트 적용
                        val bundle = Bundle()
                        bundle.putLong("point", applyPoint)
                        parentFragmentManager.setFragmentResult("point", bundle)
                        dismiss()  // DialogFragment 닫기
                    } else {
                        customToast("포인트가 부족합니다.")
                    }
                }
            }
        }
    }

    private fun getPointByMember() {
        val jwtToken = TokenManager.getToken(MyApplication.instance)!!
        RetrofitInstance.api.getMemberGrade(jwtToken).enqueue(object : Callback<MemberGradeDto> {
            override fun onResponse(
                call: Call<MemberGradeDto>,
                response: Response<MemberGradeDto>
            ) {
                if(response.isSuccessful) {
                    val memberGradeDto = response.body()
                    Log.d("TAG", "onResponse: ${memberGradeDto?.curPoint}")
                    if (memberGradeDto != null) {
                        point = memberGradeDto.curPoint?.toLong()!!
                    } else point = 0

                    binding.findPoint.text = "$point 점"
                }
            }

            override fun onFailure(call: Call<MemberGradeDto>, t: Throwable) {
                t.printStackTrace()
            }
        })
    }

    @SuppressLint("MissingInflatedId")
    fun customToast(message: String) {
        val inflater = LayoutInflater.from(context)
        val layout = inflater.inflate(R.layout.toast_board, view?.findViewById(R.id.toast_layout_root) as ViewGroup?)
        val textView = layout.findViewById<TextView>(R.id.text_board)
        textView.text = message

        val toastView = Toast.makeText(context, message, Toast.LENGTH_SHORT)
        toastView.setGravity(Gravity.TOP or Gravity.CENTER_HORIZONTAL, 0, 150)
        toastView.view = layout
        toastView.show()
    }

}