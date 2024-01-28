package net.developia.livartc.model

/**
 * LIVARTC
 * Created by 최현서
 * Date: 2024-01-28
 * Time: 오후 10:08
 */
data class MyReply(
    val createdAt: CreatedAt,
    val isDeleted: Int,
    val memberId: Int,
    val nickname: String,
    val profileImg: String,
    val productId: Int,
    val replyComment: String,
    val replyId: Int,
    val updatedAt: UpdatedAt,
    val replyImg: String,

    val productImage: String,
    val productBrand: String,
    val productName: String
) {
    data class CreatedAt(
        val chronology: Chronology,
        val dayOfMonth: Int,
        val dayOfWeek: String,
        val dayOfYear: Int,
        val hour: Int,
        val minute: Int,
        val month: String,
        val monthValue: Int,
        val nano: Int,
        val second: Int,
        val year: Int
    ) {
        data class Chronology(
            val calendarType: String,
            val id: String
        )
    }

    data class UpdatedAt(
        val chronology: Chronology,
        val dayOfMonth: Int,
        val dayOfWeek: String,
        val dayOfYear: Int,
        val hour: Int,
        val minute: Int,
        val month: String,
        val monthValue: Int,
        val nano: Int,
        val second: Int,
        val year: Int
    ) {
        data class Chronology(
            val calendarType: String,
            val id: String
        )
    }
}