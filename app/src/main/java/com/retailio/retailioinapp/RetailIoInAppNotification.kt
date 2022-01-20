package com.retailio.retailioinapp

import android.app.Activity
import android.content.Context
import androidx.fragment.app.FragmentActivity
import com.retailio.retailioinapp.ui.notification
import com.retailio.retailioinapp.ui.saveInAppNotification
import com.retailio.retailioinapp.ui.showInAppNotification

class RetailIoInAppNotification private constructor(context: Context){

    private val mContext = context

    companion object: SingletonHolder<RetailIoInAppNotification, Context>(::RetailIoInAppNotification)

    init {

    }

    fun openPushNotification(pMap: Map<String, String>){
        notification(this.mContext, pMap)
    }

    fun saveInAppNotificationInDb(pMap: Map<String, String> ){
        saveInAppNotification(mContext, pMap)
    }

    fun showInAppNotificationIfAvailable(activity: FragmentActivity){
        showInAppNotification(activity)
    }
}