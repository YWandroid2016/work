package com.example.happyfishing.tool;

import android.app.Activity;

import java.util.ArrayList;
import java.util.List;

public class ActivityCollector {

    public static List<Activity> activityList = new ArrayList<Activity>();

    public static Activity loginActivity;

//    收银台输入商品activity
    public static Activity myActivity;

//    结算activity
    public static Activity sumAmountActivity;

    public static void addActivity(Activity activity){
        activityList.add(activity);
    }

    public static void removeActivity(Activity activity){
        activityList.remove(activity);
    }

    public static void finishAll(){
        for(Activity activity : activityList){
            if(!activity.isFinishing()){
                activity.finish();
            }
        }
    }
}
