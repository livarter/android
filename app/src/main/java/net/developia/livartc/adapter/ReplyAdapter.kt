package net.developia.livartc.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import net.developia.livartc.databinding.ItemReplyBinding
import net.developia.livartc.model.Reply
import net.developia.livartc.retrofit.MyApplication

/**
 * LIVARTC
 * Created by 최현서
 * Date: 1/25/24
 * Time: 11:48
 */
class ReplyAdapter(private val replyList: List<Reply>) :
    RecyclerView.Adapter<ReplyAdapter.ReplyViewHolder>() {

    class ReplyViewHolder(val binding: ItemReplyBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ReplyViewHolder {
        val binding = ItemReplyBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ReplyViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ReplyViewHolder, position: Int) {
        val replyItem = replyList[position]
        Log.d("ReplyAdapter:", "$position : ${replyList[position]}")
        holder.binding.profileNickname.text = replyItem.nickname
        holder.binding.replyContent.text = replyItem.replyComment.replace("\\n", "\n")
        holder.binding.replyDate.text = "${replyItem.createdAt.year}.${String.format("%02d", replyItem.createdAt.monthValue)}.${String.format("%02d", replyItem.createdAt.dayOfMonth)}"

        replyItem.replyImg.let {
            val imgRef = MyApplication.storage.reference.child("review/${it}")
            imgRef.downloadUrl.addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Glide.with(holder.binding.replyImage.context)
                        .load(task.result)
                        .into(holder.binding.replyImage)
                    holder.binding.replyImage.visibility = View.VISIBLE
                } else {
                    Log.d("ReplyAdapter:", "Firebase 이미지 다운로드 실패")
                    holder.binding.replyImage.visibility = View.GONE
                }
            }
        }
    }

    override fun getItemCount() = replyList.size
}