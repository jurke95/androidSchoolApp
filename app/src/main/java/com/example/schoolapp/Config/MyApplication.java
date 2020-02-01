package com.example.schoolapp.Config;

import android.app.Application;

public class MyApplication extends Application {

    private static String serverIpAddress = "http://192.168.1.8:5000/";

    public static String getServerIpAddress(){
        return serverIpAddress;
    }
}
