package utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiManager;
import android.webkit.URLUtil;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;

@SuppressWarnings("unused")
public class NetworkUtils {

    public static boolean isNetworkAvailable(Context context) {
        ConnectivityManager connectivity = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivity.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }


    public static boolean isWifiEnabled(Context context) {
        WifiManager wifi = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
        return wifi.isWifiEnabled();
    }


    public static String getFileNameFromUrl(String url) {
        return URLUtil.guessFileName(url, null, null);
    }


    public static long getFileSize(URL url) {
        HttpURLConnection connection = null;
        try {
            connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.connect();
            return connection.getContentLength();
        } catch (Exception error) {
            error.printStackTrace();
            return -1;
        } finally {
            assert connection != null;
            connection.disconnect();
        }
    }


    public static String getOriginalRedirectedUrl(URL url) throws IOException {
        String redirectedUrl = "-1";
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.connect();
        connection.setInstanceFollowRedirects(false);
        int responseCode = connection.getResponseCode();
        if ((responseCode / 100) == 3) {
            redirectedUrl = connection.getHeaderField("Location");
        }

        return redirectedUrl;
    }


    public static boolean isResumeWithPartDownloadSupport(URL url, long totalFileSize) {
        HttpURLConnection connection = null;
        try {
            connection = (HttpURLConnection) url.openConnection();
            String range = "bytes=" + 0 + "-" + (totalFileSize / 2);
            connection.setRequestProperty("Range", range);
            connection.connect();
            if (connection.getResponseCode() == 206)
                return true;
        } catch (IOException e) {
            return false;
        } finally {
            assert connection != null;
            connection.disconnect();
        }
        return false;
    }

}
