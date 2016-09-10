package gui.static_dialogs;

import android.app.Dialog;
import android.widget.TextView;

import gui.BaseActivity;
import in.softc.app.R;
import libs.AsyncJob;
import utils.DialogUtility;

/**
 * This class very useful if you want to show a progressDialog.
 */
public final class ProgressDialog {

    private Dialog dialog;
    private TextView progressLoadingText;

    public ProgressDialog(BaseActivity activity, boolean cancelable, String progressText) {
        dialog = DialogUtility.generateNewDialog(activity, R.layout.dialog_progress);
        dialog.setCancelable(cancelable);
        progressLoadingText = (TextView) dialog.findViewById(R.id.txt_title);
        progressLoadingText.setText(progressText);
    }

    public void showInMainThread() {
        AsyncJob.doInMainThread(new AsyncJob.MainThreadJob() {
            @Override
            public void doInUIThread() {
                try {
                    dialog.show();
                } catch (Exception error) {
                    error.printStackTrace();
                }
            }
        });
    }

    public void closeInMainThread() {
        AsyncJob.doInMainThread(new AsyncJob.MainThreadJob() {
            @Override
            public void doInUIThread() {
                try {
                    dialog.dismiss();
                } catch (Exception error) {
                    error.printStackTrace();
                }
            }
        });
    }

    public Dialog getDialog() {
        return this.dialog;
    }
}
