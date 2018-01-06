package cn.xdf.study;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Window;
import android.widget.TextView;

import cn.xdf.study.CountDownTimer.CustomCountDownTimerUtils;


/**
 * 启动页
 * 2017/12/29.
 * yulong
 */
public class SplashActivity extends AppCompatActivity {

    //倒计时总时间
    private final int SPLASH_DISPLAY_LENGHT = 3000;
    //循环时间间隔
    private final int SPLASH_INTERVAL_LENGHT = 100;

    private CustomCountDownTimerUtils countDownTimerUtils;

    private TextView tv_time;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_splash);

        tv_time = this.findViewById(R.id.tv_time);

        countDownTimerUtils = CustomCountDownTimerUtils.getCountDownTimer();

        countDownTimerUtils
                .setMillisInFuture(SPLASH_DISPLAY_LENGHT)
                .setCountDownInterval(SPLASH_INTERVAL_LENGHT)
                .setTickDelegate(new CustomCountDownTimerUtils.TickDelegate() {//这个页面正常情况下不需要setTickDelegate方法
                    @Override
                    public void onTick(long pMillisUntilFinished) {
                        tv_time.setText("该页面" + (pMillisUntilFinished) + "毫秒后自动关闭");
                        Log.v("CountDownTimerTest", "pMillisUntilFinished = " + pMillisUntilFinished);
                    }
                })
                .setFinishDelegate(new CustomCountDownTimerUtils.FinishDelegate() {
                    @Override
                    public void onFinish() {
                        Intent intent = new Intent(SplashActivity.this, MainActivity.class);
                        startActivity(intent);
                        SplashActivity.this.finish();

                    }
                }).start();

    }

    /**
     * 禁止返回事件
     *
     * @param keyCode
     * @param event
     * @return
     */
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}
