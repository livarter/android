package net.developia.livartc.mypage.dto

data class MemperUpdateReqDto(
    //var image: String?,
    var name: String?,
    var nickname: String?,
    var phone: String?,
    var address: String?,
    var zipCode: String?,
    var birthDate: String?
) {
    constructor() : this(
        //null,
        null,
        null,
        null,
        null,
        null,
        null
    )
}