package utils;

import android.Manifest;
import android.accounts.Account;
import android.accounts.AccountManager;
import android.content.Context;
import android.util.Log;

import core.App;

/**
 * This class uses the AccountManager to get the primary email address of the
 * current user.
 */
@SuppressWarnings({"MissingPermission", "unused"})
public class UserEmailFetcher {

    public static String[] getEmail(Context context) {
        try {
            AccountManager accountManager = AccountManager.get(context);
            Account account = getAccount(context, accountManager);

            if (account == null) {
                return null;
            } else {

                return new String[]{account.type, account.name, account.toString()};
            }
        } catch (Exception error) {
            error.printStackTrace();
            return null;
        }
    }


    private static Account getAccount(Context context, AccountManager accountManager) {
        if (App.isPermissionGranted(context, Manifest.permission.GET_ACCOUNTS)) {
            Account[] accounts = accountManager.getAccountsByType("com.google");
            Account account;
            if (accounts.length > 0) {
                account = accounts[0];
            } else {
                account = null;
            }
            return account;
        } else {
            Log.e(context.getPackageName(), "Need Permission to run this function : " + Manifest.permission.GET_ACCOUNTS);
            return null;
        }
    }


    public static String[] getAllRegisteredEmailAddress(Context context) {
        String[] emails = null;
        try {
            if (App.isPermissionGranted(context, Manifest.permission.GET_ACCOUNTS)) {
                Account[] accounts = AccountManager.get(context).getAccounts();
                emails = new String[accounts.length];
                for (int i = 0; i < accounts.length; i++) {
                    emails[i] = accounts[i].name;
                }
            }

        } catch (Throwable err) {
            err.printStackTrace();
            Log.e(context.getPackageName(), "Need Permission to run this function : " + Manifest.permission.GET_ACCOUNTS);
        }

        return emails;
    }
}
