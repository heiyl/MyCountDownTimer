package cn.xdf.study;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import cn.xdf.study.CountDownTimer.CustomCountDownTimerUtils;

public class CountPauseActivity extends AppCompatActivity implements View.OnClickListener {

    private ProgressBar progress_bar;
    private TextView tv_time;
    private ImageView iv_media;

    //倒计时总时间
    private final int SPLASH_DISPLAY_LENGHT = 10000;
    //循环时间间隔
    private final int SPLASH_INTERVAL_LENGHT = 100;


    private final int STATE_INIT = 0;//初始化状态
    private final int STATE_PLAY = 1;//开始计时状态
    private final int STATE_PAUSE = 2;//暂停计时状态

    private int state = STATE_INIT;

    private CustomCountDownTimerUtils countDownTimerUtils;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_count_pause);
        progress_bar = this.findViewById(R.id.progress_bar);
        tv_time = this.findViewById(R.id.tv_time);
        iv_media = this.findViewById(R.id.iv_media);
        iv_media.setOnClickListener(this);

        progress_bar.setMax(SPLASH_DISPLAY_LENGHT);
        progress_bar.setProgress(0);
        tv_time.setText(0 + "/10秒");

    }

    @Override
    public void onClick(View view) {
        //开始倒计时
        if (countDownTimerUtils == null) {
            countDownTimerUtils = CustomCountDownTimerUtils.getCountDownTimer();
        }
        if (state == STATE_INIT) {
            iv_media.setImageResource(R.mipmap.stop);
            state = STATE_PLAY;
            countDownTimerUtils
                    .setMillisInFuture(SPLASH_DISPLAY_LENGHT)
                    .setCountDownInterval(SPLASH_INTERVAL_LENGHT)
                    .setTickDelegate(new CustomCountDownTimerUtils.TickDelegate() {
                        @Override
                        public void onTick(long pMillisUntilFinished) {
                            tick(pMillisUntilFinished);
                        }
                    })
                    .setFinishDelegate(new CustomCountDownTimerUtils.FinishDelegate() {
                        @Override
                        public void onFinish() {
                            complete();
                        }
                    }).start();
        } else if (state == STATE_PLAY) {
            if (countDownTimerUtils != null) {
                countDownTimerUtils.pause();
                iv_media.setImageResource(R.mipmap.play);
                state = STATE_PAUSE;
            }
        } else if (state == STATE_PAUSE) {//resume
            if (countDownTimerUtils != null) {
                state = STATE_PLAY;
                countDownTimerUtils.resume();
                iv_media.setImageResource(R.mipmap.stop);
            }
        }


    }

    /**
     * 倒计时结束
     */
    private void complete() {
        Log.v("CountDownTimerTest", "onFinish");
        if (countDownTimerUtils != null) {
            countDownTimerUtils.cancel();
            countDownTimerUtils = null;
        }

        tv_time.setText(0 + "/10秒");
        iv_media.setImageResource(R.mipmap.play);
        progress_bar.setProgress(0);
        state = STATE_INIT;
    }

    /**
     * 倒计时中...
     * @param pMillisUntilFinished
     */
    private void tick(long pMillisUntilFinished) {
        int haveTime = (int) ((pMillisUntilFinished / 1000) + 0.5);
        if (haveTime > 10) {
            haveTime = 10;
        }
        int passTime = 10 - haveTime;
        progress_bar.setProgress(passTime * 1000);
        tv_time.setText(passTime + "/10秒");
    }
}
