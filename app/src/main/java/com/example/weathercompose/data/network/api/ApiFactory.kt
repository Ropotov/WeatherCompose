package com.example.weathercompose.data.network.api

import com.example.weathercompose.BuildConfig
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.create

private const val KEY = "key"

object ApiFactory {

    private const val BASE_URL = "https://api.weatherapi.com/v1"

    private val okHttpClient = OkHttpClient.Builder().addInterceptor { chain ->
            val request = chain.request()
            val newUrl = request.url().newBuilder()
                .addQueryParameter(KEY, BuildConfig.WEATHER_API_KEY).build()
            val newRequest = request.newBuilder().url(newUrl).build()
            chain.proceed(newRequest)
        }.build()

    private val retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .client(okHttpClient)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    val apiService = retrofit.create<ApiService>()
}