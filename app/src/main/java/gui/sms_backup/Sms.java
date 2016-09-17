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


}
