package net.developia.livartc.main


import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import net.developia.livartc.MainActivity
import net.developia.livartc.adapter.PurchaseAdapter
import net.developia.livartc.databinding.FragmentMyPurchaseBinding
import net.developia.livartc.login.TokenManager
import net.developia.livartc.model.PurchaseHistory
import net.developia.livartc.retrofit.MyApplication
import net.developia.livartc.retrofit.RetrofitInstance
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Call
/**
 * LIVARTC
 * Created by 변형준
 * Date: 1/19/24
 * Time: 17:21
 */
class MyPurchaseFragment : Fragment() {
    lateinit var binding: FragmentMyPurchaseBinding
    private lateinit var purchaseList : List<PurchaseHistory>
    private lateinit var purchaseAdapter : PurchaseAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentMyPurchaseBinding.inflate(inflater, container, false)
        getAllPurchaseList()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }

    private fun getAllPurchaseList() {
        val jwtToken = TokenManager.getToken(MyApplication.instance)!!
        Log.d("jwtToken", jwtToken.toString())
        RetrofitInstance.api.getPurchaseHistory(jwtToken)
            .enqueue(object : Callback<List<PurchaseHistory>> {
                override fun onResponse(
                    call: Call<List<PurchaseHistory>>,
                    response: Response<List<PurchaseHistory>>
                ) {
                    if (response.isSuccessful) {
                        purchaseList = response.body()!!
                        setRecyclerView()
                    }
                }
                override fun onFailure(call: retrofit2.Call<List<PurchaseHistory>>, t: Throwable) {
                    t.printStackTrace()
                }
            })
    }

    private fun setRecyclerView() {
        (activity as MainActivity).runOnUiThread {
            purchaseAdapter = PurchaseAdapter(purchaseList)
            binding.purchaseListView.adapter = purchaseAdapter
            binding.purchaseListView.layoutManager = LinearLayoutManager(activity)
        }
    }

    override fun onResume() {
        super.onResume()
        getAllPurchaseList()
    }
}