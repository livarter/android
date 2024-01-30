package net.developia.livartc.model

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