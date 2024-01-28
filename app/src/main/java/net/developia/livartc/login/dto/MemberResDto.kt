package com.hyundai.loginapptest.domain

import net.developia.livartc.mypage.dto.BirthDate
import java.io.Serializable

data class MemberResDto(
    val address: String = "",
    val birthDate: BirthDate = BirthDate(),
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
) : Serializable
