package utils;

import android.os.Build;

@SuppressWarnings("unused")
public class OsUtility {

    public static int getBuildSdk() {
        return Build.VERSION.SDK_INT;
    }

}
