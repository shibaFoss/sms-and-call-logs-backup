package gui.sms_backup;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;
import android.util.Log;

import java.io.File;
import java.util.ArrayList;

import core.ProjectDirectory;
import gui.BaseActivity;
import in.softc.app.R;
import utils.BaseWritableObject;

public class SmsBrowser extends BaseWritableObject {

    static final long serialVersionUID = -8394949271450580006L;
    public static final String fileFormat = ".smsdb";

    public String fileName = "";
    public boolean isSelected = false;

    public ArrayList<Conversation> allConversations = new ArrayList<>();
    public ArrayList<Sms> allSms = new ArrayList<>();


    public void saveClass(String fileName) {
        writeObject(this, ProjectDirectory.APP_PATH, fileName);
    }


    public static SmsBrowser readClassFile(File file) {
        return (SmsBrowser) readObject(file);
    }


    public static ArrayList<Conversation> getAllConversations(ArrayList<Sms> allSms) {
        ArrayList<Conversation> conversations = new ArrayList<>();
        for (Sms sms : allSms) {
            Conversation matchingC = findConversationByAddress(conversations, sms.getSenderAddress());
            if (matchingC != null) {
                //match found.
                matchingC.allSms.add(sms);

            } else {
                //no match found.
                Conversation c = new Conversation();
                c.senderAddress = sms.getSenderAddress();
                c.allSms.add(sms);
                conversations.add(c);
            }
        }

        return conversations;
    }


    private static Conversation findConversationByAddress(ArrayList<Conversation> list, String address) {
        for (Conversation c : list)
            if (c.senderAddress.equals(address))
                return c;

        return null;
    }


    /**
     * The function will get all the sms from the {@link ContentResolver} and return
     * all the sms with a list.
     * @param folderName the sms folder either 'inbox' or 'sent' or 'all'
     * @return the list of all retrieved sms.
     */
    public static ArrayList<Sms> getAllSms(BaseActivity activity, String folderName) {
        ArrayList<Sms> smsList = new ArrayList<>();
        ContentResolver contentResolver = activity.getContentResolver();

        String uriPath = (folderName == null || folderName.length() < 1 || folderName.equals("all")) ?
                "content://sms/" :
                "content://sms/" + folderName;

        Uri queryUri = Uri.parse(uriPath);
        Cursor cursor = contentResolver.query(queryUri, null, null, null, null);

        if (cursor != null) {
            while (cursor.moveToNext()) {
                //first we need to get the column size of every cursor row.
                int columnSize = cursor.getColumnCount();
                Sms sms = new Sms();
                sms.columnIdArray = new String[columnSize];
                sms.columnDataArray = new String[columnSize];
                for (int position = 0; position < columnSize; position++) {
                    sms.columnDataArray[position] = cursor.getColumnName(position);
                    sms.columnIdArray[position] = cursor.getString(position);
                }

                Log.d("SMS:", "" + sms.toString());
                smsList.add(sms);
            }

            if (!cursor.isClosed())
                cursor.close();
        }
        return smsList;
    }


    public static ArrayList<Sms> getAllSms(ArrayList<Conversation> conversations) {
        ArrayList<Sms> allSms = new ArrayList<>();
        for (Conversation con : conversations)
            for (Sms sms : con.allSms)
                allSms.add(sms);

        return allSms;
    }


    public static void restoreSms(BaseActivity activity, ArrayList<SmsBrowser> smsBrowsers) {
        ContentResolver contentResolver = activity.getContentResolver();
        Uri queryUri = Uri.parse("content://sms/");
        int totalRowCreated = 0;
        for (SmsBrowser browser : smsBrowsers) {
            for (Sms sms : browser.allSms) {
                ContentValues values = new ContentValues(sms.columnDataArray.length);
                for (int i = 0; i < sms.columnIdArray.length; i++) {
                    values.put(sms.columnDataArray[i], sms.columnIdArray[i]);
                    Log.d("Content Value", sms.columnDataArray[i] + "[]" + sms.columnIdArray[i]);
                }
                totalRowCreated += contentResolver.bulkInsert(queryUri, new ContentValues[]{values});
                contentResolver.notifyChange(queryUri, null);
            }
        }

        activity.vibrate(10);
        activity.showSimpleMessageBox(totalRowCreated + activity.getString(R.string.sms_restored_successfully));

    }
}
