package net.developia.livartc.mypage

import android.app.Dialog
import android.content.Context
import android.view.Gravity
import android.view.WindowManager
import android.widget.ImageView
import android.widget.TextView
import com.squareup.picasso.Picasso
import net.developia.livartc.R
import net.developia.livartc.mypage.dto.PopUpDto


/**
 * 작성자 : 황수영
 * 내용 : 쿠폰이나 뱃지 발급 시 띄워주는 팝업 창
 */
class PopUp {

    // 클릭했을 때 해당 메소드를 넣어주면 팝업 창
    companion object fun show(popUp : PopUpDto, context : Context) {
        // 뱃지 정보 팝업 띄우기
        val selectedItem = popUp

        val dialog = Dialog(context)
        dialog.setContentView(R.layout.custom_popup_layout)

        val popupImageView: ImageView = dialog.findViewById(R.id.popupImageView)
        val popupTextView: TextView = dialog.findViewById(R.id.popupTextView)
        val popupTextDescriptionView: TextView = dialog.findViewById(R.id.popupDescriptionView)

        Picasso.get().load(selectedItem.image).into(popupImageView)
        popupTextView.text = selectedItem.name
        popupTextDescriptionView.text = selectedItem.description

        dialog.show()

        val window = dialog.window
        val params = window?.attributes
        val desiredSizeInDp = 360
        val density = context.resources.displayMetrics.density

        val desiredWidthSizeInPixels = (desiredSizeInDp * density).toInt()
        val desiredHeightSizeInPixels = ((desiredSizeInDp + 40) * density).toInt()

        params?.width = desiredWidthSizeInPixels
        params?.height = desiredHeightSizeInPixels

        params?.gravity = Gravity.CENTER

        window?.attributes = params as WindowManager.LayoutParams
    }
}