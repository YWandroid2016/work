package com.example.happyfishing.tool;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;

public class DialogCaller {

    public static void showDialog(Context context,String title,String message){
        showDialog(context,title,message,null);
    }
    public static void showDialog(Context context,String title,String message,
                                  DialogInterface.OnClickListener onClickListener){
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle(title);
        builder.setMessage(message);
        builder.setPositiveButton("OK",onClickListener);
        builder.setNegativeButton("Cancel",null);
        builder.show();
    }
}
