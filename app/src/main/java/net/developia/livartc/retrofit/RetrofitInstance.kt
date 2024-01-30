package net.developia.livartc.retrofit

import net.developia.livartc.login.TokenInterceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

/**
 * LIVARTC
 * Created by 오수영
 * Date: 1/23/24
 * Time: 8:57 PM
 * Function: 네트워크 요청을 위한 Retrofit 인스턴스를 초기화하고, API 호출을 관리하기 위한 싱글톤 객체를 제공합니다.
 */
// RetrofitInstance 객체는 네트워크 요청을 위한 Retrofit 인스턴스를 제공
object RetrofitInstance {
    // api 프로퍼티를 통해 INetworkService 인터페이스의 구현체에 접근
    val api: INetworkService by lazy {
        // HttpLoggingInterceptor를 설정하여 HTTP 요청/응답 로그를 확인
        val logging = HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY // 로그 레벨을 'BODY'로 설정
        }

        // OkHttpClient를 생성하고, TokenInterceptor와 로깅 인터셉터를 추가
        val client = OkHttpClient.Builder()
            .addInterceptor(TokenInterceptor()) // 토큰 인터셉터 추가
            .addInterceptor(logging) // 로깅 인터셉터 추가
            .build()

        // Retrofit 빌더를 사용하여 Retrofit 인스턴스를 생성
        Retrofit.Builder()
            .baseUrl("http://10.0.2.2:8080/") // 기본 URL 설정
            .addConverterFactory(GsonConverterFactory.create()) // Gson 컨버터 팩토리 사용
            .client(client) // 위에서 설정한 OkHttpClient 인스턴스 사용
            .build() // Retrofit 인스턴스 생성
            .create(INetworkService::class.java) // INetworkService 인터페이스의 구현체 생성
    }
}