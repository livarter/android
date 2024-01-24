package net.developia.livartc.retrofit

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

/**
 * LIVARTC
 * Created by 오수영
 * Date: 1/23/24
 * Time: 8:57 PM
 */
object RetrofitInstance {
    val api: INetworkService by lazy {
        val logging = HttpLoggingInterceptor().apply {
            // 로그 레벨을 'BODY'로 설정하여 요청과 응답의 바디 내용을 포함시킵니다.
            level = HttpLoggingInterceptor.Level.BODY
        }

        val client = OkHttpClient.Builder()
            .addInterceptor(logging)
            .build()

        Retrofit.Builder()
            .baseUrl("http://10.0.2.2:8080/")
            .addConverterFactory(GsonConverterFactory.create())
            .client(client)
            .build()
            .create(INetworkService::class.java)
    }
}