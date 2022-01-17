package com.retailio.retailioinapp.roomdb.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy.REPLACE
import androidx.room.Query
import com.retailio.retailioinapp.roomdb.DBContract
import com.retailio.retailioinapp.roomdb.entity.NotificationInAppEntity

@Dao
interface NotificationInAppEntityDao{

    @Insert(onConflict = REPLACE)
    fun insert(notificationInAppEntity: NotificationInAppEntity)

    @Query("SELECT * FROM " + DBContract.RetailioInAppNotificationTable.TABLE_NAME + " WHERE "
            + DBContract.RetailioInAppNotificationTable.COLUMN_DISPLAY_STATUS + " = :displayStatus AND "
            + DBContract.RetailioInAppNotificationTable.COLUMN_LOCATION + " = :location AND "
            + DBContract.RetailioInAppNotificationTable.COLUMN_EXPIRY_TIME + " >= :currentTime "
            + "ORDER BY " + DBContract.RetailioInAppNotificationTable.COLUMN_TEMPLATE_TYPE + " DESC, "
            + DBContract.RetailioInAppNotificationTable.COLUMN_CREATED_AT + " ASC")
    fun getNotification(displayStatus: Int, location: String, currentTime: Long): NotificationInAppEntity

    @Query("UPDATE " + DBContract.RetailioInAppNotificationTable.TABLE_NAME
            + " SET " + DBContract.RetailioInAppNotificationTable.COLUMN_DISPLAY_STATUS + " = 1"
            + " WHERE " + DBContract.RetailioInAppNotificationTable.COLUMN_ID + " = :columnId")
    fun updateNotification(columnId: Long): Int

    @Query(value = "DELETE FROM "+DBContract.RetailioInAppNotificationTable.TABLE_NAME)
    fun  deleteAll()

}
