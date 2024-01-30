package net.developia.livartc.main

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import net.developia.livartc.R

/**
 * LIVARTC
 * Created by 최현서
 * Date: 2024-01-19
 * Time: 17:00
 * 작업 내용: 홈화면 메인 홍보 배너 구현
 */
class BannerFragment : Fragment() {
    private var imageResource: Int = 0

    companion object {
        fun newInstance(imageResource: Int): BannerFragment {
            val fragment = BannerFragment()
            val args = Bundle()
            args.putInt("imageResource", imageResource)
            fragment.arguments = args
            return fragment
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_banner, container, false)
        val imageView: ImageView = view.findViewById(R.id.banner_img)

        arguments?.let {
            imageResource = it.getInt("imageResource")
            imageView.setImageResource(imageResource)
        }

        return view
    }
}