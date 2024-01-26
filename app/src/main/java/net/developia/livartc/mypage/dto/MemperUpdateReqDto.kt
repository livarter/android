package net.developia.livartc.mypage.dto

data class MemperUpdateReqDto(
    var address: String?,
    var birthDate: String?,
    var createdAt: String?,
    var curPoint: Int?,
    var email: String?,
    var grade: String?,
    var image: String?,
    var name: String?,
    var nickname: String?,
    var role: String?,
    var totalPoint: Int?,
    var updatedAt: String?,
    var zipCode: String?
) {
    // No-argument constructor for serialization
    constructor() : this(
        null,
        null,
        null,
        null,
        null,
        null,
        null,
        null,
        null,
        null,
        null,
        null,
        null
    )
}