package com.retailio.retailioinapp

import android.app.Activity
import android.app.Application
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import com.retailio.retailioinapp.enum.InAppSessionService
import kotlinx.coroutines.Runnable
import java.lang.ref.WeakReference

open class ActivityLifecycleCallBacks : Application.ActivityLifecycleCallbacks {

    private val mHandler = Handler(Looper.getMainLooper())
    private var check: Runnable? = null
    private var mIsForeground = false
    private var mPaused = true

    private var mCurrentActivity: WeakReference<Activity>? = null

    override fun onActivityCreated(activity: Activity, savedInstanceState: Bundle?) {
    }

    override fun onActivityStarted(activity: Activity) {
    }

    override fun onActivityResumed(activity: Activity) {
        mCurrentActivity = WeakReference(activity)

        mPaused = false
        val wasBackground = !mIsForeground
        mIsForeground = true

        if (check != null) {
            mHandler.removeCallbacks(check!!)
        }

        if (wasBackground) {
            // App is in foreground now
        }
    }

    override fun onActivityPaused(activity: Activity) {

        mPaused = true

        if (check != null) {
            mHandler.removeCallbacks(check!!)
        }
        mCurrentActivity = null

        check = Runnable {
            if (mIsForeground && mPaused) {
                mIsForeground = false
                InAppSessionService.IN_APP_NOTIF_VIEW_COUNT_SESSION = 1
            }
        }

        mHandler.postDelayed(check!!, 500)

    }

    override fun onActivityStopped(activity: Activity) {
    }

    override fun onActivitySaveInstanceState(activity: Activity, outState: Bundle) {

    }

    override fun onActivityDestroyed(activity: Activity) {

    }
}
