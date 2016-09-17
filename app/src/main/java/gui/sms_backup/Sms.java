package gui.sms_backup;

import java.io.Serializable;
import java.util.HashMap;

public class Sms implements Serializable {
    private static final long serialVersionUID = 296984946043656L;

    public HashMap<String, String> data = new HashMap<>();


    public String getSenderAddress() {
        for (String key : data.keySet())
            if (key.equals("address"))
                return data.get(key);

        return "";
    }


    public String getMessageBody() {
        for (String key : data.keySet())
            if (key.equals("body"))
                return data.get(key);

        return "";
    }


    public static boolean isSame(Sms firstSms, Sms secondSms) {
        if (firstSms.data.keySet().size() == secondSms.data.keySet().size()) {
            int keyCount = firstSms.data.size();
            int isSameCount = 0;
            for (String key : firstSms.data.keySet()) {
                String firstVale = firstSms.data.get(key);
                String secondValue = secondSms.data.get(key);
                if (firstVale == null && secondValue == null)
                    isSameCount ++;
                else if ((firstVale != null && secondValue != null) && (firstVale.equals(secondValue)))
                    isSameCount++;
            }

            return isSameCount == keyCount;

        } else {
            return false;
        }
    }
}
