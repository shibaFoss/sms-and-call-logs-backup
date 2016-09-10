package utils;

import android.os.CountDownTimer;

/**
 * A Handy Timer class that can replace the traditional CountDownTimer class.
 *
 * @author Shiba
 * @version 1.1
 */
@SuppressWarnings("unused")
public abstract class Timer extends CountDownTimer {


    public Timer(long total, long interval) {
        super(total, interval);
    }


    @Override
    public void onTick(long tickTimer) {

    }


    @Override
    public void onFinish() {

    }


}
