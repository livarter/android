package net.developia.livartc.retrofit

import com.hyundai.loginapptest.domain.LoginReqDto
import com.hyundai.loginapptest.domain.LoginResDto
import com.hyundai.loginapptest.domain.MemberResDto
import net.developia.livartc.model.BestProduct
import net.developia.livartc.model.Product
import net.developia.livartc.model.PurchaseReqDto
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface INetworkService {

    /**
     * LIVARTC
     * Created by 최현서
     * Date: 1/19/24
     * Time: 17:21
     */
    //베스트 상품 리스트
    @GET("api/v1/product/list")
    fun getProduct(): Call<BestProduct>

     /**
     * LIVARTC
     * Created by 황수영
     * Date: 1/24/24
     * Time: 15:20
     */
    // 회원
    @POST("/api/v1/auth/login")
    fun kakaoLogin(@Body loginResDto: LoginReqDto): Call<LoginResDto>

    @GET("/api/v1/member")
    fun getMemberInfo(): Call<MemberResDto>


    /**
     * LIVARTC
     * Created by 오수영
     * Date: 1/19/24
     * Time: 17:21
     */
    @GET("api/v1/products")
    fun searchProducts(
        @Query("category", encoded = true) category: String,
        @Query("brand", encoded = true) brand: String,
        @Query("hashtag", encoded = true) hashtag: String,
        @Query("title", encoded = true) title: String,
        @Query("sortOption") sortOption: Int,
        @Query("pageSize") pageSize: Int,
        @Query("pageNumber") pageNumber: Int
    ): Call<List<Product>>


    /**
     * LIVARTC
     * Created by 변형준
     * Date: 1/19/24
     * Time: 17:21
     */
    @POST("api/v1/purchase/insert")
    fun insertPurchaseHistory(@Body purchaseReqDto: PurchaseReqDto): Call<ResponseBody>

}