package com.yidou.wandou.example_11.db;

import android.content.Context;

import com.yidou.wandou.example_11.dao.DaoMaster;
import com.yidou.wandou.example_11.dao.DaoSession;


/**
 * Created by Administrator on 2016/11/14.
 */

public class DbCore
{
    private static final String DB_NAME = "shares.db";
    private static DaoMaster daoMaster;
    private static DaoSession daoSession;
    private static Context mContext;
    private static String DB;

    public static void init(Context context)
    {
        init(context, DB_NAME);
    }

    public static void init(Context context, String name)
    {
        if (context == null)
        {
            throw new IllegalArgumentException("context can't be null");
        }
        mContext = context.getApplicationContext();
        DB = DB_NAME;
    }

    public static DaoMaster getDaoMaster()
    {
        DaoMaster.OpenHelper helper = new MyOpeanHelper(mContext,DB);
        daoMaster = new DaoMaster(helper.getReadableDb());
        return daoMaster;
    }
    public static DaoSession getDaoSession()
    {
        if (daoSession == null)
        {
            if (daoMaster == null)
            {
                daoMaster = getDaoMaster();
            }
            daoSession = daoMaster.newSession();
        }
        return daoSession;
    }

}
