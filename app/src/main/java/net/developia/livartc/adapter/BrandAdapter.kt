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
 * Time: 4:33 PM
 * Function : 브랜드 목록을 표시하기 위한 RecyclerView 어댑터 구현
 */

// 브랜드 이름을 표시하기 위한 RecyclerView 어댑터 구현
class BrandAdapter(
    private val brands: List<String>,           // 표시할 브랜드 이름 목록
    private val onClick: (String?) -> Unit      // 아이템 클릭 시 호출될 콜백 함수
) : RecyclerView.Adapter<BrandAdapter.BrandViewHolder>() {

    var selectedPosition = RecyclerView.NO_POSITION // 현재 선택된 아이템의 위치

    //  브랜드 이름을 표시할 ViewHolder 클래스
    class BrandViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val brandText: TextView = view.findViewById(R.id.brandText)
    }

    // ViewHolder 생성 시 호출 됨. 레이아웃을 인플레이트하여 ViewHolder를 생성한다.
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BrandViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.brand_name, parent, false)
        return BrandViewHolder(view)
    }

    // ViewHolder의 데이터를 바인딩할 때 호출, 각 아이템의 내용을 설정한다.
    @SuppressLint("NotifyDataSetChanged")
    override fun onBindViewHolder(holder: BrandViewHolder, @SuppressLint("RecyclerView") position: Int) {
        val brand = brands[position]
        holder.brandText.text = brand

        // 선택된 아이템과 현재 아이템의 위치가 같다면 효과 적용
        if (position == selectedPosition) {
            holder.brandText.setTextColor(Color.parseColor("#000000")) // 선택된 해시태그 색상
            holder.brandText.setTypeface(null, Typeface.BOLD) // Bold 스타일 적용
        } else {
            holder.brandText.setTextColor(Color.parseColor("#495057")) // 기본 해시태그 색상
            holder.brandText.setTypeface(null, Typeface.NORMAL) // 기본 스타일 적용
        }

        // 이미 클릭 이벤트 처리
        holder.itemView.setOnClickListener {
            if (selectedPosition == position) {
                selectedPosition = RecyclerView.NO_POSITION
                onClick("") // 선택 해제
            } else {
                selectedPosition = position // 선택된 위치 업데이트
                onClick(brand) // 콜백 함수 호출
            }
            notifyDataSetChanged() // 데이터 변경 인지
        }
    }

    //전체 아이템 개수 반환
    override fun getItemCount(): Int = brands.size
}

