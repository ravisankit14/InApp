package com.retailio.retailioinapp.roomdb;

public class DBContract {

    public static class RetailioInAppNotificationTable {
        public static final String TABLE_NAME = "in_app_notifications";
        public static final String COLUMN_ID = "_id";
        public static final String COLUMN_IN_APP_ID = "in_app_id";
        public static final String COLUMN_CAMPAIGN_ID = "campaign_id";
        public static final String COLUMN_CAMPAIGN_NAME = "campaign_name";
        public static final String COLUMN_HEADER = "header";
        public static final String COLUMN_BODY = "body";
        public static final String COLUMN_IMAGE_URL = "image_url";
        public static final String COLUMN_TYPE= "type";
        public static final String COLUMN_BUTTON_LABEL = "button_label";
        public static final String COLUMN_DEEPLINK = "deeplink";
        public static final String COLUMN_EXPIRY_TIME = "expiry_time";
        public static final String COLUMN_TEMPLATE_TYPE = "template_type";
        public static final String COLUMN_LOCATION = "location";
        public static final String COLUMN_PRIORITY = "priority";
        public static final String COLUMN_DELAY = "delay";
        public static final String COLUMN_IS_IN_APP_NOTIFICATION = "is_in_app_notification";
        public static final String COLUMN_DISPLAY_STATUS = "display_status";
        public static final String COLUMN_CREATED_AT = "created_at";
    }
}