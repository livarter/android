package net.developia.livartc.product
import java.util.Date

class Reply : ArrayList<Reply.ReplyItem>(){
    data class ReplyItem(
        val createdAt: Long,
        val id: Int,
        val isDeleted: Int,
        val memberId: Int,
        val productId: Int,
        val replyComment: String,
        val updatedAt: Long
    )
}