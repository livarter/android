package com.hyundai.loginapptest.domain

import com.hyundai.loginapptest.domain.Chronology

data class UpdatedAt(
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