package gui.sms_backup;

import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.TextView;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

import core.ProjectDirectory;
import gui.BaseActivity;
import gui.static_dialogs.MessageDialog;
import gui.static_dialogs.OnButtonClick;
import gui.static_dialogs.ProgressDialog;
import in.softc.app.R;
import libs.AsyncJob;
import utils.DialogUtility;
import utils.FileUtils;
import utils.Font;
import utils.ViewUtility;

/**
 * The class prompt user a dialog where user can write an backup note and then execute the backup process.
 */
public class BackupDialog {

    private Dialog dialog;
    private TextView editFileName, editFileNote;
    private TextView yesBnt;
    private TextView noBnt;
    private BaseActivity activity;


    public BackupDialog(final BaseActivity activity, final ArrayList<Conversation> conversations) {
        this.activity = activity;
        dialog = DialogUtility.generateNewDialog(activity, R.layout.dialog_create_backup_file);
        Font.setFont(Font.LatoRegular, dialog, R.id.edit_file_name, R.id.edit_file_note, R.id.bntYes, R.id.bntNo);
        initActionButtonOnClickEvent(activity, conversations);

        TextView title = (TextView) dialog.findViewById(R.id.txt_title);
        editFileName = (TextView) dialog.findViewById(R.id.edit_file_name);
        editFileNote = (TextView) dialog.findViewById(R.id.edit_file_note);
        yesBnt = (TextView) dialog.findViewById(R.id.bntYes);
        noBnt = (TextView) dialog.findViewById(R.id.bntNo);

        noBnt.setText(R.string.cancel);
        yesBnt.setText(R.string.backup);

        title.setText(R.string.backup_file);
        title.setTypeface(Font.LatoMedium);

        String suggestedFileName = getSuggestedFileName();
        editFileName.setText(suggestedFileName);
    }


    private void initActionButtonOnClickEvent(final BaseActivity activity,
                                              final ArrayList<Conversation> conversations) {
        ViewUtility.setViewOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (view.getId() == yesBnt.getId()) {
                    backup(activity, conversations);

                } else if (view.getId() == noBnt.getId()) {
                    dialog.dismiss();
                }
            }
        }, dialog, R.id.bntYes, R.id.bntNo);
    }


    private String getSuggestedFileName() {
        return "Backup-" + new SimpleDateFormat("yyyy-MM-dd-HH-mm", Locale.ENGLISH)
                .format(Calendar.getInstance().getTime()) + SmsBrowser.fileFormat;
    }


    private void backup(final BaseActivity activity, ArrayList<Conversation> conversations) {
        String fileName = editFileName.getText().toString();
        if (fileName.length() < 1) {
            activity.vibrate(10);
            activity.toast(activity.getString(R.string.give_a_file_name));
            return;
        }

        if (!fileName.endsWith(SmsBrowser.fileFormat))
            fileName += SmsBrowser.fileFormat;

        FileUtils.makeDirectory(new File(ProjectDirectory.APP_PATH));
        final SmsBrowser smsBrowser = new SmsBrowser();
        smsBrowser.allConversations = conversations;
        smsBrowser.allSms = SmsBrowser.getAllSms(conversations);
        String value = editFileNote.getText().toString();
        smsBrowser.backupNote = (value.length() > 1 ? value : "No Backup Note Found.");

        final String finalFileName = fileName;
        final ProgressDialog progressDialog = new ProgressDialog(activity, false, activity.getString(R.string.backup_sms_wait));
        AsyncJob.doInBackground(new AsyncJob.BackgroundJob() {
            @Override
            public void doInBackground() {
                progressDialog.showInMainThread();
                smsBrowser.saveClass(finalFileName);
                progressDialog.closeInMainThread();
                AsyncJob.doInMainThread(new AsyncJob.MainThreadJob() {
                    @Override
                    public void doInUIThread() {
                        dialog.dismiss();
                        activity.showSimpleMessageBox(activity.getString(R.string.sms_backup_successfully), new OnButtonClick() {
                            @Override
                            public void onYesClick(MessageDialog messageDialog) {
                                super.onYesClick(messageDialog);
                                messageDialog.dismiss();
                                activity.showFullscreenAd();
                                activity.finish();
                            }
                        });
                    }
                });
            }
        });
    }


    public void show() {
        dialog.show();
        InputMethodManager imm = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.showSoftInput(editFileNote, InputMethodManager.SHOW_FORCED);
        editFileNote.requestFocus();
    }
}
