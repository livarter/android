package net.developia.livartc.mypage.myroom

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import net.developia.livartc.R
import net.developia.livartc.databinding.FragmentMyroomBinding
import net.developia.livartc.login.TokenManager
import net.developia.livartc.mypage.dto.Catalog
import net.developia.livartc.mypage.dto.CatalogListResDto
import net.developia.livartc.retrofit.MyApplication
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MyRoomFragment : Fragment() {
    private lateinit var binding: FragmentMyroomBinding
    private lateinit var roomDataList: List<Catalog>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentMyroomBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        // Call getCatalogs to fetch the roomDataList asynchronously
        getCatalogs()
    }

    /**
     * 작성자 : 황수영
     * 내용 : 나의 방 정보 조회
     */
    private fun getCatalogs() {
        val networkService = (context?.applicationContext as MyApplication).networkService
        val jwtToken = TokenManager.getToken(MyApplication.instance)!!
        val getCatalogs = networkService.getCatalogs(jwtToken)
        Log.d("나의 방 정보 조회 API 시작", getCatalogs.toString())

        getCatalogs.enqueue(object : Callback<CatalogListResDto> {
            override fun onResponse(call: Call<CatalogListResDto>, response: Response<CatalogListResDto>) {
                Log.d("나의 방 정보 조회 API 성공 response", response.toString())

                if (response.isSuccessful) {
                    val resDto = response.body()
                    Log.d("나의 방 정보 조회 API 성공", resDto.toString())
                    if (resDto != null) {
                        Log.d("나의 방 정보 조회 API 성공", resDto.toString())

                        // Update the roomDataList
                        if (resDto.catalogs != null) {
                            roomDataList = resDto.catalogs
                        } else {
                            // roomDataList =
                        }
                        setupUI()
                    }
                } else {
                    Log.d("나의 방 정보 조회 API 실패", "서버 응답 실패")
                }
            }

            override fun onFailure(call: Call<CatalogListResDto>, t: Throwable) {
                Log.d("나의 방 정보 조회 API 실패", "네트워크 오류" + t.toString())
            }
        })
    }

    private fun setupUI() {
        Log.d("FragmentAccountBinding hashtag", roomDataList[0]?.hashtag.toString())

        Glide.with(this)
            .load(roomDataList[0].leftChair)
            .into(binding.leftChair)

        Glide.with(this)
            .load(roomDataList[0].rightChair)
            .into(binding.rightChair)

        val hashtagContainer: LinearLayout = view?.findViewById(R.id.hashtag_container) ?: return

        for (roomData in roomDataList) {
            val hashtagTextView = TextView(requireContext())
            hashtagTextView.text = "#" + roomData.hashtag + "  "
            hashtagTextView.setTextColor(ContextCompat.getColor(requireContext(), R.color.black))
            hashtagTextView.textSize = 16f // resources.getDimension(R.dimen.font_small)
            hashtagTextView.layoutParams = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            ).apply {
                setMargins(0, 20, 0, 20)
            }
            hashtagContainer.addView(hashtagTextView)
            hashtagTextView.setOnClickListener {
                // 해시태그 클릭 시 해당 방의 이미지 업데이트
                // 배경


                Glide.with(this@MyRoomFragment)
                    .load(roomData.background)
                    .into(binding.myroomBackground)

                Glide.with(this@MyRoomFragment)
                    .load(roomData.leftChair)
                    .into(binding.leftChair)

                Glide.with(this@MyRoomFragment)
                    .load(roomData.rightChair)
                    .into(binding.rightChair)

                // 소파
                Glide.with(this@MyRoomFragment)
                    .load(roomData.sofa)
                    .into(binding.sofa)

                Glide.with(this@MyRoomFragment)
                    .load(roomData.deco)
                    .into(binding.deco)

            }
        }
    }


}