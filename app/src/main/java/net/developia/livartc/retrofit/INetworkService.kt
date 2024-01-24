package net.developia.livartc.retrofit

import net.developia.livartc.model.BestProduct
import net.developia.livartc.model.Product
import net.developia.livartc.model.User
import net.developia.livartc.product.Reply
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface INetworkService {


    //상품 리스트
    @GET("api/v1/product/list")
    fun getProduct(): Call<BestProduct>


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



    //리뷰 리스트
    @GET("api/v1/product/reply")
    fun getReview(): Call<Reply>
}