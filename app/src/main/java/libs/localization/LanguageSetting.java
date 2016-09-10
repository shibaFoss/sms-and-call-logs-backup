package libs.localization;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;

import java.util.Locale;

public class LanguageSetting {
    private static final String PREFERENCE_LANGUAGE = "pref_language";
    private static final String KEY_LANGUAGE = "key_language";
    private static String DEFAULT_LANGUAGE = Locale.ENGLISH.getLanguage();
    private static String currentLanguage = Locale.ENGLISH.getLanguage();


    public static void setDefaultLanguage(String language) {
        DEFAULT_LANGUAGE = language;
    }


    public static String getDefaultLanguage() {
        return DEFAULT_LANGUAGE;
    }


    public static void setDefaultLanguage(Locale locale) {
        LanguageSetting.setDefaultLanguage(locale.getLanguage());
    }


    public static void setLanguage(Context context, String language) {
        currentLanguage = language;
        SharedPreferences.Editor editor = getLanguagePreference(context).edit();
        editor.putString(KEY_LANGUAGE, language);
        editor.apply();
    }


    public static void setLanguage(Context context, Locale locale) {
        setLanguage(context, locale.getLanguage());
    }


    public static String getLanguage() {
        return currentLanguage;
    }


    private static String getLanguage(Context context) {
        return getLanguagePreference(context).getString(KEY_LANGUAGE, DEFAULT_LANGUAGE);
    }


    private static SharedPreferences getLanguagePreference(Context context) {
        return context.getSharedPreferences(PREFERENCE_LANGUAGE, Activity.MODE_PRIVATE);
    }


    public static Locale getLocale() {
        return getLocale(getLanguage());
    }


    public static Locale getLocale(Context context) {
        return getLocale(getLanguage(context));
    }


    public static Locale getLocale(String language) {
        return new Locale(language.toLowerCase(Locale.getDefault()));
    }

}
