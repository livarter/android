package net.developia.livartc.retrofit

import com.hyundai.loginapptest.domain.LoginReqDto
import com.hyundai.loginapptest.domain.LoginResDto
import com.hyundai.loginapptest.domain.MemberResDto
import net.developia.livartc.model.MyReply
import net.developia.livartc.model.Product
import net.developia.livartc.model.PurchaseHistory
import net.developia.livartc.model.PurchaseReqDto
import net.developia.livartc.model.Reply
import net.developia.livartc.mypage.dto.BadgeResDto
import net.developia.livartc.mypage.dto.CatalogListResDto
import net.developia.livartc.mypage.dto.CouponResDto
import net.developia.livartc.mypage.dto.MemberGradeDto
import net.developia.livartc.mypage.dto.MemperUpdateReqDto
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.Query

interface INetworkService {

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
    fun getMemberInfo(
        @Header("Authorization") Authorization: String
    ): Call<MemberResDto>

    @GET("/api/v1/member/badge")
    fun getBadgesByMember(
        @Header("Authorization") Authorization: String
    ): Call<BadgeResDto>

    @GET("/api/v1/member/grade")
    fun getMemberGrade(
        @Header("Authorization") Authorization: String
    ): Call<MemberGradeDto>

    @GET("/api/v1/member/coupon")
    fun getCouponsByMember(
        @Header("Authorization") Authorization: String
    ): Call<CouponResDto>

    @PATCH("/api/v1/member/")
    fun updateMember(
        @Header("Authorization") Authorization: String,
        @Body memberUpdateReqDto : MemperUpdateReqDto
    ): Call<MemberResDto>

    @GET("/api/v1/member/catalogs")
    fun getCatalogs(
        @Header("Authorization") Authorization: String
    ): Call<CatalogListResDto>

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
    fun insertPurchaseHistory(@Body purchaseReqDto: PurchaseReqDto, @Header("Authorization") Authorization: String): Call<ResponseBody>

    @GET("api/v1/purchase")
    fun getPurchaseHistory(@Header("Authorization") Authorization: String): Call<List<PurchaseHistory>>

    @PATCH("api/v1/member/point")
    fun increasePoint(@Header("Authorization") Authorization: String, @Query("money") money: Long): Call<ResponseBody>

    @DELETE("api/v1/member/point")
    fun decreasePoint(@Header("Authorization") Authorization: String, @Query("point") point: Long): Call<ResponseBody>


    /**
     * LIVARTC
     * Created by 최현서
     * Date: 1/25/24
     * Time: 11:14
     */
    @GET("api/v1/reply")
    fun getReview (
        @Query("productId") productId: Long
    ): Call<List<Reply>>

    @GET("api/v1/reply/save")
    fun saveReview (
        @Header("Authorization") Authorization: String,
        @Query("productId") productId: Long,
        @Query("replyComment") replyComment: String,
        @Query("replyImg") replyImg: String?
    ): Call<ResponseBody>

    @GET("api/v1/reply/user")
    fun getMyReview(@Header("Authorization") Authorization: String): Call<List<MyReply>>
}