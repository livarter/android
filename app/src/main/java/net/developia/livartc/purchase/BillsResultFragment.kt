package net.developia.livartc.purchase

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.FragmentResultListener
import net.developia.livartc.R
import net.developia.livartc.databinding.FragmentBillsResultBinding
import org.json.JSONException
import org.json.JSONObject
import java.text.NumberFormat
import java.util.Locale


/**
 * LIVARTC
 * Created by 변형준
 * Date: 1/19/24
 * Time: 17:21
 */
class BillsResultFragment : Fragment() {
    lateinit var binding: FragmentBillsResultBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentBillsResultBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // TextView와 View 요소 참조
        val resultText = binding.resultText
        val totalPriceText = binding.totalPriceText
        val totalPriceView = binding.totalPrice
        val data = arguments?.getString("data")
        var totalPrice = 0
        try {
            val jsonObject = JSONObject(data) // data를 JSONObject로 변환합니다.
            val dataObject = jsonObject.getJSONObject("data") // "data"를 가져옵니다.
            totalPrice = dataObject.getString("price").toInt() // "price"를 가져옵니다.
        } catch (e: JSONException) {
            e.printStackTrace()
        }

        // 텍스트 설정
        resultText.text = "결제가 완료되었습니다!"
        totalPriceText.text = "총 결제 금액"

        totalPriceView.text = "₩${totalPrice?.let { NumberFormat.getNumberInstance(Locale.KOREA).format(it) } ?: "0"}" // totalPrice 값을 텍스트뷰에 설정합니다.

    }

}
