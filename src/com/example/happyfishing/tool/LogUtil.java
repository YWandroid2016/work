package com.example.happyfishing.tool;

import android.util.Log;

public class LogUtil {
	private static boolean flag = true;

	public static final int VERBOSE = 1;

    public static final int DEBUG = 2;

    public static final int INFO = 3;

    public static final int WARN = 4;

    public static final int ERROR = 5;

    public static final int NOTHING = 6;

    public static final int LEVEL = VERBOSE;

    public static void v(String tag, String msg){
        if(LEVEL <= VERBOSE&&flag==true){
            Log.v(tag, msg);
        }
    }

    public static void d(String tag, String msg){
        if(LEVEL <= DEBUG&&flag==true){
            Log.d(tag, msg);
        }
    }

    public static void i(String tag, String msg){
        if(LEVEL <= INFO&&flag==true){
            Log.i(tag, msg);
        }
    }

    public static void w(String tag, String msg){
        if(LEVEL <= WARN&&flag==true){
            Log.w(tag, msg);
        }
    }

    public static void e(String tag, String msg){
        if(LEVEL <= ERROR&&flag==true){
            Log.e(tag, msg);
        }
    }

}
