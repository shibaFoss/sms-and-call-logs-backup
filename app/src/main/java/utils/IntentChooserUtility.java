package utils;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ResolveInfo;

import java.util.List;

@SuppressWarnings("unused")
public class IntentChooserUtility {

    public static List<ResolveInfo> getMatchingIntentActivities(Activity activity, Intent intent) {
        return activity.getPackageManager().queryIntentActivities(intent, 0);
    }

}

