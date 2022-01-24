package com.retailio.retailioinapp

import android.app.Application
import android.content.Context
import android.util.Log
import androidx.fragment.app.FragmentActivity
import com.retailio.retailioinapp.ui.notification
import com.retailio.retailioinapp.ui.saveInAppNotification
import com.retailio.retailioinapp.ui.showInAppNotification

class RetailIoInAppNotification private constructor(context: Context){

    private val mContext = context
    private var activityLifecycleCallBacks: ActivityLifecycleCallBacks? = null

    companion object: SingletonHolder<RetailIoInAppNotification, Context>(::RetailIoInAppNotification)

    init {
        registerMixpanelActivityLifecycleCallbacks()
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

    private fun registerMixpanelActivityLifecycleCallbacks() {
        if (mContext.applicationContext is Application) {
            Log.d("RetailIoInApp", "applicationContext")
            val app = mContext.applicationContext as Application
            activityLifecycleCallBacks = ActivityLifecycleCallBacks()
            app.registerActivityLifecycleCallbacks(activityLifecycleCallBacks)
        }
    }
}