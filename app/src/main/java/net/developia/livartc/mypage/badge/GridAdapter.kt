package net.developia.livartc.mypage.badge

import android.content.Context
import android.util.Log
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
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

        val textSize = holder.textView.context.resources.getDimension(R.dimen.font_xsmall)
        holder.textView.setTextSize(TypedValue.COMPLEX_UNIT_PX, textSize)
        holder.textView.setTextColor(holder.textView.context.resources.getColor(R.color.dark_grey))
        Log.d("GridAdapter textSize 1", textSize.toString())
        holder.textView.text = item.text

        val isEarned = item.earned
        if (isEarned != null && !isEarned) {
            val defaultDrawable = ContextCompat.getDrawable(context, R.drawable.badge1)
            holder.imageView.setImageDrawable(defaultDrawable)
        } else {
            val drawableResName = "badge${item.id}"
            val drawableResId = context.resources.getIdentifier(drawableResName, "drawable", context.packageName)
            val drawable = ContextCompat.getDrawable(context, drawableResId)
            holder.imageView.setImageDrawable(drawable)
        }
        return view
    }

    private class ViewHolder(view: View) {
        val imageView: ImageView = view.findViewById(R.id.grid_image)
        val textView: TextView = view.findViewById(R.id.grid_text)
    }
}