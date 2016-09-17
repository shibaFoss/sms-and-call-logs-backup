package gui.sms_restore;

import android.support.v7.widget.AppCompatCheckBox;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import gui.sms_backup.Sms;
import gui.sms_backup.SmsBrowser;
import in.softc.app.R;
import utils.Font;

public class BackupFileListAdapter extends BaseAdapter {
    private SmsRestoreActivity restoreActivity;
    private ArrayList<SmsBrowser> smsBrowsers;


    public BackupFileListAdapter(SmsRestoreActivity restoreActivity, ArrayList<SmsBrowser> smsBrowsers) {
        this.restoreActivity = restoreActivity;
        this.smsBrowsers = smsBrowsers;
    }


    @Override
    public int getCount() {
        TextView emptyBox = (TextView) restoreActivity.findViewById(R.id.txt_empty_box);
        emptyBox.setTypeface(Font.LatoMedium);
        emptyBox.setVisibility(smsBrowsers.size() < 1 ? View.VISIBLE : View.GONE);
        return smsBrowsers.size();
    }


    @Override
    public Object getItem(int index) {
        return smsBrowsers.get(index);
    }


    @Override
    public long getItemId(int index) {
        return index;
    }


    @Override
    public View getView(final int position, View view, ViewGroup viewGroup) {
        final ViewHolder viewHolder;
        if (view == null) {
            view = View.inflate(restoreActivity, R.layout.activity_sms_restore_backup_file_list_row, null);
            view.setClickable(true);
            viewHolder = new ViewHolder();
            viewHolder.checkBox = (AppCompatCheckBox) view.findViewById(R.id.checkbox);
            viewHolder.backupFileName = (TextView) view.findViewById(R.id.txt_backup_file_name);
            viewHolder.backupFileInfo = (TextView) view.findViewById(R.id.txt_backup_file_info);

            viewHolder.backupFileName.setTypeface(Font.LatoRegular);
            viewHolder.backupFileInfo.setTypeface(Font.LatoLight);

            view.setTag(viewHolder);

        } else {
            viewHolder = (ViewHolder) view.getTag();
        }

        final SmsBrowser smsBrowser = (SmsBrowser) getItem(position);
        Sms sms = smsBrowser.allSms.get(0);
        viewHolder.backupFileName.setText(smsBrowser.fileName);
        viewHolder.backupFileInfo.setText(String.valueOf(restoreActivity.getString(R.string.total_sms_)
                + smsBrowser.allSms.size()));
        viewHolder.checkBox.setChecked(smsBrowser.isSelected);

        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                smsBrowser.isSelected = !smsBrowser.isSelected;
                viewHolder.checkBox.setChecked(smsBrowser.isSelected);
            }
        });

        viewHolder.checkBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                smsBrowser.isSelected = !smsBrowser.isSelected;
                viewHolder.checkBox.setChecked(smsBrowser.isSelected);
            }
        });

        return view;
    }


    public ArrayList<SmsBrowser> getSelectedSMSBrowser() {
        ArrayList<SmsBrowser> list = new ArrayList<>();
        for (SmsBrowser smsBrowser : smsBrowsers)
            if (smsBrowser.isSelected)
                list.add(smsBrowser);

        return list;
    }


    public void selectAllConversation(boolean isSelect) {
        for (SmsBrowser smsBrowser : smsBrowsers)
            smsBrowser.isSelected = isSelect;
    }


    public static class ViewHolder {
        public TextView backupFileName, backupFileInfo;
        public AppCompatCheckBox checkBox;
    }

}
