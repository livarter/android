package com.hyundai.loginapptest.domain

data class Chronology(
    val calendarType: String,
    val id: String
)

val actualChronology = Chronology(
    calendarType = "Gregorian",
    id = "gregory"
)