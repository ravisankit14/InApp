package com.retailio.retailioinapp.ui

import android.app.*
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Color
import android.media.RingtoneManager
import android.net.Uri
import android.os.Build
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat.startActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentTransaction
import androidx.lifecycle.lifecycleScope
import com.retailio.retailioinapp.R
import com.retailio.retailioinapp.enum.InAppLocationType
import com.retailio.retailioinapp.enum.InAppSessionService
import com.retailio.retailioinapp.roomdb.InAppRoomDatabase
import com.retailio.retailioinapp.roomdb.entity.NotificationInAppEntity
import java.io.IOException
import java.io.InputStream
import java.net.URL
import java.util.concurrent.Executors

var intent: Intent? = Intent()
const val CHANNEL_ID = "retailio_in_app_channel"
const val CHANNEL_NAME = "In App Notification"
const val NOTIFICATION_ID = "notification_id"

fun getNotificationIntent(pMap: Map<String, String>): Intent{
    if (pMap.containsKey("")) {
        val deepLinkUrl =
            pMap[""]!!.trim { it <= ' ' }
        if (deepLinkUrl.isNotEmpty()) {
            intent = Intent(Intent.ACTION_VIEW, Uri.parse(deepLinkUrl))
            intent?.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP)
            return intent!!
        }
    }

    return intent!!
}

/* Show notification coming from firebase service */
fun notification(activity: Context, pMap: Map<String, String>) {

    getNotificationIntent(pMap)

    val notificationId = (System.currentTimeMillis() % 1000).toInt()

    val defaultSoundUri: Uri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
    val body = pMap["body"]
    val title = pMap["title"]
    val button = pMap["buttonLabel"]

    //To get image URI from the data if available
    val imageUri = pMap["imageUrl"]
    val thumbnailImageUri = pMap["thumbnailImage"]

    val mNotificationManager: NotificationManager =
        activity.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
        /* Create or update. */
        val channel = NotificationChannel(
            CHANNEL_ID,
            CHANNEL_NAME,
            NotificationManager.IMPORTANCE_DEFAULT
        )
        mNotificationManager.createNotificationChannel(channel)
    }
    val requestCode = 1000 + (System.currentTimeMillis() % 1000).toInt()
    val pendingIntent: PendingIntent = PendingIntent.getActivity(
        activity, requestCode, intent,
        PendingIntent.FLAG_ONE_SHOT
    )
    val mBuilder: NotificationCompat.Builder = NotificationCompat.Builder(activity, CHANNEL_ID)

    if (!imageUri.isNullOrEmpty()) {
        mBuilder.setSmallIcon(R.mipmap.ic_launcher)
            .setContentText(body)
            .setLargeIcon(getBitmap(thumbnailImageUri))
            .setStyle(NotificationCompat.BigPictureStyle().bigPicture(getBitmap(imageUri)).bigLargeIcon(null))
            .setAutoCancel(true)
            .setSound(defaultSoundUri)
            .setTicker(body)
            .setLights(Color.GREEN, 500, 200)
            .setContentIntent(pendingIntent)

        if(!title.isNullOrEmpty()){
            mBuilder.setContentTitle(title)
        }
        if(!button.isNullOrEmpty()){
            intent?.putExtra(NOTIFICATION_ID, notificationId)
            val btnIntent = PendingIntent.getActivity(activity, 0, intent,
                PendingIntent.FLAG_CANCEL_CURRENT
            )
            mBuilder.addAction(R.mipmap.ic_launcher, button, btnIntent)
        }
        mNotificationManager.notify(notificationId, mBuilder.build())

    } else {
        mBuilder
            .setSmallIcon(R.mipmap.ic_launcher)
            .setContentText(body)
            .setStyle(NotificationCompat.BigTextStyle().bigText(body))
            .setAutoCancel(true)
            .setSound(defaultSoundUri)
            .setTicker(body)
            .setLights(Color.GREEN, 500, 200)
            .setContentIntent(pendingIntent)

        if(!title.isNullOrEmpty()){
            mBuilder.setContentTitle(title)
        }
        if(!button.isNullOrEmpty()) {
            intent?.putExtra(NOTIFICATION_ID, notificationId)
            val btnIntent = PendingIntent.getActivity(activity, 0, intent,
                PendingIntent.FLAG_CANCEL_CURRENT
            )
            mBuilder.addAction(R.mipmap.ic_launcher, button, btnIntent)
        }
        mNotificationManager.notify(notificationId, mBuilder.build())
    }

}

fun getBitmap(uri: String?): Bitmap{
    var bitmap: Bitmap? = null
    if (uri != null) {
        try {
            bitmap =  BitmapFactory.decodeStream(URL(uri).content as InputStream)
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }
    return bitmap!!
}

/* Saving data in Room Database*/
fun saveInAppNotification(mContext: Context, pMap: Map<String, String>){

    val expiryTime = if (pMap.containsKey("expiryTime")) pMap["expiryTime"]!!.toLong() else 0

    val notificationInAppEntity = NotificationInAppEntity(
        0,
        if (pMap.containsKey("inAppId")) pMap["inAppId"]!!.toLong() else 0L,
        if (pMap.containsKey("campaignId")) pMap["campaignId"]!!.toLong() else 0L,
        if (pMap.containsKey("campaignName")) pMap["campaignName"].toString() else "",
        if (pMap.containsKey("title")) pMap["title"].toString() else "",
        if (pMap.containsKey("body")) pMap["body"].toString() else "",
        if (pMap.containsKey("imageUrl")) pMap["imageUrl"].toString() else "",
        if (pMap.containsKey("type")) pMap["type"].toString() else "",
        if (pMap.containsKey("buttonLabel")) pMap["buttonLabel"].toString() else "",
        if (pMap.containsKey("deepLinkUrl")) pMap["deepLinkUrl"].toString() else "",
        expiryTime + System.currentTimeMillis(),
        if (pMap.containsKey("templateType")) pMap["templateType"].toString() else "",
        if (pMap.containsKey("location")) pMap["location"].toString() else "",
        if(pMap.containsValue("priority")) pMap["priority"].toBoolean() else false,
        if (pMap.containsKey("delay")) pMap["delay"] else "",
        if(pMap.containsValue("isInAppNotification")) pMap["isInAppNotification"].toBoolean() else false,
        false,
        System.currentTimeMillis()
    )

    Executors.newSingleThreadExecutor().execute {
        InAppRoomDatabase.getInstance(mContext).notificationInAppDao().insert(notificationInAppEntity)
    }
}

/* Fetch data from database and show accordingly i.e.
* If Centre, then show centre
* else Mini
* */
fun showInAppNotification(mContext: FragmentActivity){
    var mdata: NotificationInAppEntity?

    Executors.newSingleThreadExecutor().execute {
        mdata = InAppRoomDatabase.getInstance(mContext).notificationInAppDao().getNotification(0,
            InAppLocationType.HOMEPAGE.name,  System.currentTimeMillis())

        Log.d("RetailIoInApp data", "$mdata")

        if(mdata != null && InAppSessionService.IN_APP_NOTIF_VIEW_COUNT_SESSION <= 2) {
            mContext.runOnUiThread {
                Log.d("RetailIoInApp 1", "$mdata")
                if (mdata?.type == "CENTRE") {
                    mContext.startActivity(InAppCentreActivity.getLaunchIntent(mContext, mdata!!))
                } else if (mdata?.type == "MINI") {
                    val inApp = InAppMiniFragment.getInstance(mdata!!)
                    val transaction = mContext.supportFragmentManager.beginTransaction()
                    transaction.add(android.R.id.content, inApp)
                    transaction.commitAllowingStateLoss()
                }
            }
        }
    }
}