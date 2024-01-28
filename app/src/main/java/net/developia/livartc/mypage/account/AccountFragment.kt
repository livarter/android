package net.developia.livartc.mypage.account

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import net.developia.livartc.R
import net.developia.livartc.mypage.dto.RoomResDtoItem

//class AccountFragment : Fragment() {
//
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//    }
//
//    override fun onCreateView(
//        inflater: LayoutInflater, container: ViewGroup?,
//        savedInstanceState: Bundle?
//    ): View? {
//        return inflater.inflate(R.layout.fragment_account, container, false)
//    }
//
//
//}

class AccountFragment : Fragment() {

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

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_account, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val fragmentContainer = view.findViewById<LinearLayout>(R.id.fragmentContainer)

        for (roomData in roomDataList) {
            val roomView = layoutInflater.inflate(R.layout.item_room, fragmentContainer, false)
            populateRoomView(roomView, roomData)
            fragmentContainer.addView(roomView)
        }
    }

    private fun populateRoomView(roomView: View, roomData: RoomResDtoItem) {
        val leftChairImageView = roomView.findViewById<ImageView>(R.id.leftChair)
        val rightChairImageView = roomView.findViewById<ImageView>(R.id.rightChair)
        val sofaImageView = roomView.findViewById<ImageView>(R.id.myroom_background)
        val hashtagTextView = roomView.findViewById<TextView>(R.id.myroom_hashtag)

        Glide.with(this).load(roomData.leftChair).into(leftChairImageView)
        Glide.with(this).load(roomData.rightChair).into(rightChairImageView)
        Glide.with(this).load(roomData.sofa).into(sofaImageView)
        hashtagTextView.text = roomData.hashtag
    }
}
