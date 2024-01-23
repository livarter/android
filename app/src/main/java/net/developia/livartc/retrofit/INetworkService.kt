package net.developia.livartc.retrofit

import net.developia.livartc.model.BestProduct
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
}