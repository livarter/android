package net.developia.livartc.mypage.coupon

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import com.squareup.picasso.Picasso
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import net.developia.livartc.R
import net.developia.livartc.login.TokenManager
import net.developia.livartc.mypage.dto.CouponListResDto
import net.developia.livartc.mypage.dto.CouponResDto
import net.developia.livartc.retrofit.MyApplication
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import kotlin.coroutines.suspendCoroutine

/**
 * 작성자 : 황수영
 * 내용 : 나의 쿠폰 페이지
 */
class CouponFragment : Fragment() {
    private lateinit var linearLayout: LinearLayout

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_coupon, container, false)
        linearLayout = view.findViewById(R.id.coupon_container)

        GlobalScope.launch(Dispatchers.Main) {
            val result = fetchDataFromServer()
            updateUI(result)
        }

        return view
    }

    /**
     * 작성자 : 황수영
     * 내용 : 나의 쿠폰 정보 조회 api 연동
     */
    private suspend fun fetchDataFromServer(): List<CouponListResDto> {
        return try {
            val jwtToken = TokenManager.getToken(MyApplication.instance)!!
            val networkService = (context?.applicationContext as MyApplication).networkService

            suspendCoroutine<List<CouponListResDto>> { continuation ->
                val couponResDto = networkService.getCouponsByMember(jwtToken)
                couponResDto.enqueue(object : Callback<CouponResDto> {
                    override fun onResponse(call: Call<CouponResDto>, response: Response<CouponResDto>) {
                        Log.d("쿠폰 조회 API 성공 response", response.toString())
                        val resDto = response.body()
                        val dataList = resDto?.couponResDtos?.map {
                            CouponListResDto(it.name, it.image)
                        } ?: emptyList()
                        continuation.resumeWith(Result.success(dataList))
                    }

                    override fun onFailure(call: Call<CouponResDto>, t: Throwable) {
                        Log.d("쿠폰 조회 API 실패", t.toString())
                        continuation.resumeWith(Result.failure(t))
                    }
                })
            }
        } catch (e: Exception) {
            Log.d("쿠폰 조회 API 예외", e.toString())
            emptyList()
        }
    }

    private fun updateUI(result: List<CouponListResDto>) {
        for (data in result) {
            addViewsToLayout(data)
        }
    }

    /**
     * 작성자 : 황수영
     * 내용 : UI 업데이트
     */
    private fun addViewsToLayout(data: CouponListResDto) {
        val textView = TextView(requireContext())
        textView.layoutParams = ViewGroup.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
        textView.text = data.text
        linearLayout.addView(textView)

        val imageView = ImageView(requireContext())
        imageView.layoutParams = ViewGroup.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
        Picasso.get().load(data.imageUrl).into(imageView)
        imageView.scaleType = ImageView.ScaleType.FIT_CENTER
        linearLayout.addView(imageView)
    }
}
