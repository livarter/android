package net.developia.livartc.purchase

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.DialogFragment
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
                    Toast.makeText(context, "적용할 수 있는 포인트를 초과하였습니다.", Toast.LENGTH_SHORT).show()
                    return@setOnClickListener
                }else{
                    if (applyPoint in 0..point) {
                        // 포인트 적용
                        val bundle = Bundle()
                        bundle.putLong("point", applyPoint)
                        parentFragmentManager.setFragmentResult("point", bundle)
                        dismiss()  // DialogFragment 닫기
                    } else {
                        Toast.makeText(context, "포인트가 부족합니다.", Toast.LENGTH_SHORT).show()
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
}