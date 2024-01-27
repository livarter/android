package net.developia.livartc.mypage.dto

import java.time.LocalDate
import java.time.format.DateTimeFormatter

/**
 * 작성자 : 황수영
 * 내용 : 생일 받는 res dto
 */
data class BirthDate(
    val chronology: Chronology = Chronology(),
    val dayOfMonth: Int = 1,
    val dayOfWeek: String = "",
    val dayOfYear: Int = 1,
    val era: String = "",
    val leapYear: Boolean = true,
    val month: String = "",
    val monthValue: Int = 1,
    val year: Int = 2000
)

// 날짜 형식 파싱 함수
fun BirthDate.toFormattedString(): String {
    val localDate = LocalDate.of(year, monthValue, dayOfMonth)
    val formatter = DateTimeFormatter.ofPattern("yyyy.MM.dd")
    return localDate.format(formatter)
}
