package com.yidou.wandou.example_11.app;

import android.app.Application;

import com.yidou.wandou.example_11.db.DbCore;

/**
 * Created by Administrator on 2016/11/14.
 */

public class App extends Application
{
    @Override
    public void onCreate()
    {
        super.onCreate();
        DbCore.init(this);
    }
}
