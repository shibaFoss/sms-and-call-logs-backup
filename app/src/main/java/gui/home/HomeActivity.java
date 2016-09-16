package gui.home;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

import gui.BaseActivity;
import gui.static_dialogs.MessageDialog;
import in.softc.app.R;
import utils.Timer;

public class HomeActivity extends BaseActivity {

    /**
     * The call back function is used to get the layout res Id of
     * the activity.
     * @return the res Id of the activity's layout xml file.
     */
    @Override
    public int getLayoutResId() {
        return R.layout.activity_home;
    }

    /**
     * The function get called on {@link Activity#onPostCreate(Bundle)}
     * function of the activity.
     * @param bundle the bundle reference that the activity uses to store data in the
     *               activity's whole life cycle.
     */
    @Override
    public void onInitialize(Bundle bundle) {
        loadBannerAd();
    }

    /**
     * The function get called when the activity's {@link Activity#onBackPressed()}
     * function get executed.
     */
    @Override
    public void onClosed() {
        exitActivityOnDoublePress();
    }



    /**
     * The function opens the navigation drawer,
     * Note : the function get called automatically when the user click the navigation button.
     */
    public void openNavigationDrawer(View view) {
        new MessageDialog(this)
                .setTitle(getString(R.string.welcome))
                .setMessage(getString(R.string.premium_upgrade_msg))
                .setButtonName(getString(R.string.upgrade_premium), getString(R.string.skip))
                .show();
    }

    /**
     * Note : the function get called automatically when the user click the backup button.
     */
    public void onClickedBackup(View view) {
        BackupTypeChooserDialog typeChooserDialog = new BackupTypeChooserDialog(this);
        typeChooserDialog.getDialog().show();
    }

    /**
     * Note : the function get called automatically when the user click the restore button.
     */
    public void onClickedRestore(View view) {
        vibrate(20);
        toast(getString(R.string.restore));
    }

    public void onClickedView(View view) {
        vibrate(20);
        toast(getString(R.string.view));
    }

    public void onClickedSearch(View view) {
        vibrate(20);
        toast(getString(R.string.search));
    }
}
