package libs.localization;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;

import java.util.Locale;

public class LocalizationActivity extends AppCompatActivity implements OnLocaleChangedListener {
    // Bundle key
    private static final String KEY_ACTIVITY_LOCALE_CHANGED = "activity_locale_changed";

    // Boolean flag to check that activity was recreated from locale changed.
    private boolean isLocalizationChanged = false;

    // Prepare default language.
    private String currentLanguage = LanguageSetting.getDefaultLanguage();


    @Override
    public void onCreate(Bundle savedInstanceState) {
        setupLanguage();
        checkBeforeLocaleChanging();
        super.onCreate(savedInstanceState);
    }


    // Provide method to set application language by country name.
    public final void setLanguage(String language) {
        if (!isDuplicatedLanguageSetting(language)) {
            LanguageSetting.setLanguage(this, language);
            notifyLanguageChanged();
        }
    }


    public final void setDefaultLanguage(String language) {
        LanguageSetting.setDefaultLanguage(language);
    }


    public final void setDefaultLanguage(Locale locale) {
        LanguageSetting.setDefaultLanguage(locale.getLanguage());
    }


    // Get current language
    public final String getLanguage() {
        return LanguageSetting.getLanguage();
    }


    // Provide method to set application language by locale.
    public final void setLanguage(Locale locale) {
        setLanguage(locale.getLanguage());
    }


    // Get current locale
    public final Locale getLocale() {
        return LanguageSetting.getLocale(this);
    }


    // Check that bundle come from locale change.
    // If yes, bundle will obe remove and set boolean flag to "true".
    private void checkBeforeLocaleChanging() {
        boolean isLocalizationChanged = getIntent().getBooleanExtra(KEY_ACTIVITY_LOCALE_CHANGED, false);
        if (isLocalizationChanged) {
            this.isLocalizationChanged = true;
            getIntent().removeExtra(KEY_ACTIVITY_LOCALE_CHANGED);
        }
    }


    // Setup language to locale and language preference.
    // This method will called before onCreate.
    private void setupLanguage() {
        Locale locale = LanguageSetting.getLocale(this);
        setupLocale(locale);
        currentLanguage = locale.getLanguage();
        LanguageSetting.setLanguage(this, locale.getLanguage());
    }


    // Set locale configuration.
    private void setupLocale(Locale locale) {
        updateLocaleConfiguration(this, locale);
    }


    private void updateLocaleConfiguration(Context context, Locale locale) {
        Configuration config = new Configuration();
        config.locale = locale;
        DisplayMetrics dm = context.getResources().getDisplayMetrics();
        context.getResources().updateConfiguration(config, dm);
    }


    // Avoid duplicated setup
    private boolean isDuplicatedLanguageSetting(String language) {
        return language.toLowerCase(Locale.getDefault()).equals(LanguageSetting.getLanguage());
    }


    // Let's take it change! (Using recreate method that available on API 11 or more.
    private void notifyLanguageChanged() {
        onBeforeLocaleChanged();
        getIntent().putExtra(KEY_ACTIVITY_LOCALE_CHANGED, true);
        callDummyActivity();
        recreate();
    }


    // If activity is run to back-stack. So we have to check if this activity is resume working.
    @Override
    public void onResume() {
        super.onResume();
        new Handler().post(new Runnable() {
            @Override
            public void run() {
                checkLocaleChange();
                checkAfterLocaleChanging();
            }
        });
    }


    // Check if locale has change while this activity was run to back-stack.
    private void checkLocaleChange() {
        if (!LanguageSetting.getLanguage().toLowerCase(Locale.getDefault())
                .equals(currentLanguage.toLowerCase(Locale.getDefault()))) {
            callDummyActivity();
            recreate();
        }
    }


    // Call override method if local is really changed
    private void checkAfterLocaleChanging() {
        if (isLocalizationChanged) {
            onAfterLocaleChanged();
            isLocalizationChanged = false;
        }
    }


    private void callDummyActivity() {
        startActivity(new Intent(this, BlankDummyActivity.class));
    }


    // Just override method locale change event
    @Override
    public void onBeforeLocaleChanged() {
    }


    @Override
    public void onAfterLocaleChanged() {
    }
}
