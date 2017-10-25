package com.tsm.way.utils;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public final class CommonUtils {

    public static final int PLAN_TYPE_PRIVATE_EVENT = 8801;
    public static final int PLAN_TYPE_PUBLIC_EVENT = 8802;
    public static final int PLAN_TYPE_FB_EVENT = 8803;
    public static final int GUEST_STATUS_CONFIRMED = 9901;
    public static final int GUEST_STATUS_PENDING = 9902;
    public static final int GUEST_STATUS_DECLINED = 9903;

    public static String getFormattedTimeFromTimestamp(long timeStamp) {
        Date date = new java.util.Date(timeStamp);
        String formattedTime = new SimpleDateFormat("MMM dd, hh:mm a", Locale.US).format(date);
        return formattedTime;
    }
}