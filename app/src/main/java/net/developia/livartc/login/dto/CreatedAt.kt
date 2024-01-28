package com.hyundai.loginapptest.domain

data class CreatedAt(
    val chronology: Chronology = actualChronology, // 기본값 설정 예시
    val dayOfMonth: Int = 0,
    val dayOfWeek: String = "",
    val dayOfYear: Int = 0,
    val era: String = "",
    val leapYear: Boolean = false,
    val month: String = "",
    val monthValue: Int = 0,
    val year: Int = 0
)