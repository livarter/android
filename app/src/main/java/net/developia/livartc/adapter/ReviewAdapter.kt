package net.developia.livartc.adapter

import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition
import net.developia.livartc.databinding.ItemBestBinding
import net.developia.livartc.databinding.ItemReviewBinding
import net.developia.livartc.main.HomeFragment
import net.developia.livartc.model.BestProduct
import net.developia.livartc.product.DetailFragment
import net.developia.livartc.product.Reply
import java.text.SimpleDateFormat
import java.util.Date

/**
 * LIVARTC
 * Created by 최현서
 * Date: 2024-01-22
 * Time: 오후 12:54
 */
class ReviewAdapter(private val replyList : Reply?, val context: DetailFragment)
    : RecyclerView.Adapter<ReviewAdapter.ReplyViewHolder>() {
    inner class ReplyViewHolder(binding: ItemReviewBinding) : RecyclerView.ViewHolder(binding.root) {
        val profileId = binding.profileId
        val replyContent = binding.reviewContent
        val replyDate = binding.replyDate
        val root = binding.root
        val format = SimpleDateFormat("yyyy.MM.dd")
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ReplyViewHolder {
        val binding: ItemReviewBinding = ItemReviewBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ReplyViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ReplyViewHolder, position: Int) {
        val replyData = replyList!![position]

        holder.profileId.text = "livarter${position+1}"
        holder.replyContent.text = replyData.replyComment
        holder.replyDate.text = holder.format.format(replyData.createdAt)
    }

    override fun getItemCount(): Int {
        // 리사이클러뷰 아이템 개수는 할일 리스트 크기
        Log.d("hschoi", (replyList?.size?:0).toString())
        return replyList?.size?:0
    }


}