package com.retailio.retailioinapp

import android.content.Context
import com.retailio.retailioinapp.ui.notification

class InAppNotification {

    companion object{
        fun getInstance(){

        }

        fun openInAppNotification(){

        }

        fun openPushNotification(activity: Context, pMap: Map<String, String>){
            notification(activity, pMap)
        }
    }
}