package gui.launch;

import android.os.Bundle;
import android.os.Handler;

import gui.BaseActivity;
import gui.home.HomeActivity;
import in.softc.app.R;

public class LaunchActivity extends BaseActivity {

    @Override
    public int getLayoutResId() {
        return R.layout.activity_launcher;
    }

    @Override
    public void onInitialize(Bundle bundle) {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                startActivity(HomeActivity.class);
                finish();
            }
        }, 1200);
    }

    @Override
    public void onClosed() {
        finish();
    }

}
