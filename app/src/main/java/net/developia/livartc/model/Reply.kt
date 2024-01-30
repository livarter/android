package net.developia.livartc.model

/**
 * LIVARTC
 * Created by 최현서
 * Date: 2024-01-23
 * Time: 10:08
 * 작업 내용: 상품 리뷰 model 구현
 */

data class Reply(
    val createdAt: CreatedAt,
    val isDeleted: Int,
    val memberId: Int,
    val nickname: String,
    val productId: Int,
    val profileImg: String,
    val replyComment: String,
    val replyId: Int,
    val updatedAt: UpdatedAt,
    val replyImg: String
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