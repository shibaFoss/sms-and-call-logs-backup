package gui.sms_backup;

import java.io.Serializable;

public class Sms implements Serializable {
    private static final long serialVersionUID = 296984946043656L;

    public String columnIdArray[];
    public String columnDataArray[];


    @Override
    public String toString() {
        String result = "";
        if (columnIdArray.length == columnDataArray.length)
            for (int i = 0; i < columnIdArray.length; i++)
                result += "Id:" + columnIdArray[i] + "-------" + "Data:" + columnDataArray[i] + "\n";
        return result;
    }


    public String getSenderAddress() {
        if (columnIdArray != null && columnIdArray.length > 0)
            return columnIdArray[getColumnIndex("address")];

        return "";
    }

    public String getMessageBody() {
        if (columnIdArray != null && columnIdArray.length > 0)
            return columnIdArray[getColumnIndex("body")];

        return "";
    }

    public int getColumnIndex(String columnName) {
        if (columnDataArray != null && columnDataArray.length > 0)
            for (int i = 0; i < columnDataArray.length; i++)
                if (columnDataArray[i].equals(columnName))
                    return i;

        return -1;
    }

}
