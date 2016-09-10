package gui.sms_backup;

import android.content.ContentProvider;
import android.content.ContentResolver;
import android.database.Cursor;
import android.net.Uri;

import java.util.ArrayList;

import gui.BaseActivity;

/**
 * The class reads the sms from the {@link ContentProvider} and list them in a array list.
 */
public class SmsManager extends BaseWritableObject {

    public BaseActivity activity;
    public ArrayList<SMS> smsList;

    public SmsManager(BaseActivity activity) {
        this.activity = activity;
        this.smsList = new ArrayList<>();
    }

    public ArrayList<Conversation> getAllConversations() {
        ArrayList<Conversation> conversationList = new ArrayList<>();
        ArrayList<SMS> allSmsList = getAllSms("all");
        for (SMS sms : allSmsList) {

        }

        return null;
    }

    /**
     * The function will get all the sms from the {@link ContentResolver} and return
     * all the sms with a list.
     * @param folderName the sms folder either 'inbox' or 'sent' or 'all'
     * @return the list of all retrieved sms.
     */
    public ArrayList<SMS> getAllSms(String folderName) {
        ContentResolver contentResolver = activity.getContentResolver();
        final String uriPath = (folderName == null || folderName.length() < 1 || folderName.equals("all")) ?
                "content://sms/" : "content://sms/" + folderName;
        Uri queryUri = Uri.parse(uriPath);
        Cursor cursor = contentResolver.query(queryUri, null, null, null, null);
        if (cursor != null) {
            while (cursor.moveToNext()) {
                //first we need to get the column size of every cursor row.
                int columnSize = cursor.getColumnCount();
                SMS sms = new SMS();
                sms.columnIdArray = new String[columnSize];
                sms.columnDataArray = new String[columnSize];
                for (int position = 0; position < columnSize; position++) {
                    sms.columnDataArray[position] = cursor.getColumnName(position);
                    sms.columnIdArray[position] = cursor.getString(position);
                }
                smsList.add(sms);
            }

            if (!cursor.isClosed())
                cursor.close();
        }
        return smsList;
    }

    /**
     * The structural class of sms.
     */
    public static class SMS {
        public String columnIdArray[];
        public String columnDataArray[];
    }

    /**
     * The structural class of conversations.
     */
    public static class Conversation {
        public SMS[] allSms;
    }
}
