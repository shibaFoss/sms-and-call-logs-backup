package gui.sms_backup;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.provider.Settings;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;

import core.ProjectDirectory;
import gui.BaseActivity;
import gui.static_dialogs.MessageDialog;
import in.softc.app.R;
import utils.BaseWritableObject;

/**
 * The class is like a database where we put and restore {@link Sms} objects. The class itself can save to sdcard and
 * later we can convert the file to the object. So all data will be retrieve by this system.
 */
public class SmsBrowser extends BaseWritableObject {

    /**
     * this is the file format of the sms backup file. just for recognizing the correct object file from sdcard.
     */
    public static final String fileFormat = ".sms";
    static final long serialVersionUID = -8394949271450580006L;
    /**
     * The following two variable is used at {@link ConversationsListAdapter} class. so it doesn't do anything to the
     * database. It's used just for font-end work.
     */
    public String fileName = "";
    public boolean isSelected = false;

    //-------------------------- Actual Data base ------------------------------------------------------------//
    public ArrayList<Conversation> allConversations = new ArrayList<>();
    public ArrayList<Sms> allSms = new ArrayList<>();
    public String backupNote = "No Backup note.";
    //-------------------------------------------------------------------------------------------------------//


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
                int columnCount = cursor.getColumnCount();
                Sms sms = new Sms();
                sms.data = new HashMap<>();
                for (int position = 0; position < columnCount; position++) {
                    String columnName = cursor.getColumnName(position);
                    String columnValue = cursor.getString(position);

                    if (columnName.equals("address"))
                        sms.data.put(columnName, columnValue);

                    else if (columnName.equals("body"))
                        sms.data.put(columnName, columnValue);

                    else if (columnName.equals("contact_name"))
                        sms.data.put(columnName, columnValue);

                    else if (columnName.equals("date"))
                        sms.data.put(columnName, columnValue);

                    else if (columnName.equals("date_sent"))
                        sms.data.put(columnName, columnValue);

                    else if (columnName.equals("locked"))
                        sms.data.put(columnName, columnValue);

                    else if (columnName.equals("protocol"))
                        sms.data.put(columnName, columnValue);

                    else if (columnName.equals("read"))
                        sms.data.put(columnName, columnValue);

                    else if (columnName.equals("service_center"))
                        sms.data.put(columnName, columnValue);

                    else if (columnName.equals("status"))
                        sms.data.put(columnName, columnValue);

                    else if (columnName.equals("subject"))
                        sms.data.put(columnName, columnValue);

                    else if (columnName.equals("type"))
                        sms.data.put(columnName, columnValue);
                }
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


    public static void restoreSms(final BaseActivity activity, ArrayList<SmsBrowser> smsBrowsers) {
        ArrayList<Sms> allSms = removeDuplicateSms(smsBrowsers);
        ContentResolver contentResolver = activity.getContentResolver();
        Uri queryUri = Uri.parse("content://sms/");

        int totalRowCreated = 0;
        for (Sms sms : allSms) {
            ContentValues values = new ContentValues();
            for (String key : sms.data.keySet()) {
                values.put(key, sms.data.get(key));
            }
            totalRowCreated += contentResolver.bulkInsert(queryUri, new ContentValues[]{values});
            contentResolver.notifyChange(queryUri, null);
        }

        activity.vibrate(10);
        MessageDialog messageDialog = new MessageDialog(activity);
        messageDialog.setButtonName(activity.getString(R.string.open_setting_ui),
                activity.getString(R.string.cancel));
        messageDialog.setMessage(totalRowCreated + " " +
                activity.getString(R.string.sms_restored_successfully_msg));
        messageDialog.setTitle(activity.getString(R.string.restoring_sms_completed));
        messageDialog.setCallback(new MessageDialog.OnClickButton() {
            @Override
            public void onYesClick(MessageDialog messageDialog) {
                messageDialog.dismiss();
                activity.startActivity(new Intent(Settings.ACTION_WIRELESS_SETTINGS));
                activity.finish();
            }


            @Override
            public void onNoClick(MessageDialog messageDialog) {
                messageDialog.dismiss();
                activity.finish();
            }
        });
        messageDialog.show();

    }


    private static ArrayList<Sms> removeDuplicateSms(ArrayList<SmsBrowser> smsBrowsers) {
        ArrayList<Sms> result = new ArrayList<>();
        for (SmsBrowser browser : smsBrowsers) {
            for (Sms sms : browser.allSms) {
                boolean alreadyAdded = false;
                for (Sms storedSms : result)
                    if (Sms.isSame(storedSms, sms))
                        alreadyAdded = true;

                if (!alreadyAdded)
                    result.add(sms);
            }
        }

        return result;
    }


    public void saveClass(String fileName) {
        writeObject(this, ProjectDirectory.APP_PATH, fileName);
    }
}
