package net.developia.livartc.mypage

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import net.developia.livartc.mypage.myroom.MyRoomFragment
import net.developia.livartc.mypage.badge.BadgeFragment
import net.developia.livartc.mypage.membership.MemberShipFragment

/**
 * 작성자 : 황수영
 * 내용 : 마이페이지 뷰페이저 어댑터
 */

class ViewPagerAdapter(fragment: FragmentActivity) : FragmentStateAdapter(fragment) {
    override fun getItemCount(): Int = 3

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> MemberShipFragment()
            1 -> BadgeFragment()
            else -> MyRoomFragment()
        }
    }
}