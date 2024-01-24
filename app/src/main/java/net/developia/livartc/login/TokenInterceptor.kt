package net.developia.livartc.login

import net.developia.livartc.retrofit.MyApplication
import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.Response
import java.io.IOException

class TokenInterceptor : Interceptor {

    @Throws(IOException::class)
    override fun intercept(chain: Interceptor.Chain): Response {
        val originalRequest: Request = chain.request()

        // 기존 요청에 토큰 추가
        val token = TokenManager.getToken(MyApplication.instance)
        return if (token != null) {
            val newRequest: Request = originalRequest.newBuilder()
                .header("Authorization", token)
                .build()
            chain.proceed(newRequest)
        } else {
            chain.proceed(originalRequest)
        }
    }
}