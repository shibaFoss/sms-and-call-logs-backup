package libs.localization;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;

import in.softc.app.R;

public class BlankDummyActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_blank_dummy);

        delayedFinish();
    }


    @Override
    public void finish() {
        super.finish();
    }


    private void delayedFinish() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                finish();
            }
        }, 200);
    }
}
