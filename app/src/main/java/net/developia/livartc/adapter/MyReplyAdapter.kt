package net.developia.livartc.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import net.developia.livartc.R
import net.developia.livartc.databinding.ItemMyreviewBinding
import net.developia.livartc.model.MyReply
import net.developia.livartc.retrofit.MyApplication

/**
 * LIVARTC
 * Created by 최현서
 * Date: 1/25/24
 * Time: 11:48
 * 작업 내용: 나의 리뷰 조회 구현
 */
class MyReplyAdapter(private val myReplyList: List<MyReply>) :
    RecyclerView.Adapter<MyReplyAdapter.MyReplyViewHolder>() {

    class MyReplyViewHolder(val binding: ItemMyreviewBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyReplyViewHolder {
        val binding = ItemMyreviewBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyReplyViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MyReplyViewHolder, position: Int) {
        val myReplyItem = myReplyList[position]
        Log.d("MyReplyAdapter:", "$position : ${myReplyList[position]}")
        holder.binding.profileNickname.text = myReplyItem.nickname
        holder.binding.replyContent.text = myReplyItem.replyComment.replace("\\n", "\n")
        holder.binding.replyDate.text = "${myReplyItem.createdAt.year}.${String.format("%02d", myReplyItem.createdAt.monthValue)}.${String.format("%02d", myReplyItem.createdAt.dayOfMonth)}"
        holder.binding.productName.text = myReplyItem.productName
        holder.binding.productBrand.text = myReplyItem.productBrand
        Glide.with(holder.binding.productImg.context)
            .load(myReplyItem.productImage)
            .into(holder.binding.productImg)
        if (myReplyItem.profileImg != null) {
            Glide.with(holder.binding.profileImage.context)
                .load(myReplyItem.profileImg)
                .into(holder.binding.profileImage)
        } else {
            holder.binding.profileImage.setImageResource(R.drawable.badge1)
        }
        if (myReplyItem.replyImg != null) {
            val imgRef = MyApplication.storage.reference.child("review/${myReplyItem.replyImg}")
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

    override fun getItemCount() = myReplyList.size
}