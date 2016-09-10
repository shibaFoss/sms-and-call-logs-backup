package utils;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.Typeface;
import android.support.annotation.IdRes;
import android.view.View;
import android.widget.TextView;

@SuppressWarnings("unused")
public class FontUtility {

    public static final String RegularFontPath = "fonts/Ubuntu-R.ttf";
    public static final String MediumFontPath = "fonts/Ubuntu-M.ttf";
    public static final String LightFontPath = "fonts/Ubuntu-L.ttf";

    public static Typeface Regular;
    public static Typeface Light;
    public static Typeface Medium;


    public static void initialize(Context context) {
        Regular = Typeface.createFromAsset(context.getAssets(), RegularFontPath);
        Light = Typeface.createFromAsset(context.getAssets(), LightFontPath);
        Medium = Typeface.createFromAsset(context.getAssets(), MediumFontPath);
    }


    public static void setFontFromAssetManager(String fontName, TextView textView) {
        AssetManager assetManager = textView.getContext().getAssets();
        Typeface font = Typeface.createFromAsset(assetManager, fontName);
        textView.setTypeface(font);
    }


    public static void setFont(Typeface font, Activity activity, @IdRes int... resIds) {
        for (int id : resIds) {
            View view = activity.findViewById(id);
            if (view instanceof TextView) {
                ((TextView) view).setTypeface(font);
            }
        }
    }


    public static void setFont(Typeface font, View mainView, @IdRes int... resIds) {
        for (int id : resIds) {
            View view = mainView.findViewById(id);
            if (view instanceof TextView) {
                ((TextView) view).setTypeface(font);
            }
        }
    }


    public static void setFont(Typeface font, Dialog dialog, @IdRes int... resIds) {
        for (int id : resIds) {
            View view = dialog.findViewById(id);
            if (view instanceof TextView) {
                ((TextView) view).setTypeface(font);
            }
        }
    }

}
