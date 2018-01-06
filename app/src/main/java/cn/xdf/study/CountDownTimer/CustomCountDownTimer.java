package cn.xdf.study.CountDownTimer;

import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
/**
 * Schedule a countdown until a time in the future, with
 * regular notifications on intervals along the way.
 * 官方文档中的使用例子：
 * Example of showing a 30 second countdown in a text field:
 * <p>
 * <pre class="prettyprint">
 * new CountDownTimer(30000, 1000) {
 * <p>
 * public void onTick(long millisUntilFinished) {
 * mTextField.setText("seconds remaining: " + millisUntilFinished / 1000);
 * }
 * <p>
 * public void onFinish() {
 * mTextField.setText("done!");
 * }
 * }.start();
 * </pre>
 * <p>
 * The calls to {@link #onTick(long)} are synchronized to this object so that
 * one call to {@link #onTick(long)} won't ever occur before the previous
 * callback is complete.  This is only relevant when the implementation of
 * {@link #onTick(long)} takes an amount of time to execute that is significant
 * compared to the countdown interval.
 */

/**
 * customize from CountDownTimer
 * Created by zhubingning on 16/09/16.
 */
public abstract class CustomCountDownTimer {


    /**
     * Millis since epoch when alarm should stop.
     */
    private final long mMillisInFuture;

    //!add,为了暂停时保存当前还剩下的毫秒数
    private long mCurrentMillisLeft;

    /**
     * The interval in millis that the user receives callbacks
     */
    private final long mCountdownInterval;

    private long mStopTimeInFuture;

    /**
     * boolean representing if the timer was cancelled
     */
    private boolean mCancelled = false;

    /**
     * @param millisInFuture    The number of millis in the future from the call
     *                          to {@link #start()} until the countdown is done and {@link #onFinish()}
     *                          is called.
     * @param countDownInterval The interval along the way to receive
     *                          {@link #onTick(long)} callbacks.
     */
    //构造函数，（总倒计时毫秒为单位，倒计时间隔）
    public CustomCountDownTimer(long millisInFuture, long countDownInterval) {
        mMillisInFuture = millisInFuture;
        mCountdownInterval = countDownInterval;
    }

    //!add, 获取此时倒计时的总时间
    public long getCountTimes() {
        return mMillisInFuture;
    }

    /**
     * Cancel the countdown.
     */
    //取消倒计时，handler从消息队列里取出message
    public synchronized final void cancel() {
        mCancelled = true;
        mHandler.removeMessages(MSG);
    }

    /**
     * Pause the countdown.
     */
    //！add, 暂停，调用cancel（）函数， mCurrentMillisLeft为全局变量自动保存
    public synchronized final void pause() {
        cancel();
    }

    /**
     * Resume the countdown.
     */
    //！add, 恢复函数，根据mCurrentMillisLeft的值重新添加message开始倒计时
    public synchronized final void resume() {
        mCancelled = false;
        if (mCurrentMillisLeft <= 0) {
            onFinish();
            return;
        }
        mStopTimeInFuture = SystemClock.elapsedRealtime() + mCurrentMillisLeft;
        mHandler.sendMessage(mHandler.obtainMessage(MSG));
        return;
    }

    /**
     * Start the countdown.
     */
    //开始倒计时，handler发送消息到队列
    public synchronized final CustomCountDownTimer start() {
        mCancelled = false;
        if (mMillisInFuture <= 0) {
            onFinish();
            return this;
        }
        mStopTimeInFuture = SystemClock.elapsedRealtime() + mMillisInFuture;
        mHandler.sendMessage(mHandler.obtainMessage(MSG));
        return this;
    }


    /**
     * Callback fired on regular interval.
     *
     * @param millisUntilFinished The amount of time until finished.
     */
    //虚拟函数
    public abstract void onTick(long millisUntilFinished);

    /**
     * Callback fired when the time is up.
     */
    //虚拟函数
    public abstract void onFinish();


    private static final int MSG = 1;


    // handles counting down
    //handler
    private Handler mHandler = new Handler() {

        @Override
        public void handleMessage(Message msg) {

            //同步线程
            synchronized (CustomCountDownTimer.this) {
                //判断倒计时是否已取消
                if (mCancelled) {
                    return;
                }

                //计算当前剩余毫秒数
                final long millisLeft = mStopTimeInFuture - SystemClock.elapsedRealtime();


                //根据剩余毫秒数，或者结束倒计时，或者只延时，或者调用onTick并延时
                if (millisLeft <= 0) {
                    onFinish();
                } else if (millisLeft < mCountdownInterval) {
                    // no tick, just delay until done
                    onTick(0);//！add
                    sendMessageDelayed(obtainMessage(MSG), millisLeft);
                } else {
                    long lastTickStart = SystemClock.elapsedRealtime();
                    mCurrentMillisLeft = millisLeft;//！add
                    onTick(millisLeft);

                    // take into account user's onTick taking time to execute
                    long delay = lastTickStart + mCountdownInterval - SystemClock.elapsedRealtime();

                    // special case: user's onTick took more than interval to
                    // complete, skip to next interval
                    while (delay < 0) delay += mCountdownInterval;

                    sendMessageDelayed(obtainMessage(MSG), delay);
                }
            }
        }
    };
}
