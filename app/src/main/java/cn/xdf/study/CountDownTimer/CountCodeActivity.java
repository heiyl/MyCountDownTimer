package cn.xdf.study.CountDownTimer;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.widget.TextView;

import cn.xdf.study.R;

public class CountCodeActivity extends AppCompatActivity implements View.OnClickListener{

    private TextView mTextView;

    private CustomCountDownTimerUtils countDownTimerUtils;

    //倒计时总时间
    private final int SPLASH_DISPLAY_LENGHT = 60000;
    //循环时间间隔
    private final int SPLASH_INTERVAL_LENGHT = 100;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_count_code);
        mTextView = this.findViewById(R.id.tv_getcode);
        countDownTimerUtils = CustomCountDownTimerUtils.getCountDownTimer();
        mTextView.setOnClickListener(this);

        countDownTimerUtils
                .setMillisInFuture(SPLASH_DISPLAY_LENGHT)
                .setCountDownInterval(SPLASH_INTERVAL_LENGHT);


    }

    @Override
    public void onClick(View view) {
        countDownTimerUtils.setTickDelegate(new CustomCountDownTimerUtils.TickDelegate() {
            @Override
            public void onTick(long pMillisUntilFinished) {
                mTextView.setClickable(false); //设置不可点击
                mTextView.setText(pMillisUntilFinished / 1000 + "秒后可重新发送");  //设置倒计时时间
                mTextView.setBackgroundResource(R.drawable.btn_get_code_press); //设置按钮为灰色，这时是不能点击的

                SpannableString spannableString = new SpannableString(mTextView.getText().toString());  //获取按钮上的文字
                ForegroundColorSpan span = new ForegroundColorSpan(Color.RED);
                spannableString.setSpan(span, 0, 2, Spannable.SPAN_INCLUSIVE_EXCLUSIVE);//将倒计时的时间设置为红色
                mTextView.setText(spannableString);
            }
        })
                .setFinishDelegate(new CustomCountDownTimerUtils.FinishDelegate() {
                    @Override
                    public void onFinish() {
                        mTextView.setText("重新获取验证码");
                        mTextView.setClickable(true);//重新获得点击
                        mTextView.setBackgroundResource(R.drawable.btn_get_code_normal);  //还原背景色

                    }
                }).start();
    }
}
