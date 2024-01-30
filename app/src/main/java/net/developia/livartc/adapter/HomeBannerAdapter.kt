package net.developia.livartc.adapter

import android.util.Log
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import net.developia.livartc.R
import net.developia.livartc.main.BannerFragment

/**
 * LIVARTC
 * Created by 최현서
 * Date: 2024-01-19
 * Time: 16:50
 * 작업 내용: 홈화면 메인 광고 배너 구현
 */
class HomeBannerAdapter(fragment: Fragment) : FragmentStateAdapter(fragment) {

    private val fragmentList: ArrayList<Fragment> = ArrayList()

    override fun getItemCount(): Int = fragmentList.size

    override fun createFragment(position: Int): Fragment = fragmentList[position]

    fun addFragment(imageResource: Int) {
        val bannerFragment = BannerFragment.newInstance(imageResource)
        fragmentList.add(bannerFragment)
        notifyItemInserted(fragmentList.size - 1)
    }
}