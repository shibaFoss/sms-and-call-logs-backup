package utils;

import android.content.Context;
import android.content.pm.PackageManager;

/**
 * The class helps you to get the current version name and version code
 * of the app.
 */
@SuppressWarnings("unused")
public class AppVersionUtils {

    public static String getVersionName(Context context) {
        try {
            PackageManager packageManager = context.getPackageManager();
            return packageManager.getPackageInfo(context.getPackageName(), 0).versionName;
        } catch (Throwable error) {
            error.printStackTrace();
            return "";
        }
    }


    public static int getVersionCode(Context context) {
        try {
            PackageManager packageManager = context.getPackageManager();
            return packageManager.getPackageInfo(context.getPackageName(), 0).versionCode;
        } catch (Throwable error) {
            error.printStackTrace();
            return 0;
        }
    }

}
