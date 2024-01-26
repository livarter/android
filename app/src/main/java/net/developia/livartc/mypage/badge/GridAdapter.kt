package net.developia.livartc.mypage.badge

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import android.widget.TextView
import com.squareup.picasso.Picasso
import net.developia.livartc.R


/**
 * 작성자 : 황수영
 * 내용 : 나의 뱃지 화면용 그리드 어댑터
 */

class GridAdapter(private val context: Context, private val dataList: List<GridItem>) : BaseAdapter() {

    override fun getCount(): Int {
        return dataList.size
    }

    override fun getItem(position: Int): Any {
        return dataList[position]
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val view: View
        val holder: ViewHolder

        if (convertView == null) {
            view = LayoutInflater.from(context).inflate(R.layout.grid_item, parent, false)
            holder = ViewHolder(view)
            view.tag = holder
        } else {
            view = convertView
            holder = view.tag as ViewHolder
        }

        val item = getItem(position) as GridItem
        holder.textView.text = item.text

        val isEarned = item.isEarned

        if (isEarned != null && !isEarned) {
            // 뱃지 부여 받지 않은 경우 처리 - 이후 디자인 예정
            // Picasso.get().load(item.image).into(holder.imageView)
        } else {
            Picasso.get().load(item.image).into(holder.imageView)
        }

        return view
    }

    private class ViewHolder(view: View) {
        val imageView: ImageView = view.findViewById(R.id.grid_image)
        val textView: TextView = view.findViewById(R.id.grid_text)
    }
}