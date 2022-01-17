package com.retailio.retailioinapp.retrofit

import android.util.Log
import com.google.gson.GsonBuilder
import com.retailio.retailioinapp.BuildConfig
import okhttp3.OkHttpClient
import retrofit2.HttpException
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

private fun retrofit(): Retrofit {

    val builder = OkHttpClient.Builder()
        .readTimeout(20, TimeUnit.SECONDS)
        .connectTimeout(10, TimeUnit.SECONDS)

    return Retrofit.Builder().baseUrl(BuildConfig.BASE_URL)
        .client(builder.build())
        .addConverterFactory(GsonConverterFactory.create())
        .build()
}

fun <T> retro(service: Class<T>): T {
    return retrofit().create(service)
}

val gson by lazy {
    GsonBuilder().create()
}

fun httpErrorMessage(error: Throwable): String? {

    val response = (error as HttpException).response().errorBody()
    val body = response?.string()
    if (body != null) {
        Log.e("U", body)

        val e = gson.fromJson(body, Error::class.java)
        return e?.message
    } else
        return null
}