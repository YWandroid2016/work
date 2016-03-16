package com.example.happyfishing.view;

import java.util.Timer;

import java.util.TimerTask;


import android.content.Context;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class TimeButton extends Button implements OnClickListener {

	private long length = 60*1000;
	private String textAfter = "秒后重新获取";
	private String textBefore = "点击获取验证码";
	private OnClickListener mclickListener;
	private Timer timer;
	private TimerTask timerTask;
	private long time;
	@SuppressWarnings("unused")
	private Context context;
	
	public TimeButton(Context context) {
		super(context);
		setOnClickListener(this);
	}

	public TimeButton(Context context, AttributeSet attrs) {
		super(context, attrs);
		this.context = context;
		setOnClickListener(this);
	}
	
	Handler handler = new Handler(){
		public void handleMessage(android.os.Message msg) {
			TimeButton.this.setText(time/1000+textAfter);
			time -= 1000;
			if (time<0) {
				TimeButton.this.setEnabled(true);
				TimeButton.this.setText(textBefore);
				clearTimer();
			}
		}

	};

	private void initTimer() {
		time = length;
		timer = new Timer();
		timerTask = new TimerTask() {
			
			@Override
			public void run() {
				handler.sendEmptyMessage(0);
			}
		};
	}
	
	private void clearTimer() {
		if (timerTask != null) {
			timerTask.cancel();
			timerTask = null;
        }
        if (timer != null)
        	timer.cancel();
        timer = null;
	};
	
	@Override
	public void setOnClickListener(OnClickListener l) {
		if (l instanceof TimeButton) {
			
			super.setOnClickListener(l);
		}else {
			this.mclickListener = l;
		}
	}
	
	@Override
	public void onClick(View v) {
		if (mclickListener != null)
            mclickListener.onClick(v);
        initTimer();
        this.setText(time / 1000 + textAfter);
        this.setEnabled(false);
        timer.schedule(timerTask, 0, 1000);
	}
	
	
	/** * 设置计时时候显示的文本 */
    public TimeButton setTextAfter(String text1) {
        this.textAfter = text1;
        return this;
    }
    
    /** * 设置点击之前的文本 */
    public TimeButton setTextBefore(String text0) {
        this.textBefore = text0;
        this.setText(textBefore);
        return this;
    }
    
    /**
     * 设置到计时长度
     * 
     * @param lenght
     *            时间 默认毫秒
     * @return
     */
    public TimeButton setLenght(long length) {
        this.length = length;
        return this;
    }
    
}
