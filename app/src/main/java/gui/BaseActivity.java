package gui;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Vibrator;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.text.Html;
import android.text.Spanned;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;

import core.AdMobUnitIds;
import core.App;
import gui.static_dialogs.MessageDialog;
import gui.static_dialogs.OnButtonClick;
import in.softc.app.R;
import libs.AsyncJob;
import libs.localization.LocalizationActivity;
import utils.OsUtility;
import utils.Timer;

import static android.content.pm.PackageManager.PERMISSION_GRANTED;

/**
 * Base class of all {@link android.app.Activity} classes of this project.
 * The class have all sort of useful functions that's are ofter used throughout any
 * activity creation.
 * @author Shiba
 */
public abstract class BaseActivity extends LocalizationActivity {

    /**
     * The variable is requesting code that is used to requesting permissions.
     */
    public static final int USES_PERMISSIONS_REQUEST_CODE = 4;
    public boolean isPremiumVersion;
    protected InterstitialAd fullscreenAd;
    private App app;
    private Vibrator vibrator;

    /**
     * The variable is used as a indication that if the activity is visible on not.
     */
    private boolean isActivityRunning = false;
    private int isBackPressEventFired = 0;


    @Override
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        initiate();
    }


    @Override
    public void onResume() {
        super.onResume();
        isActivityRunning = true;
    }


    private void initiate() {
        //initialize all the basic objects
        this.vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);
        this.app = (App) getApplication();

        //set the activity layout
        if (getLayoutResId() != -1)
            setContentView(getLayoutResId());
    }


    /**
     * The call back function is used to get the layout res Id of
     * the activity.
     * @return the res Id of the activity's layout xml file.
     */
    public abstract int getLayoutResId();


    @Override
    public void onPostCreate(Bundle bundle) {
        super.onPostCreate(bundle);
        onInitialize(bundle);
    }


    /**
     * The function get called on {@link android.app.Activity#onPostCreate(Bundle)}
     * function of the activity.
     * @param bundle the bundle reference that the activity uses to store data in the
     *               activity's whole life cycle.
     */
    public abstract void onInitialize(Bundle bundle);


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        onClosed();
    }


    /**
     * The function get called when the activity's {@link Activity#onBackPressed()}
     * function get executed.
     */
    public abstract void onClosed();


    @Override
    public void onPause() {
        super.onPause();
        isActivityRunning = false;
    }


    /**
     * Callback for the result from requesting permissions. This method
     * is invoked for every call on {@link #requestPermissions(String[], int)}.
     * <p>
     * <strong>Note:</strong> It is possible that the permissions request interaction
     * with the user is interrupted. In this case you will receive empty permissions
     * and results arrays which should be treated as a cancellation.
     * </p>
     * @param requestCode  The request code passed in {@link #requestPermissions(String[], int)}.
     * @param permissions  The requested permissions. Never null.
     * @param grantResults The grant results for the corresponding permissions
     *                     which is either {@link android.content.pm.PackageManager#PERMISSION_GRANTED}
     *                     or {@link android.content.pm.PackageManager#PERMISSION_DENIED}. Never null.
     * @see #requestPermissions(String[], int)
     */
    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String permissions[],
                                           @NonNull int[] grantResults) {
        switch (requestCode) {
            case USES_PERMISSIONS_REQUEST_CODE: {
                if ((grantResults.length > 0) && (grantResults[0] == PERMISSION_GRANTED)) {
                    String msg = getString(R.string.granting_app_permissions_msg);
                    showSimpleMessageBox(msg);

                } else {
                    String msg = getString(R.string.not_granting_app_permission_msg);
                    showSimpleMessageBox(msg, new OnButtonClick() {
                        @Override
                        public void onYesClick(MessageDialog messageDialog) {
                            super.onYesClick(messageDialog);
                            finish();
                        }
                    });
                }
            }
        }
    }


    public void showSimpleMessageBox(final String message) {
        AsyncJob.doInMainThread(new AsyncJob.MainThreadJob() {
            @Override
            public void doInUIThread() {
                new MessageDialog(BaseActivity.this)
                        .setMessage(message)
                        .setButtonName(getString(R.string.okay), null)
                        .show();
            }
        });
    }


    public void showSimpleMessageBox(final String message, final MessageDialog.OnClickButton callback) {
        AsyncJob.doInMainThread(new AsyncJob.MainThreadJob() {
            @Override
            public void doInUIThread() {
                new MessageDialog(BaseActivity.this)
                        .setMessage(message)
                        .setButtonName(getString(R.string.okay), null)
                        .setCallback(callback)
                        .show();
            }
        });
    }


    public void loadNewFullScreenAd() {
        if (isPremiumVersion) return;

        final AdRequest adRequest = new AdRequest.Builder()
                .addTestDevice(AdMobUnitIds.DEVICE_ID).build();
        if (fullscreenAd == null) {
            fullscreenAd = new InterstitialAd(this);
            fullscreenAd.setAdUnitId(AdMobUnitIds.FullScreenAdUnitId);
        }

        if (!fullscreenAd.isLoaded()) {
            fullscreenAd.loadAd(adRequest);
        }
    }


    public void showFullscreenAd() {
        if (isPremiumVersion) return;

        if (fullscreenAd != null) {
            if (fullscreenAd.isLoaded()) {
                fullscreenAd.show();
            }
        }
    }


    /**
     * The function returns the {@link App} object reference.
     */
    public App getApp() {
        return this.app;
    }


    /**
     * The function opens the play store app with the given package Id.
     * @param packageId the app's package id that the function will open in the play store.
     */
    public void openAppPageInPlayStore(String packageId) {
        String url = "market://details?id=" + packageId;
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
        startActivity(intent);
    }


    /**
     * The function open the google play store app with it's own parent app's package id.
     */
    public void openAppPageInPlayStore() {
        String url = "market://details?id=" + getPackageName();
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
        startActivity(intent);
    }


    /**
     * The function returns the {@link InputMethodManager} object reference.
     */
    public InputMethodManager getInputMethodManager() {
        return (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
    }


    /**
     * The function return the {@link Drawable} obeject with the given the drawable res id.
     */
    public Drawable getDrawableImage(int resId) {
        return getResources().getDrawable(resId);
    }


    /**
     * The function start the given activity class.
     * @param activityClass the activity class that need to start.
     */
    public void startActivity(Class activityClass) {
        startActivity(new Intent(this, activityClass));
    }


    /**
     * The function starts the given activity with the given animations.
     */
    public void startActivityWithAnimation(Class activityClass, int outAnim, int enterAnim) {
        startActivity(new Intent(this, activityClass));
        overridePendingTransition(enterAnim, outAnim);
    }


    /**
     * The function loads the banner ad and show them on the {@link AdView} of the activity's layout.
     */
    public void loadBannerAd() {
        //checks if the user already brought the premium version of the app.
        if (isPremiumVersion) {
            View adView = findViewById(R.id.adView);
            if (adView != null) {
                adView.setVisibility(View.GONE);
            }
        } else {
            final AdView adView = (AdView) findViewById(R.id.adView);
            final AdRequest adRequest = new AdRequest.Builder().build();
            if (adView != null) {
                adView.loadAd(adRequest);
                adView.setAdListener(new AdListener() {
                    @Override
                    public void onAdFailedToLoad(int errorCode) {
                        super.onAdFailedToLoad(errorCode);
                        new Timer(5000, 5000) {
                            @Override
                            public void onFinish() {
                                adView.loadAd(adRequest);
                            }
                        }.start();
                    }
                });
            }
        }
    }


    public void showSimpleMessageBox(final String title, final String message) {
        AsyncJob.doInMainThread(new AsyncJob.MainThreadJob() {
            @Override
            public void doInUIThread() {
                new MessageDialog(BaseActivity.this)
                        .setTitle(title)
                        .setMessage(message)
                        .setButtonName(getString(R.string.okay), null)
                        .show();
            }
        });
    }


    public void showSimpleMessageBox(final String title,
                                     final String message, final boolean isCancelable) {
        AsyncJob.doInMainThread(new AsyncJob.MainThreadJob() {
            @Override
            public void doInUIThread() {
                MessageDialog messageDialog = new MessageDialog(BaseActivity.this)
                        .setTitle(title)
                        .setMessage(message)
                        .setButtonName(getString(R.string.okay), null);
                messageDialog.dialog.setCancelable(isCancelable);
                messageDialog.dialog.show();
            }
        });
    }


    @TargetApi(Build.VERSION_CODES.N)
    public void showSimpleHtmlMessageBox(final String message) {
        AsyncJob.doInMainThread(new AsyncJob.MainThreadJob() {
            @Override
            public void doInUIThread() {
                Spanned msg;
                if (OsUtility.getBuildSdk() >= 24) {
                    msg = Html.fromHtml(message, Html.FROM_HTML_MODE_LEGACY);
                } else {
                    msg = Html.fromHtml(message);
                }

                if (msg != null) {
                    new MessageDialog(BaseActivity.this)
                            .setMessage(message)
                            .setButtonName(getString(R.string.okay), null)
                            .show();
                }
            }
        });
    }


    @TargetApi(Build.VERSION_CODES.N)
    public void showSimpleHtmlMessageBox(final String message,
                                         final MessageDialog.OnClickButton callback) {
        AsyncJob.doInMainThread(new AsyncJob.MainThreadJob() {
            @Override
            public void doInUIThread() {
                Spanned msg = null;
                if (OsUtility.getBuildSdk() >= 24) {
                    msg = Html.fromHtml(message, Html.FROM_HTML_MODE_LEGACY);
                } else {
                    msg = Html.fromHtml(message);
                }

                if (msg != null) {
                    new MessageDialog(BaseActivity.this)
                            .setMessage(message)
                            .setButtonName(getString(R.string.okay), null)
                            .setCallback(callback)
                            .show();
                }
            }
        });
    }


    public int getColorFrom(int resColorId) {
        return getResources().getColor(resColorId);
    }


    public void toast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }


    public void vibrate(int duration) {
        if (vibrator != null) {
            vibrator.vibrate(20);
        }
    }


    public void exitActivityOnDoublePress() {
        if (isBackPressEventFired == 0) {
            String msg = getString(R.string.press_back_once_more_to_exit);
            Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
            isBackPressEventFired = 1;
            new CountDownTimer(2000, 1000) {
                @Override
                public void onTick(long time) {
                }


                @Override
                public void onFinish() {
                    isBackPressEventFired = 0;
                }
            }.start();
        } else if (isBackPressEventFired == 1) {
            isBackPressEventFired = 0;
            finish();
        }
    }


    /**
     * The function close the app's entire process.
     */
    private void forceCloseApplication() {
        int pid = android.os.Process.myPid();
        android.os.Process.killProcess(pid);
        System.exit(0);
    }


    protected void askUserForPermissionsGranted(final String[] permissions) {
        String msg = getString(R.string.msg_app_permissions);
        MessageDialog message = new MessageDialog(this);
        message.setButtonName(getString(R.string.grant_permissions), getString(R.string.cancel));
        message.setMessage(msg);
        message.setCallback(new MessageDialog.OnClickButton() {
            @Override
            public void onYesClick(MessageDialog messageDialog) {
                ActivityCompat.requestPermissions(BaseActivity.this, permissions, USES_PERMISSIONS_REQUEST_CODE);
                messageDialog.dismiss();
            }


            @Override
            public void onNoClick(MessageDialog messageDialog) {
                messageDialog.dismiss();
                finish();
            }
        });
        message.show();
    }
}
