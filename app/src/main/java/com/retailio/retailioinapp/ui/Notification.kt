package com.retailio.retailioinapp.ui

import android.annotation.SuppressLint
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Color
import android.media.RingtoneManager
import android.net.Uri
import android.os.Build
import androidx.core.app.NotificationCompat
import com.retailio.retailioinapp.R
import java.io.IOException
import java.io.InputStream
import java.net.URL

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

@SuppressLint("UnspecifiedImmutableFlag")
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
    val pendingIntent: PendingIntent? = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
        PendingIntent.getActivity(
            activity, requestCode, intent,
            PendingIntent.FLAG_MUTABLE
        )
    }else {
        PendingIntent.getActivity(
            activity, requestCode, intent,
            PendingIntent.FLAG_ONE_SHOT
        )
    }
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