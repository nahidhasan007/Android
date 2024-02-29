package net.sharetrip.b2b.network

import net.sharetrip.b2b.BuildConfig
import net.sharetrip.b2b.util.AppSharedPreference
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import java.util.concurrent.TimeUnit

object ServiceGenerator {
    val API_BASE_URL = if (BuildConfig.DEBUG) {
        "https://stg-b2b.api.sharetrip.net/api/v1/"
    } else {
        "https://b2b.api.sharetrip.net/api/v1/"
    }

    var BASE_URL = if (BuildConfig.DEBUG) {
        "https://stg-b2b.sharetrip.net/"
    } else {
        "https://b2b.sharetrip.net/"
    }

    class HeaderInterceptor : Interceptor {
        override fun intercept(chain: Interceptor.Chain): okhttp3.Response {
            val original: Request = chain.request()

            val isAccessToken = original.headers["isAccessToken"] == "true"
            val headerName = if (isAccessToken) "accessToken" else "userToken"

            val requestBuilder = original
                .newBuilder()
                .method(original.method, original.body)
                .removeHeader("isAccessToken")
                .addHeader(headerName, AppSharedPreference.accessToken)
                .addHeader("st-access", "7C8ABC7DFB20B0D4528E532DA4DA664C7028E4AF")

            return chain.proceed(requestBuilder.build())
        }
    }

    private val logger: HttpLoggingInterceptor = if (BuildConfig.DEBUG) {
        val data = HttpLoggingInterceptor()
        data.setLevel(HttpLoggingInterceptor.Level.BODY)
    } else {
        val data = HttpLoggingInterceptor()
        data.setLevel(HttpLoggingInterceptor.Level.NONE)
    }

    private val client = OkHttpClient().newBuilder()
        .addInterceptor(HeaderInterceptor())
        .addInterceptor(logger)
        .connectTimeout(1, TimeUnit.MINUTES)
        .readTimeout(1, TimeUnit.MINUTES)
        .writeTimeout(1, TimeUnit.MINUTES)
        .build()

    private val retrofit = Retrofit.Builder()
        .client(client)
        .baseUrl(API_BASE_URL)
        .addCallAdapterFactory(NetworkResponseAdapterFactory())
        .addConverterFactory(MoshiConverterFactory.create())
        .build()

    fun <S> createService(serviceClass: Class<S>): S {
        return retrofit.create(serviceClass)
    }
}
