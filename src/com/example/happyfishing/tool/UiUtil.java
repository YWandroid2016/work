package com.example.happyfishing.tool;

import android.os.Build;
import android.text.InputType;
import android.widget.EditText;

public class UiUtil {
	
	private static boolean newMessage ;
	
    public static void disableSoftInputFromAppearing(EditText editText){
        if(Build.VERSION.SDK_INT >=11){
            editText.setRawInputType(InputType.TYPE_CLASS_TEXT);
            editText.setTextIsSelectable(true);
        }else{
            editText.setRawInputType(InputType.TYPE_NULL);
            editText.setFocusable(false);
        }
    }
    
    public static void setNewMessage(boolean hasNewMessage) {
		newMessage = hasNewMessage;
	}
    
    public static boolean getNewMessage() {
		return newMessage;
	}
    
}
