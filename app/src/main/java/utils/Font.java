package utils;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.Typeface;
import android.support.annotation.IdRes;
import android.view.View;
import android.widget.TextView;

/**
 * Font : The class is a utility class, and offers some useful functions for applying
 * custom fonts to text views.
 */
public class Font {

    public static final String Lato_Regular = "fonts/Lato-Regular.ttf";
    public static final String Lato_Medium = "fonts/Lato-Medium.ttf";
    public static final String Lato_Light = "fonts/Lato-Light.ttf";
    public static final String OpenSans_Regular = "fonts/OpenSans-Regular.ttf";

    public static Typeface LatoRegular;
    public static Typeface LatoMedium;
    public static Typeface LatoLight;
    public static Typeface OpenSansRegular;

    public static void init(Context context) {
        OpenSansRegular = Typeface.createFromAsset(context.getAssets(), OpenSans_Regular);
        LatoRegular = Typeface.createFromAsset(context.getAssets(), Lato_Regular);
        LatoMedium = Typeface.createFromAsset(context.getAssets(), Lato_Medium);
        LatoLight = Typeface.createFromAsset(context.getAssets(), Lato_Light);
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
