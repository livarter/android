package com.hyundai.loginapptest.domain

data class CreatedAt(
    val chronology: Chronology,
    val dayOfMonth: Int,
    val dayOfWeek: String,
    val dayOfYear: Int,
    val era: String,
    val leapYear: Boolean,
    val month: String,
    val monthValue: Int,
    val year: Int
)