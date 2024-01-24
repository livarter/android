package net.developia.livartc.retrofit

import com.hyundai.loginapptest.domain.LoginReqDto
import com.hyundai.loginapptest.domain.LoginResDto
import com.hyundai.loginapptest.domain.MemberResDto
import net.developia.livartc.model.BestProduct
import net.developia.livartc.model.Product
import net.developia.livartc.model.User
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface INetworkService {
    //예시
//    @POST("walking/user/join")
//    fun doInsertUser(@Body user: User?): Call<User>
//
//    @POST("walking/user/update")
//    fun doUpdateUser(@Body user: User?): Call<User>
//
//    @GET("walking/user/delete")
//    fun doDeleteUser(@Query("email") email:String?): Call<Int>


    //베스트 상품 리스트
    @GET("api/v1/product/list")
    fun getProduct(): Call<BestProduct>

    // 회원
    @POST("/api/v1/auth/login")
    fun kakaoLogin(@Body loginResDto: LoginReqDto): Call<LoginResDto>

    @GET("/api/v1/member")
    fun getMemberInfo(): Call<MemberResDto>


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
}