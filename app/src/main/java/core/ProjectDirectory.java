package core;

import android.content.Context;

import java.io.File;

import static android.os.Environment.getExternalStorageDirectory;

/**
 * The class responsible for verifying the project directory in device's sdcard.
 */
public class ProjectDirectory {

    public final static String APP_PATH = getExternalStorageDirectory() + "/" + "SMS_Backup";


    /**
     * Validate the system paths, if the paths are not exists then it
     * creates them.
     */
    public static boolean validate(Context context) {
        File appPathFile = new File(APP_PATH);
        boolean isVerified = false;
        if (appPathFile.exists())
            if (appPathFile.isDirectory())
                isVerified = true;

        return isVerified;
    }
}
