package com.team13.fooriend.ui.util

import android.content.Context
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

fun saveAccessToken(context: Context, token: String) {
    val sharedPreferences = context.getSharedPreferences("MyAppPrefs", Context.MODE_PRIVATE)
    sharedPreferences.edit().putString("AccessToken", token).apply()
}

fun getAccessToken(context: Context): String? {
    val sharedPreferences = context.getSharedPreferences("MyAppPrefs", Context.MODE_PRIVATE)
    return sharedPreferences.getString("AccessToken", null)
}

fun createRetrofit(context: Context): Retrofit {
    val okHttpClient = OkHttpClient.Builder()
        .addInterceptor { chain ->
            val original = chain.request()
            val accessToken = getAccessToken(context)

            val requestBuilder = original.newBuilder()
                .header("Authorization", "Bearer $accessToken")
                .method(original.method, original.body)

            val request = requestBuilder.build()
            chain.proceed(request)
        }
        .build()

    return Retrofit.Builder()
        .baseUrl("http://ec2-13-125-245-104.ap-northeast-2.compute.amazonaws.com")
        .addConverterFactory(GsonConverterFactory.create())
        .client(okHttpClient)
        .build()
}
