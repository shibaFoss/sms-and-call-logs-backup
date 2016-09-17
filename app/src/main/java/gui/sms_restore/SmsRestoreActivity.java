package gui.sms_restore;

import android.annotation.TargetApi;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.provider.Telephony;
import android.view.View;
import android.widget.ListView;

import java.io.File;
import java.io.FilenameFilter;
import java.util.ArrayList;

import core.ProjectDirectory;
import gui.BaseActivity;
import gui.sms_backup.SmsBrowser;
import gui.static_dialogs.MessageDialog;
import gui.static_dialogs.OnButtonClick;
import in.softc.app.R;
import utils.Font;
import utils.ViewUtility;

public class SmsRestoreActivity extends BaseActivity implements View.OnClickListener {

    private static final int DEFAULT_MESSAGE_APP_REQUEST_CODE = 4;
    private BackupFileListAdapter backupFileListAdapter;
    private boolean isSelectionButtonClicked = false;


    @Override
    public int getLayoutResId() {
        return R.layout.activity_sms_restore;
    }


    @Override
    public void onInitialize(Bundle bundle) {
        Font.setFont(Font.LatoMedium, this, R.id.txt_title, R.id.txt_restore, R.id.txt_empty_box);
        ViewUtility.setViewOnClickListener(this, this, R.id.bnt_back, R.id.txt_restore, R.id.bnt_select);

        ListView listView = (ListView) findViewById(R.id.list_sms_backup_files);
        File[] smsBackupFiles = getBackupFiles();
        if (smsBackupFiles != null) {
            ArrayList<SmsBrowser> smsBrowsers = getSmsBrowsers(smsBackupFiles);
            backupFileListAdapter = new BackupFileListAdapter(this, smsBrowsers);
            listView.setAdapter(backupFileListAdapter);
        }

        loadBannerAd();
        loadNewFullScreenAd();
    }


    @Override
    public void onClosed() {
        finish();
    }


    private File[] getBackupFiles() {
        return new File(ProjectDirectory.APP_PATH).listFiles(new FilenameFilter() {
            @Override
            public boolean accept(File file, String name) {
                return name.endsWith(SmsBrowser.fileFormat);
            }
        });
    }


    private ArrayList<SmsBrowser> getSmsBrowsers(File[] backupFiles) {
        ArrayList<SmsBrowser> smsBrowsers = new ArrayList<>();
        for (File file : backupFiles) {
            SmsBrowser browser = SmsBrowser.readClassFile(file);
            if (browser != null) {
                browser.fileName = file.getName();
                smsBrowsers.add(browser);
            }
        }

        return smsBrowsers;
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == DEFAULT_MESSAGE_APP_REQUEST_CODE) {
            restoreSms();
        }
    }


    private void restoreSms() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            //the device runs on kitkat and above.
            if (!isDefaultSmsApp()) {
                showSimpleMessageBox(getString(R.string.msg_app_default_message_permission), new OnButtonClick() {
                    @TargetApi(Build.VERSION_CODES.KITKAT)
                    @Override
                    public void onYesClick(MessageDialog messageDialog) {
                        super.onYesClick(messageDialog);
                        messageDialog.dismiss();
                        Intent intent = new Intent(Telephony.Sms.Intents.ACTION_CHANGE_DEFAULT);
                        intent.putExtra(Telephony.Sms.Intents.EXTRA_PACKAGE_NAME, getPackageName());
                        startActivityForResult(intent, DEFAULT_MESSAGE_APP_REQUEST_CODE);
                    }
                });
            } else {
                //restore sms normally.
                restore(backupFileListAdapter.getSelectedSMSBrowser());
            }
        }

    }


    @TargetApi(Build.VERSION_CODES.KITKAT)
    private boolean isDefaultSmsApp() {
        return getPackageName().equals(Telephony.Sms.getDefaultSmsPackage(this));
    }


    private void restore(final ArrayList<SmsBrowser> smsBrowsers) {
        backupFileListAdapter.selectAllConversation(false);
        isSelectionButtonClicked = false;
        backupFileListAdapter.notifyDataSetChanged();

        if (smsBrowsers.size() > 0)
            SmsBrowser.restoreSms(this, smsBrowsers);
        else {
            vibrate(5);
            showSimpleMessageBox(getString(R.string.select_some_backup_files));
        }
    }


    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.bnt_back)
            finish();

        else if (view.getId() == R.id.bnt_select)
            toggleAllSelection();

        else if (view.getId() == R.id.txt_restore)
            restoreSms();
    }


    private void toggleAllSelection() {
        if (!isSelectionButtonClicked) {
            backupFileListAdapter.selectAllConversation(true);
            isSelectionButtonClicked = true;

        } else {
            backupFileListAdapter.selectAllConversation(false);
            isSelectionButtonClicked = false;
        }
        backupFileListAdapter.notifyDataSetChanged();
    }

}
