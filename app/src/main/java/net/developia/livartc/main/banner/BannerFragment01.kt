package net.developia.livartc.main.banner

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import net.developia.livartc.R
/**
 * LIVARTC
 * Created by 최현서
 * Date: 2024-01-19
 * Time: 오후 5:00
 */
class BannerFragment01 : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_banner01, container, false)
    }
}