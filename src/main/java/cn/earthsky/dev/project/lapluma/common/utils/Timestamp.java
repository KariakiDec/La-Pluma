package cn.earthsky.dev.project.lapluma.common.utils;

import java.util.Calendar;

public class Timestamp {
    private static long baseStamp;
    static {
        Calendar date = Calendar.getInstance();
        date.set(Calendar.HOUR,0);
        date.set(Calendar.MINUTE,0);
        date.set(Calendar.SECOND,0);
        date.set(Calendar.MILLISECOND,0);
        baseStamp = date.getTimeInMillis();
    }

    public static long getRelativeTimestamp(){
        return System.currentTimeMillis()-baseStamp;
    }
}
