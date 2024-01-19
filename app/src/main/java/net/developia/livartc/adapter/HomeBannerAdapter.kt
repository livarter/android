package net.developia.livartc.adapter

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter

/**
 * LIVARTC
 * Created by 최현서
 * Date: 2024-01-19
 * Time: 오후 4:50
 */
class HomeBannerAdapter(fragment: Fragment) : FragmentStateAdapter(fragment) {

    private val fragmentList : ArrayList<Fragment> = ArrayList()
    override fun getItemCount(): Int = fragmentList.size
    override fun createFragment(position: Int): Fragment = fragmentList[position]

    fun addFragment(fragment: Fragment) {
        fragmentList.add(fragment)
        notifyItemInserted(fragmentList.size -1) // viewpager 에게 추가된 fragment 를 알려줌
    }


}