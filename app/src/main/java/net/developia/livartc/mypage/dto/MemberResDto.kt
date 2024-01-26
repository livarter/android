package net.developia.livartc.mypage.dto

data class MemberResDto(
    val address: Any,
    val birthDate: Any,
    val createdAt: CreatedAt,
    val curPoint: Int,
    val email: String,
    val grade: String,
    val image: Any,
    val name: Any,
    val nickname: String,
    val role: String,
    val totalPoint: Int,
    val updatedAt: UpdatedAt,
    val zipCode: Any
)