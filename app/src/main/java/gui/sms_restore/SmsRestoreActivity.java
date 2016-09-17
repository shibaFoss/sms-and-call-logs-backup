package gui.sms_restore;

import android.os.Bundle;
import android.view.View;
import android.widget.ListView;

import java.io.File;
import java.io.FilenameFilter;
import java.util.ArrayList;

import core.ProjectDirectory;
import gui.BaseActivity;
import gui.sms_backup.SmsBrowser;
import gui.static_dialogs.ProgressDialog;
import in.softc.app.R;
import libs.AsyncJob;
import utils.Font;
import utils.ViewUtility;

public class SmsRestoreActivity extends BaseActivity implements View.OnClickListener {

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
    }


    @Override
    public void onClosed() {
        finish();
    }


    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.bnt_back)
            finish();

        if (view.getId() == R.id.bnt_select)
            toggleAllSelection();

        if (view.getId() == R.id.txt_restore) {
            if (backupFileListAdapter != null)
                restoreSms(backupFileListAdapter.getSelectedSMSBrowser());
        }
    }


    private void restoreSms(final ArrayList<SmsBrowser> smsBrowsers) {
        backupFileListAdapter.selectAllConversation(false);
        isSelectionButtonClicked = false;
        backupFileListAdapter.notifyDataSetChanged();

        SmsBrowser.restoreSms(SmsRestoreActivity.this, smsBrowsers);
        vibrate(10);
        showSimpleMessageBox(getString(R.string.restored_successfully));
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

}
