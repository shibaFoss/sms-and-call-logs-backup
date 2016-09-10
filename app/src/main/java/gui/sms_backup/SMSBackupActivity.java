package gui.sms_backup;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;

import gui.BaseActivity;
import in.softc.app.R;

public class SMSBackupActivity extends BaseActivity {

    /**
     * The call back function is used to get the layout res Id of
     * the activity.
     * @return the res Id of the activity's layout xml file.
     */
    @Override
    public int getLayoutResId() {
        return R.layout.activity_sms_backup_activity;
    }

    /**
     * The function get called on {@link Activity#onPostCreate(Bundle)}
     * function of the activity.
     * @param bundle the bundle reference that the activity uses to store data in the
     *               activity's whole life cycle.
     */
    @Override
    public void onInitialize(Bundle bundle) {

    }

    /**
     * The function get called when the activity's {@link Activity#onBackPressed()}
     * function get executed.
     */
    @Override
    public void onClosed() {
        finish();
    }

    public void onBackClicked(View view) {
        onClosed();
    }

    public void onClickedBackup(View view) {
        SmsManager smsManager = new SmsManager(this);
        smsManager.getAllSms(null);
    }
}
