package utils;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.os.Build;
import android.telephony.TelephonyManager;
import android.util.Base64;
import android.util.Log;

import java.security.MessageDigest;

import static android.content.Context.TELEPHONY_SERVICE;
import static android.content.pm.PackageManager.GET_SIGNATURES;

/**
 * The class offers device related useful functions.
 */
@SuppressWarnings("unused")
public class DeviceTool {

    public static String getDeviceName() {
        String manufacturer = Build.MANUFACTURER;
        String model = Build.MODEL;
        if (model.startsWith(manufacturer)) {
            return TextUtility.capitalize(model);
        } else {
            return TextUtility.capitalize(manufacturer) + " " + model;
        }
    }


    public static void printOutKeyHashCode(Context context) {
        try {
            PackageManager pm = context.getPackageManager();
            @SuppressLint("PackageManagerGetSignatures")
            PackageInfo pi = pm.getPackageInfo(context.getPackageName(), GET_SIGNATURES);
            for (Signature s : pi.signatures) {
                MessageDigest msg = MessageDigest.getInstance("SHA");
                msg.update(s.toByteArray());
                String logMsg = "KeyHash = " + Base64.encodeToString(msg.digest(), Base64.DEFAULT);
                Log.i(context.getPackageName(), logMsg);
            }
        } catch (Exception err) {
            err.printStackTrace();
        }
    }


    public static String getTelephonyServiceProvider(Context context) {
        TelephonyManager manager = (TelephonyManager) context.getSystemService(TELEPHONY_SERVICE);
        return manager.getNetworkOperatorName();
    }
}

