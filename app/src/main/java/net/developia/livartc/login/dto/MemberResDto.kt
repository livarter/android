package com.hyundai.loginapptest.domain

data class MemberResDto(
    val address: String = "",
    val birthDate: Chronology = Chronology(),
    val createdAt: CreatedAt = CreatedAt(),
    val curPoint: Int = 0,
    val email: String = "",
    val grade: String = "",
    val image: String = "",
    val name: String = "",
    val nickname: String = "",
    val phone: String = "",
    val role: String = "",
    val totalPoint: Int = 0,
    val updatedAt: UpdatedAt = UpdatedAt(),
    val zipCode: String = ""
)
