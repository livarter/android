package com.hyundai.loginapptest.domain


data class UpdatedAt(
    val chronology: Chronology = actualChronology, // Chronology 클래스에 대한 초기값 설정
    val dayOfMonth: Int = 0,
    val dayOfWeek: String = "",
    val dayOfYear: Int = 0,
    val era: String = "",
    val leapYear: Boolean = false,
    val month: String = "",
    val monthValue: Int = 0,
    val year: Int = 0
)
