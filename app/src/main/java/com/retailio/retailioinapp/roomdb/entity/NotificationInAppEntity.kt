package com.retailio.retailioinapp.roomdb.entity

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.retailio.retailioinapp.roomdb.DBContract
import kotlinx.parcelize.Parcelize

@Entity(tableName = DBContract.RetailioInAppNotificationTable.TABLE_NAME)
@Parcelize
data class NotificationInAppEntity(
    @PrimaryKey(autoGenerate = true) val _id:Long,
    @ColumnInfo(name = DBContract.RetailioInAppNotificationTable.COLUMN_IN_APP_ID) var inAppId: Long,
    @ColumnInfo(name = DBContract.RetailioInAppNotificationTable.COLUMN_CAMPAIGN_ID) var campaignId: Long,
    @ColumnInfo(name = DBContract.RetailioInAppNotificationTable.COLUMN_CAMPAIGN_NAME) var campaignName: String,
    @ColumnInfo(name = DBContract.RetailioInAppNotificationTable.COLUMN_HEADER) var header: String?="",
    @ColumnInfo(name = DBContract.RetailioInAppNotificationTable.COLUMN_BODY) var body: String?="",
    @ColumnInfo(name = DBContract.RetailioInAppNotificationTable.COLUMN_IMAGE_URL) var imageUrl: String?="",
    @ColumnInfo(name = DBContract.RetailioInAppNotificationTable.COLUMN_TYPE) var type: String?="",
    @ColumnInfo(name = DBContract.RetailioInAppNotificationTable.COLUMN_BUTTON_LABEL) var buttonLabel: String?="",
    @ColumnInfo(name = DBContract.RetailioInAppNotificationTable.COLUMN_DEEPLINK) var deeplink: String?="",
    @ColumnInfo(name = DBContract.RetailioInAppNotificationTable.COLUMN_EXPIRY_TIME) var expiryTime: Long? = 0,
    @ColumnInfo(name = DBContract.RetailioInAppNotificationTable.COLUMN_TEMPLATE_TYPE) var templateType: String? = "",
    @ColumnInfo(name = DBContract.RetailioInAppNotificationTable.COLUMN_LOCATION) var location: String? = "",
    @ColumnInfo(name = DBContract.RetailioInAppNotificationTable.COLUMN_PRIORITY) var priority: Boolean = false,
    @ColumnInfo(name = DBContract.RetailioInAppNotificationTable.COLUMN_DELAY) var delay: String? = "",
    @ColumnInfo(name = DBContract.RetailioInAppNotificationTable.COLUMN_IS_IN_APP_NOTIFICATION) var isInAppNotification: Boolean = true,
    @ColumnInfo(name = DBContract.RetailioInAppNotificationTable.COLUMN_DISPLAY_STATUS) var displayStatus: Boolean = false,
    @ColumnInfo(name = DBContract.RetailioInAppNotificationTable.COLUMN_CREATED_AT) var createdAt: Long = 0,

    ): Parcelable{
}
