package com.dt.wsjf.sp;

/**
 * Created by xunwang on 2017/8/29.
 */

public class CommSetting {
    public static final String SETTING = "setting";
    public static final String SP_KEY_SYNC_TIME = "sp_key_sync_time";//numsOfDay与vipShouldNums的同步时间，如果是同一天就不再同步

    public static long getLastSyncTime() {
        return PrefsMgr.getLong(SETTING, SP_KEY_SYNC_TIME, 0);
    }

    public static void setLastSyncTime(long syncTime) {
        PrefsMgr.putLong(SETTING, SP_KEY_SYNC_TIME, syncTime);
    }

}
