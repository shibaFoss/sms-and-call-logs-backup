package core;

import android.app.Application;
import android.content.Context;
import android.content.pm.PackageManager;
import android.support.v4.content.ContextCompat;

import libs.Remember;
import utils.Font;

public class App extends Application {

    public static boolean isPermissionGranted(Context context, String permission) {
        int result = ContextCompat.checkSelfPermission(context, permission);
        return result == PackageManager.PERMISSION_GRANTED;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Remember.init(this, "App");
        Font.init(this);
    }

}
