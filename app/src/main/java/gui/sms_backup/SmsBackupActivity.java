package gui.sms_backup;

import android.os.Bundle;
import android.util.Log;
import android.view.View;

import java.util.ArrayList;

import gui.BaseActivity;
import in.softc.app.R;
import utils.ViewUtility;

public class SmsBackupActivity extends BaseActivity implements View.OnClickListener {

    @Override
    public int getLayoutResId() {
        return R.layout.activity_sms_backup_activity;
    }


    @Override
    public void onInitialize(Bundle bundle) {
        ViewUtility.setViewOnClickListener(this, this, R.id.bnt_backup);
    }


    @Override
    public void onClosed() {
        finish();
    }


    public void onBackClicked(View view) {
        onClosed();
    }


    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.bnt_backup) {
            ArrayList<Sms> allSms = SmsBrowser.getAllSms(this, null);
            ArrayList<Conversation> conversations = SmsBrowser.getAllConversations(allSms);
            for (Conversation c : conversations) {
                Log.d("Conversation", c.senderAddress);
            }

            Log.d("All Converation", "Conversations size : " + conversations.size());
        }
    }
}
