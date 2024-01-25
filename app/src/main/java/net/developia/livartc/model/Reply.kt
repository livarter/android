package net.developia.livartc.model

import com.hyundai.loginapptest.domain.CreatedAt
import com.hyundai.loginapptest.domain.UpdatedAt

data class Reply(
    val createdAt: CreatedAt,
    val isDeleted: Int,
    val memberId: Int,
    val nickname: String,
    val productId: Int,
    val replyComment: String,
    val replyId: Int,
    val updatedAt: UpdatedAt
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