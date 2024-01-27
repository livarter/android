package net.developia.livartc.mypage.membership

import com.hyundai.loginapptest.domain.MemberResDto

/**
 * 작성자 : 황수영
 * 내용 : 화면 업데이트
 */
interface OnUpdateListener {
    fun onUpdate(memberResDto: MemberResDto)
}