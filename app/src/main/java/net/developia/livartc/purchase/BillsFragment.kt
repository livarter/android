package net.developia.livartc.purchase

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import com.hyundai.loginapptest.domain.MemberResDto
import kr.co.bootpay.android.Bootpay
import kr.co.bootpay.android.events.BootpayEventListener
import kr.co.bootpay.android.models.BootExtra
import kr.co.bootpay.android.models.BootItem
import kr.co.bootpay.android.models.BootUser
import kr.co.bootpay.android.models.Payload
import net.developia.livartc.BuildConfig
import net.developia.livartc.R
import net.developia.livartc.databinding.FragmentBillsBinding
import net.developia.livartc.db.AppDatabase
import net.developia.livartc.db.CartDao
import net.developia.livartc.db.CartEntity
import net.developia.livartc.login.TokenManager
import net.developia.livartc.model.PurchaseReqDto
import net.developia.livartc.retrofit.MyApplication
import net.developia.livartc.retrofit.RetrofitInstance
import okhttp3.ResponseBody
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.NumberFormat
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.Locale
import kotlin.random.Random

/**
 * LIVARTC
 * Created by 변형준
 * Date: 1/19/24
 * 작업내용: 주문서 작성
 *           포인트 조회, 적립 및 사용
 *           주문서 내용 자동 입력
 *           결제 금액 정산
 *           부트페이 api 사용 통합 결제 구현
 *           주문내역 및 상세 내역 DB 저장
 */
class BillsFragment : Fragment() {
    lateinit var binding: FragmentBillsBinding
    private val applicationId = BuildConfig.bootpay_api_key
    private lateinit var cartList : ArrayList<CartEntity>
    private lateinit var db :AppDatabase
    private lateinit var cartDao: CartDao
    private var originPrice = 0L
    private var totalPrice = 0L
    private var address = ""
    private var zipCode = ""
    private var name = ""
    private var phone = ""
    private var email = ""
    private var appliedPoint = 0L
    private var deliveryCharge = 3000L
    private lateinit var memberResDto : MemberResDto
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentBillsBinding.inflate(inflater, container, false)
        originPrice = arguments?.getLong("totalPrice")!!
        totalPrice = originPrice + deliveryCharge
        Log.d("BillsFragment", "totalPrice: $totalPrice")

        db = AppDatabase.getInstance(requireContext())!!
        cartDao = db.getCartDao()
        getAllCartList()

        val purchaseButton: Button = binding.buttonPurchase // binding 객체를 통해 버튼을 찾습니다.
        purchaseButton.setOnClickListener { view -> // 버튼 클릭 시의 동작을 설정합니다.
            PaymentTest(view, totalPrice)
        }
        val pointBtn: Button = binding.pointBtn
        pointBtn.setOnClickListener {
            //totalPrice 데이터 넘겨주기
            val bundle = Bundle()
            bundle.putLong("totalPrice", totalPrice)
            val pointApplyFragment = PointApplyFragment().apply {
                arguments = bundle
            }
            pointApplyFragment.show(parentFragmentManager, "pointApplyFragment")
        }
        parentFragmentManager.setFragmentResultListener("point", viewLifecycleOwner) { key, bundle ->
            appliedPoint = bundle.getLong("point")
            val formattingAppliedPoint = formattingPrice(appliedPoint)
            binding.point.text = "$formattingAppliedPoint 점"
            binding.myPoint.text = "- $formattingAppliedPoint 원"
            totalPrice -= appliedPoint
            val formattedPrice = formattingPrice(totalPrice)
            binding.totalPrice.text = "$formattedPrice 원"
        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setTotalPrice(totalPrice)
        binding.deliverPrice.text = "+ ${formattingPrice(deliveryCharge)} 원"
        val jwtToken = TokenManager.getToken(MyApplication.instance)!!
        RetrofitInstance.api.getMemberInfo(jwtToken)
            .enqueue(object : Callback<MemberResDto> {
                override fun onResponse(
                    call: Call<MemberResDto>,
                    response: Response<MemberResDto>
                ) {
                    memberResDto = response.body()!!
                    email = memberResDto.email
                    address = memberResDto.address
                    zipCode = memberResDto.zipCode
                    name = memberResDto.name
                    phone = memberResDto.phone

                    binding.editTextName.setText(name)
                    binding.editTextEmail.setText(email)
                    binding.editTextDeliveryAddress.setText(address)
                    binding.editTextZipCode.setText(zipCode)
                    binding.editTextPhone.setText(phone)
                }

                override fun onFailure(call: Call<MemberResDto>, t: Throwable) {
                    t.printStackTrace()
                }
            })
    }

    private fun formattingPrice(price: Long): String {
        val numberFormat = NumberFormat.getNumberInstance(Locale.US)
        return numberFormat.format(price)
    }
    private fun setTotalPrice(totalPrice: Long) {
        requireActivity().runOnUiThread {
            val formattedOriginPrice = formattingPrice(originPrice)
            val formattedTotalPrice = formattingPrice(totalPrice)
            binding.originPrice.text = "$formattedOriginPrice 원"
            binding.totalPrice.text = "$formattedTotalPrice 원" // 총 결제 금액을 화면에 표시
        }
    }

    private fun getAllCartList() {
        Thread {
            cartList = ArrayList(cartDao.getAll())
        }.start()
    }

    fun PaymentTest(v: View?, totalPrice: Long?) {
        val extra = BootExtra()
            .setCardQuota("0,2,3")
        val items: MutableList <BootItem> = ArrayList()

        val editTextDeliveryAddress = view?.findViewById<EditText>(R.id.editTextDeliveryAddress)
        address = editTextDeliveryAddress?.text.toString()
        val editTextZipCode = view?.findViewById<EditText>(R.id.editTextZipCode)
        zipCode = editTextZipCode?.text.toString()
        val editTextName = view?.findViewById<EditText>(R.id.editTextName)
        name = editTextName?.text.toString()
        val editTextPhone = view?.findViewById<EditText>(R.id.editTextPhone)
        phone = editTextPhone?.text.toString()
        val editTextEmail = view?.findViewById<EditText>(R.id.editTextEmail)
        email = editTextEmail?.text.toString()

        if (address == "") {
            editTextDeliveryAddress?.error = "배송지를 입력해주세요."
            editTextDeliveryAddress?.requestFocus()
            return
        }
        if (zipCode == "") {
            editTextZipCode?.error = "우편번호를 입력해주세요."
            editTextZipCode?.requestFocus()
            return
        }
        if (name == "") {
            editTextName?.error = "이름을 입력해주세요."
            editTextName?.requestFocus()
            return
        }
        if (phone == "") {
            editTextPhone?.error = "전화번호를 입력해주세요."
            editTextPhone?.requestFocus()
            return
        }
        if (email == "") {
            editTextEmail?.error = "이메일을 입력해주세요."
            editTextEmail?.requestFocus()
            return
        }


        cartList.forEach { cart ->
            val item = BootItem().setName(cart.name ?: "")
                .setId(cart.product_id?.toString() ?: "")
                .setQty(cart.product_cnt ?: 0)
                .setPrice((cart.price ?: 0).toDouble())
            items.add(item)
        }

        val payload = Payload()
        val orderName = if (items.isNotEmpty()) {
            val firstItemName = items[0].name
            if (items.size > 1) {
                "$firstItemName 외 ${items.size - 1}건"
            } else {
                firstItemName
            }
        } else {
            "" // items가 비어있는 경우 공백 반환
        }

        val date = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss"))
        val randomNum = Random.nextInt(100000, 999999)
        val orderId = "$date$randomNum"

        payload.setApplicationId(applicationId)
            .setOrderName(orderName)
            .setOrderId(orderId)
            .setPrice(totalPrice?.toDouble() ?: 0.0)
            .setUser(getBootUser())
            .setExtra(extra)

        //백에서 필요한 개인 정보 모두 넣기
        val map: MutableMap<String, Any> = HashMap()
        map["member_id"] = "1"
        map["address"] = address
        map["zipcode"] = zipCode
        map["receiver_name"] = name
        map["receiver_phone"] = phone
        val itemsStringList = items.map { bootItemToString(it) }
        map["items"] = itemsStringList

        payload.metadata = map

        Bootpay.init(childFragmentManager, requireContext())
            .setPayload(payload)
            .setEventListener(object : BootpayEventListener {
                override fun onCancel(data: String) {
                    Log.d("bootpay", "cancel: $data")
                }
                override fun onError(data: String) {
                    Log.d("bootpay", "error: $data")
                }
                override fun onClose() {
                    Bootpay.removePaymentWindow()
                }
                override fun onIssued(data: String) {
                    Log.d("bootpay", "issued: $data")
                }
                override fun onConfirm(data: String): Boolean {
                    Log.d("bootpay", "confirm: $data")

                    return true
                }
                override fun onDone(data: String) {
                    Log.d("done", data)

                    // 스프링 db 연동 / 구매내역 저장
                    val jsonObject = JSONObject(data)
                    val dataObject = jsonObject.getJSONObject("data")
                    val metaObject = dataObject.getJSONObject("metadata")

                    val purchaseReqDto = PurchaseReqDto(
                        memberId = metaObject.getString("member_id").toInt(),
                        receiptId = dataObject.getString("order_id"),
                        createdAt = dataObject.getString("purchased_at"),
                        address = metaObject.getString("address"),
                        zipcode = metaObject.getString("zipcode"),
                        receiverName = metaObject.getString("receiver_name"),
                        receiverPhone = metaObject.getString("receiver_phone"),
                        purchaseDetailStatus = dataObject.getString("status").toInt(),
                        items = items.map { item ->
                            PurchaseReqDto.Item(
                                id = item.id.toInt(),
                                qty = item.qty,
                                price = item.price.toLong()
                            )
                        }
                    )
                    val jwtToken = TokenManager.getToken(MyApplication.instance)!!

                    // 결제가 완료되면 카트를 비웁니다.
                    RetrofitInstance.api.insertPurchaseHistory(purchaseReqDto, jwtToken).enqueue(object : retrofit2.Callback<ResponseBody> {
                        override fun onResponse(
                            call: Call<ResponseBody>,
                            response: Response<ResponseBody>
                        ) {
                            Log.d("done", "insertPurchaseHistory: ${response.body()}")
                            Thread {
                                cartDao.deleteAll()
                            }.start()
                        }
                        override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                            Log.d("done", "insertPurchaseHistory: $t")
                        }
                    })
                    RetrofitInstance.api.decreasePoint(jwtToken, appliedPoint).enqueue(object : retrofit2.Callback<ResponseBody> {
                        override fun onResponse(
                            call: Call<ResponseBody>,
                            response: Response<ResponseBody>
                        ) {
                            Log.d("done", "decreasePoint: ${response.body()}")
                        }
                        override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                            Log.d("done", "decreasePoint: $t")
                        }
                    })
                    val purchasedPrice = dataObject.getString("price").toLong()
                    Log.d("done", "purchasedPrice: $purchasedPrice")
                    RetrofitInstance.api.increasePoint(jwtToken, purchasedPrice).enqueue(object : retrofit2.Callback<ResponseBody> {
                        override fun onResponse(
                            call: Call<ResponseBody>,
                            response: Response<ResponseBody>
                        ) {
                            Log.d("done", "increasePoint: ${response.body()}")
                        }
                        override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                            Log.d("done", "increasePoint: $t")
                        }
                    })

                    // 결제가 완료되면 BillsResultFragment로 전환합니다.
                    val billsResultFragment = BillsResultFragment()

                    val bundle = Bundle()
                    bundle.putString("data", data)

                    billsResultFragment.arguments = bundle // Bundle을 BillsResultFragment의 arguments에 설정합니다.

                    val fragmentTransaction = requireFragmentManager().beginTransaction()
                    fragmentTransaction.replace(R.id.purchase_container, billsResultFragment)
                    fragmentTransaction.commit()
                }

            }).requestPayment()
    }

    fun bootItemToString(item: BootItem): String {
        return "Name: ${item.name}, ID: ${item.id}, Quantity: ${item.qty}, Price: ${item.price}"
    }

    // 구매자 정보
    fun getBootUser(): BootUser? {
        val user = BootUser()   // MemberDto
        user.id = memberResDto.email // 구매자 아이디
        user.area = address // 주소
        user.addr = address // 주소
        user.email = email // 주문서 받을 주소
        user.phone = phone // 전화번호
        user.username = name  // 주문자 이름
        return user
    }

}