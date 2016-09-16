package gui.sms_backup;

import java.io.Serializable;
import java.util.ArrayList;

public class Conversation implements Serializable {

    private static final long serialVersionUID = 256984946043656L;

    public boolean isSelected = false;
    public String senderAddress;
    public ArrayList<Sms> allSms = new ArrayList<>();

}
