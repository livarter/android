package net.developia.livartc.purchase

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.FragmentResultListener
import net.developia.livartc.MainActivity
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
        val totalPriceView = binding.totalPrice
        val orderIdView = binding.orderId
        val purchasedDateView = binding.purchasedDate
        val data = arguments?.getString("data")
        var totalPrice = 0
        var orderId = ""
        var purchasedDate = ""
        try {
            val jsonObject = JSONObject(data) // data를 JSONObject로 변환합니다.
            val dataObject = jsonObject.getJSONObject("data") // "data"를 가져옵니다.
            if(dataObject.getString("status") == "1") {
                resultText.text = "결제가 완료되었습니다."
                totalPrice = dataObject.getString("price").toInt() // "price"를 가져옵니다.
                orderId = dataObject.getString("order_id")
                purchasedDate = dataObject.getString("purchased_at").replace("T", " ").replace("+09:00", "")
            } else {
                resultText.text = "결제를 실패하였습니다."
                totalPrice = 0
            }
            totalPriceView.text = "₩${totalPrice?.let { NumberFormat.getNumberInstance(Locale.KOREA).format(it) } ?: "0"}" // totalPrice 값을 텍스트뷰에 설정합니다.
            orderIdView.text = orderId
            purchasedDateView.text = purchasedDate
        } catch (e: JSONException) {
            e.printStackTrace()
        }

        binding.homeBtn.setOnClickListener {
            val intent = Intent(requireContext(), MainActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
            intent.putExtra("startFragment", "HomeFragment")
            startActivity(intent)
        }
        binding.mypageBtn.setOnClickListener {
            val intent = Intent(requireContext(), MainActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
            intent.putExtra("startFragment", "MyPageFragment")
            startActivity(intent)
        }
    }

}
