package gui.home;

import android.os.Bundle;
import android.view.View;

import gui.BaseActivity;
import gui.static_dialogs.MessageDialog;
import in.softc.app.R;
import utils.Font;

public class HomeActivity extends BaseActivity {

    @Override
    public int getLayoutResId() {
        return R.layout.activity_home;
    }


    @Override
    public void onInitialize(Bundle bundle) {
        Font.setFont(Font.LatoMedium, this, R.id.txt_backup, R.id.txt_restore, R.id.txt_view, R.id.txt_search);
        loadBannerAd();
    }


    @Override
    public void onClosed() {
        exitActivityOnDoublePress();
    }


    public void openNavigationDrawer(View view) {
        new MessageDialog(this)
                .setTitle(getString(R.string.welcome))
                .setMessage(getString(R.string.premium_upgrade_msg))
                .setButtonName(getString(R.string.upgrade_premium), getString(R.string.skip))
                .show();
    }


    public void onClickedBackup(View view) {
        BackupTypeChooserDialog typeChooserDialog = new BackupTypeChooserDialog(this);
        typeChooserDialog.getDialog().show();
    }


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
