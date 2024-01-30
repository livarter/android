package net.developia.livartc.adapter

import android.annotation.SuppressLint
import android.graphics.Color
import android.graphics.Typeface
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import net.developia.livartc.R

/**
 * LIVARTC
 * Created by 오수영
 * Date: 1/24/24
 * Time: 5:22 PM
 * Function : 사용자가 선택할 수 있는 해시태그 목록을 표시하기 위한 RecyclerView 어댑터 구현
 */
class HashTagAdapter(private val hashTags: List<String>,        // 표시 할 해시태그 목록
                     private val onClick: (String) -> Unit) :   // 아이템 클릭 시 호출된 콜백 함수
    RecyclerView.Adapter<HashTagAdapter.HashTagViewHolder>() {  // RecyclerView.Adapter 상속

    private var selectedPosition = RecyclerView.NO_POSITION // 현재 선택된 아이템의 위치


    // 해시태그를 표시할 ViewHolder 클래스
    class HashTagViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val hashTagText: TextView = view.findViewById(R.id.hashTagText) // 해시태그를 표시할 Text
    }

    // ViewHolder 생성 시 호출됨. 레이아웃을 Inflate하여 ViewHolder를 생성한다.
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HashTagViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.hashtag_name, parent, false)
        return HashTagViewHolder(view)
    }

    // ViewHolder와 데이터를 바인딩할 때 호출됨. 각 아이템의 내용을 설정한다.
    @SuppressLint("NotifyDataSetChanged")
    override fun onBindViewHolder(holder: HashTagViewHolder, @SuppressLint("RecyclerView") position: Int) {
        val hashTag = hashTags[position]    //현재 위치의 해시태그
        holder.hashTagText.text = hashTag   //TextView에 해시태그 설정

        // 선택된 해시태그 스타일 변경
        if (position == selectedPosition) {
            holder.hashTagText.setTextColor(Color.parseColor("#000000")) // 선택된 해시태그 색상
            holder.hashTagText.setTypeface(null, Typeface.BOLD) // Bold 스타일 적용
        } else {
            holder.hashTagText.setTextColor(Color.parseColor("#495057")) // 기본 해시태그 색상
            holder.hashTagText.setTypeface(null, Typeface.NORMAL) // 기본 스타일 적용
        }

        // 아이템 클릭 이벤트 처리
        holder.itemView.setOnClickListener {
            if(selectedPosition == position) {
                selectedPosition = RecyclerView.NO_POSITION
                onClick("") // 선택 해제 시 콜벡 호출
            } else {
                selectedPosition = position // 선택된 위치 업데이트
                onClick(hashTag)    // 콜백함수 호출
            }
            notifyDataSetChanged()  // 데이터 변경 알림
        }
    }


    // 전체 아이템 개수 반환
    override fun getItemCount() = hashTags.size
}