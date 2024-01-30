package net.developia.livartc.purchase

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import net.developia.livartc.MainActivity
import net.developia.livartc.databinding.FragmentBillsResultBinding
import net.developia.livartc.mypage.membership.MyOrderActivity
import org.json.JSONException
import org.json.JSONObject
import java.text.NumberFormat
import java.util.Locale


/**
 * LIVARTC
 * Created by 변형준
 * Date: 1/19/24
 * 작업내용: 주문 결과 내용 반영
 *
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
        val pointView = binding.point
        val data = arguments?.getString("data")
        var totalPrice = 0L
        var orderId = ""
        var purchasedDate = ""
        var pointValue = arguments?.getInt("point")
        try {
            val jsonObject = JSONObject(data) // data를 JSONObject로 변환합니다.
            val dataObject = jsonObject.getJSONObject("data") // "data"를 가져옵니다.
            if(dataObject.getString("status") == "1") {
                resultText.text = "결제가 완료되었습니다."
                totalPrice = dataObject.getString("price").toLong() // "price"를 가져옵니다.
                orderId = dataObject.getString("order_id")
                purchasedDate = dataObject.getString("purchased_at").replace("T", " ").replace("+09:00", "")
            } else {
                resultText.text = "결제를 실패하였습니다."
                totalPrice = 0L
            }
            val numberFormat = NumberFormat.getNumberInstance(Locale.US)
            val formattedPrice = numberFormat.format(totalPrice)
            totalPriceView.text = "$formattedPrice 원"
            orderIdView.text = orderId
            purchasedDateView.text = purchasedDate

            Log.d("done", pointValue.toString())
            pointView.text = pointValue.toString()
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
            var intent = Intent(requireContext(), MainActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
            intent.putExtra("startFragment", "MyPageFragment")
            intent = Intent(requireContext(), MyOrderActivity::class.java)
            startActivity(intent)
            activity?.finish()
        }
    }

}
