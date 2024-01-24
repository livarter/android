package com.hyundai.loginapptest.domain

data class MemberResDto(
    val address: String,
    val birthDate: String,
    val createdAt: CreatedAt,
    val curPoint: Int,
    val email: String,
    val grade: String,
    val image: String,
    val name: String,
    val nickname: String,
    val role: String,
    val totalPoint: Int,
    val updatedAt: UpdatedAt,
    val zipCode: String
)