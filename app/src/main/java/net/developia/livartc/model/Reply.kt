package net.developia.livartc.model

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