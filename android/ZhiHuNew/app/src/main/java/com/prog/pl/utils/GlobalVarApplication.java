package com.prog.pl.utils;

import android.app.Application;
import com.prog.pl.bean.User;
/**
 * Created by ads_123 on 2016/10/22.
 */

public class GlobalVarApplication extends Application{
    private static User  User=null;

    private User aUser;

    @Override
    public void onCreate()
    {
        super.onCreate();
        setaUser(User); // 初始化全局变量
    }

    public void setaUser(User value)
    {
        this.aUser = value;
    }

    public User getUser()
    {
        return aUser;
    }
}
