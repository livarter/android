package net.developia.livartc.mypage.account

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
import net.developia.livartc.databinding.FragmentAccountBinding
import net.developia.livartc.mypage.dto.RoomResDtoItem

class AccountFragment : Fragment() {
    private lateinit var binding: FragmentAccountBinding
    private val roomDataList: List<RoomResDtoItem> = listOf(
        RoomResDtoItem(
            1,
            "https://github.com/livarter/backend/assets/77563814/8b45040d-f479-480e-be3b-f7d6b407c8dd",
            "https://github.com/livarter/backend/assets/77563814/10223998-89f3-48dc-a58f-230d4d871bf0",
            "https://github.com/livarter/backend/assets/77563814/ed33b2de-4862-4e9c-924b-eeadb96cb35d",
            "귀여운"
        ),
        RoomResDtoItem(
            2,
            "https://github.com/livarter/backend/assets/77563814/af02b67d-b446-4e7d-a093-f231087439f8",
            "https://github.com/livarter/backend/assets/77563814/adf70d26-a950-4f62-a3b4-9372ee7b11a6",
            "https://github.com/livarter/backend/assets/77563814/70fc387b-f0fd-499c-8761-f9fb8b7676d5",
            "시크한"
        )
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentAccountBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        Log.d("FragmentAccountBinding hashtag", roomDataList[0]?.hashtag.toString())

        Glide.with(this)
            .load(roomDataList[0].leftChair)
            .into(binding.leftChair)

        Glide.with(this)
            .load(roomDataList[0].rightChair)
            .into(binding.rightChair)

        super.onViewCreated(view, savedInstanceState)

        val hashtagContainer: LinearLayout = view.findViewById(R.id.hashtag_container)

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
                Glide.with(this@AccountFragment)
                    .load(roomData.leftChair)
                    .into(binding.leftChair)

                Glide.with(this@AccountFragment)
                    .load(roomData.rightChair)
                    .into(binding.rightChair)
            }
        }
    }
}