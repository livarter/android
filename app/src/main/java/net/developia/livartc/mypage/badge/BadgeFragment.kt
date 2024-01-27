package net.developia.livartc.mypage.badge

import android.app.Dialog
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.GridView
import net.developia.livartc.R

import android.util.Log
import android.view.Gravity
import android.view.WindowManager
import android.widget.ImageView
import android.widget.TextView
import com.squareup.picasso.Picasso
import net.developia.livartc.login.TokenManager
import net.developia.livartc.mypage.dto.BadgeResDto
import net.developia.livartc.retrofit.MyApplication
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class BadgeFragment : Fragment() {

    private lateinit var gridView: GridView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_badge, container, false)

        gridView = view.findViewById(R.id.gridView)

        val dataList = ArrayList<GridItem>()

        var jwtToken = TokenManager.getToken(MyApplication.instance)!!
        val networkService = (context?.applicationContext as MyApplication).networkService
        var badgeResDto = networkService.getBadgesByMember(jwtToken)
        Log.d("뱃지 조회 API 시작", badgeResDto.toString())

        badgeResDto.enqueue(object : Callback<BadgeResDto> {
            override fun onResponse(call: Call<BadgeResDto>, response: Response<BadgeResDto>) {
                Log.d("뱃지 조회 API 성공 response", response.toString())

                var resDto = response.body()!!

                if (resDto != null) {
                    Log.d("뱃지 조회 API 성공 resDto", resDto.badges.toString())
                    for (badge in resDto.badges) {
                        Log.d("뱃지 조회 API 성공", badge.toString())
                        dataList.add(GridItem(badge.image, badge.name, badge.earned, badge.description))
                    }

                    // API 요청 후 GridAdapter 초기화
                    activity?.runOnUiThread {
                        val adapter = GridAdapter(requireContext(), dataList)
                        gridView.adapter = adapter
                    }
                }
            }
            override fun onFailure(call: Call<BadgeResDto>, t: Throwable) {
                Log.d("뱃지 조회 API 실패", t.toString())
            }
        })

        val adapter = GridAdapter(requireContext(), dataList)
        gridView.adapter = adapter

        popup(dataList)

        return view
    }
    fun popup(dataList : ArrayList<GridItem>) {
        // 뱃지 정보 팝업 띄우기
        gridView.setOnItemClickListener { _, _, position, _ ->
            val selectedItem = dataList[position]

            val dialog = Dialog(requireContext())
            dialog.setContentView(R.layout.custom_popup_layout)

            val popupImageView: ImageView = dialog.findViewById(R.id.popupImageView)
            val popupTextView: TextView = dialog.findViewById(R.id.popupTextView)
            val popupTextDescriptionView: TextView = dialog.findViewById(R.id.popupDescriptionView)

            Picasso.get().load(selectedItem.image).into(popupImageView)
            popupTextView.text = selectedItem.text
            popupTextDescriptionView.text = selectedItem.description

            dialog.show()

            val window = dialog.window
            val params = window?.attributes

            val desiredSizeInDp = 360

            val density = resources.displayMetrics.density
            val desiredWidthSizeInPixels = (desiredSizeInDp * density).toInt()
            val desiredHeightSizeInPixels = ((desiredSizeInDp + 40) * density).toInt()

            params?.width = desiredWidthSizeInPixels
            params?.height = desiredHeightSizeInPixels

            params?.gravity = Gravity.CENTER

            window?.attributes = params as WindowManager.LayoutParams
        }
    }
}