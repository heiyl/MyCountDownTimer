package cn.xdf.study.CountDownTimer;


/**
 * 倒计时工具类
 */
public class CustomCountDownTimerUtils {
    /**
     * 倒计时结束的回调接口
     */
    public interface FinishDelegate {
        void onFinish();
    }

    /**
     * 定期回调的接口
     */
    public interface TickDelegate {
        void onTick(long pMillisUntilFinished);
    }

    private final static long ONE_SECOND = 1000;
    /**
     * 总倒计时时间
     */
    private long mMillisInFuture = 0;
    /**
     * 定期回调的时间 必须大于0 否则会出现ANR
     */
    private long mCountDownInterval;
    /**
     * 倒计时结束的回调
     */
    private FinishDelegate mFinishDelegate;
    /**
     * 定期回调
     */
    private TickDelegate mTickDelegate;
    private MyCountDownTimer mCountDownTimer;

    /**
     * 获取 CountDownTimerUtils
     *
     * @return CountDownTimerUtils
     */
    public static CustomCountDownTimerUtils getCountDownTimer() {
        return new CustomCountDownTimerUtils();
    }

    /**
     * 设置定期回调的时间 调用{@link #setTickDelegate(TickDelegate)}
     *
     * @param pCountDownInterval 定期回调的时间 必须大于0
     * @return CountDownTimerUtils
     */
    public CustomCountDownTimerUtils setCountDownInterval(long pCountDownInterval) {
        this.mCountDownInterval = pCountDownInterval;
        return this;
    }

    /**
     * 设置倒计时结束的回调
     *
     * @param pFinishDelegate 倒计时结束的回调接口
     * @return CountDownTimerUtils
     */
    public CustomCountDownTimerUtils setFinishDelegate(FinishDelegate pFinishDelegate) {
        this.mFinishDelegate = pFinishDelegate;
        return this;
    }

    /**
     * 设置总倒计时时间
     *
     * @param pMillisInFuture 总倒计时时间
     * @return CountDownTimerUtils
     */
    public CustomCountDownTimerUtils setMillisInFuture(long pMillisInFuture) {
        this.mMillisInFuture = pMillisInFuture;
        return this;
    }

    /**
     * 设置定期回调
     *
     * @param pTickDelegate 定期回调接口
     * @return CountDownTimerUtils
     */
    public CustomCountDownTimerUtils setTickDelegate(TickDelegate pTickDelegate) {
        this.mTickDelegate = pTickDelegate;
        return this;
    }

    public void create() {
        if (mCountDownTimer != null) {
            mCountDownTimer.cancel();
            mCountDownTimer = null;
        }
        if (mCountDownInterval <= 0) {
            mCountDownInterval = mMillisInFuture + ONE_SECOND;
        }
        mCountDownTimer = new MyCountDownTimer(mMillisInFuture, mCountDownInterval);
        mCountDownTimer.setTickDelegate(mTickDelegate);
        mCountDownTimer.setFinishDelegate(mFinishDelegate);
    }

    /**
     * 开始倒计时
     */
    public void start() {
        if (mCountDownTimer == null) {
            create();
        }
        mCountDownTimer.start();
    }

    public void pause(){
        if (mCountDownTimer != null) {
            mCountDownTimer.pause();
        }
    }

    public void resume(){
        if (mCountDownTimer != null) {
            mCountDownTimer.resume();
        }
    }

    /**
     * 取消倒计时
     */
    public void cancel() {
        if (mCountDownTimer != null) {
            mCountDownTimer.cancel();
        }
    }

    private static class MyCountDownTimer extends CustomCountDownTimer {
        private FinishDelegate mFinishDelegate;
        private TickDelegate mTickDelegate;

        /**
         * @param millisInFuture    The number of millis in the future from the call
         *                          to {@link #start()} until the countdown is done and {@link #onFinish()}
         *                          is called.
         * @param countDownInterval The interval along the way to receive
         *                          {@link #onTick(long)} callbacks.
         */
        public MyCountDownTimer(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);
        }

        @Override
        public void onTick(long millisUntilFinished) {
            if (mTickDelegate != null) {
                mTickDelegate.onTick(millisUntilFinished);
            }
        }

        @Override
        public void onFinish() {
            if (mFinishDelegate != null) {
                mFinishDelegate.onFinish();
            }
        }

        void setFinishDelegate(FinishDelegate pFinishDelegate) {
            this.mFinishDelegate = pFinishDelegate;
        }

        void setTickDelegate(TickDelegate pTickDelegate) {
            this.mTickDelegate = pTickDelegate;
        }
    }
}
