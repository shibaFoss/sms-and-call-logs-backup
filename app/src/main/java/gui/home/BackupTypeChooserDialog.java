package gui.home;

import android.app.Dialog;
import android.view.View;
import android.widget.TextView;

import gui.BaseActivity;
import gui.sms_backup.SmsBackupActivity;
import in.softc.app.R;
import utils.DialogUtility;
import utils.Font;
import utils.ViewUtility;

public class BackupTypeChooserDialog implements View.OnClickListener {

    private BaseActivity activity;
    private Dialog dialog;


    public BackupTypeChooserDialog(BaseActivity activity) {
        this.activity = activity;
        this.dialog = DialogUtility.generateNewDialog(activity, R.layout.dialog_backup_type_chooser);

        TextView title = (TextView) dialog.findViewById(R.id.txt_title);
        title.setText(activity.getString(R.string.select_backup_type));
        title.setTypeface(Font.LatoMedium);

        Font.setFont(Font.LatoRegular, dialog, R.id.txt_sms_backup, R.id.txt_call_backup, R.id.txt_contact_backup);
        ViewUtility.setViewOnClickListener(this, dialog,
                R.id.bnt_sms_backup, R.id.bnt_call_backup, R.id.bnt_contact_backup);
    }


    @Override
    public void onClick(View view) {
        getDialog().dismiss();

        switch (view.getId()) {
            case R.id.bnt_sms_backup: {
                activity.startActivity(SmsBackupActivity.class);
            }
            break;

            case R.id.bnt_call_backup: {

            }
            break;

            case R.id.bnt_contact_backup: {

            }
            break;
        }
    }


    public Dialog getDialog() {
        return this.dialog;
    }
}
