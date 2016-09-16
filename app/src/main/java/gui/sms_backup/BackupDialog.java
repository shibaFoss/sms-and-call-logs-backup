package gui.sms_backup;

import android.app.Dialog;
import android.view.View;
import android.widget.TextView;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

import core.ProjectDirectory;
import gui.BaseActivity;
import in.softc.app.R;
import utils.DialogUtility;
import utils.FileUtils;
import utils.Font;
import utils.ViewUtility;

public class BackupDialog {

    private Dialog dialog;
    private TextView title, editFileName, yesBnt, noBnt;


    public BackupDialog(final BaseActivity activity, final ArrayList<Conversation> conversations) {
        dialog = DialogUtility.generateNewDialog(activity, R.layout.dialog_create_file_name);

        title = (TextView) dialog.findViewById(R.id.txt_title);
        editFileName = (TextView) dialog.findViewById(R.id.edit_file_name);
        yesBnt = (TextView) dialog.findViewById(R.id.bntYes);
        noBnt = (TextView) dialog.findViewById(R.id.bntNo);

        noBnt.setText(R.string.cancel);
        yesBnt.setText(R.string.backup);

        title.setText(R.string.backup_file_name);
        title.setTypeface(Font.LatoMedium);

        String suggestedFileName = getSuggestedFileName();
        editFileName.setText(suggestedFileName);

        Font.setFont(Font.LatoRegular, dialog, R.id.edit_file_name, R.id.bntYes, R.id.bntNo);
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
        return "SMS-Backup-" + new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss")
                .format(Calendar.getInstance().getTime()) + ".db";
    }


    private void backup(BaseActivity activity, ArrayList<Conversation> conversations) {
        String fileName = editFileName.getText().toString();
        if (fileName.length() < 1) {
            activity.vibrate(10);
            activity.toast(activity.getString(R.string.give_a_file_name));
            return;
        }

        FileUtils.makeDirectory(new File(ProjectDirectory.APP_PATH));
        SmsBrowser smsBrowser = new SmsBrowser();
        smsBrowser.allConversations = conversations;
        smsBrowser.allSms = SmsBrowser.getAllSms(conversations);
        smsBrowser.saveClass(fileName);
        dialog.dismiss();
        activity.showSimpleMessageBox(activity.getString(R.string.sms_get_backup));
    }


    public void show() {
        dialog.show();
    }
}
