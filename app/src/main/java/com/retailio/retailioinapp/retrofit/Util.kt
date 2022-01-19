package com.retailio.retailioinapp.retrofit

import android.util.Log
import com.google.gson.GsonBuilder
import com.retailio.retailioinapp.BuildConfig
import retrofit2.HttpException
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

private fun retrofit(): Retrofit {

    return Retrofit.Builder().baseUrl("https://staging.retailio.in/")
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