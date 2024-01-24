package net.developia.livartc.retrofit

import android.app.Application
import android.util.Log
import com.kakao.sdk.common.KakaoSdk
import net.developia.livartc.BuildConfig
import net.developia.livartc.util.PreferenceUtil
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


class MyApplication: Application() {

    var networkService: INetworkService
    lateinit var prefs: PreferenceUtil
    companion object {
        lateinit var instance: MyApplication
            private set
    }
    override fun onCreate() {
        super.onCreate()
        instance = this

        Log.d("MyApplication onCreate", "KakaoSdk START")
        KakaoSdk.init(this, BuildConfig.kakao_key)
        prefs = PreferenceUtil(applicationContext)
    }

    val retrofit: Retrofit
        get() = Retrofit.Builder()
            .baseUrl("http://10.0.2.2:8080/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

    init {
        networkService = retrofit.create(INetworkService::class.java)
    }

}