package net.developia.livartc.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import net.developia.livartc.databinding.ItemReplyBinding
import net.developia.livartc.model.Reply

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
        holder.binding.profileNickname.text = replyItem.nickname
        holder.binding.replyContent.text = replyItem.replyComment.replace("\\n", "\n")
        holder.binding.replyDate.text = "${replyItem.createdAt.year}.${String.format("%02d",replyItem.createdAt.monthValue)}.${String.format("%02d",replyItem.createdAt.dayOfMonth)}"
    }

    override fun getItemCount() = replyList.size
}