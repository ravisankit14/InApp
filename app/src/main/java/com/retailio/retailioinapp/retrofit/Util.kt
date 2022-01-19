package com.retailio.retailioinapp.retrofit

import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.google.gson.GsonBuilder
import com.retailio.retailioinapp.BuildConfig
import com.retailio.retailioinapp.R
import retrofit2.HttpException
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

const val WEBVIEW_APP = "app"

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

fun AppCompatActivity.addFragment(fragment: Fragment, containerId : Int = R.id.container) {
    this.supportFragmentManager.beginTransaction()
        .add(containerId, fragment)
        .addToBackStack(fragment.javaClass.name)
        .commitAllowingStateLoss()
}

fun AppCompatActivity.addFragmentWithoutBackStack(fragment: Fragment, containerId : Int = R.id.container) {
    this.supportFragmentManager.beginTransaction()
        .add(containerId, fragment, getString(R.string.in_app_mini_tag))
        .commitAllowingStateLoss()
}