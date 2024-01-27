package net.developia.livartc.mypage.dto

import java.time.LocalDate
import java.time.format.DateTimeFormatter

/**
 * 작성자 : 황수영
 * 내용 : 생일 받는 res dto
 */
data class BirthDate(
    val chronology: Chronology = Chronology(),
    val dayOfMonth: Int = 0,
    val dayOfWeek: String = "",
    val dayOfYear: Int = 0,
    val era: String = "",
    val leapYear: Boolean = true,
    val month: String = "",
    val monthValue: Int= 0,
    val year: Int = 0
)

// 날짜 형식 파싱 함수
fun BirthDate.toFormattedString(): String {
    val localDate = LocalDate.of(year, monthValue, dayOfMonth)
    val formatter = DateTimeFormatter.ofPattern("yyyy.MM.dd")
    return localDate.format(formatter)
}