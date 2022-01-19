package com.retailio.retailioinapp

import android.content.Context
import com.retailio.retailioinapp.ui.notification
import com.retailio.retailioinapp.ui.saveInAppNotification

class InAppNotification {

    init {

    }

    companion object{

        fun openPushNotification(context: Context, pMap: Map<String, String>){
            notification(context, pMap)
        }

        fun saveInAppNotificationInDb(context: Context, pMap: Map<String, String> ){
            saveInAppNotification(context, pMap)
        }

    }

    fun showInAppMini(){

    }

    fun showInAppCentre(){

    }
}