package com.im.sdk.dy.storage;

/**
 * com.im.sdk.dy.storage in IMSDK_Android
 * Created by yung on 2015/7/15.
 */
public class MeetingSqlManager extends AbstractSQLManager {

    private static MeetingSqlManager sInstance = new MeetingSqlManager();

    private static MeetingSqlManager getInstance() {
        return sInstance;
    }


}
