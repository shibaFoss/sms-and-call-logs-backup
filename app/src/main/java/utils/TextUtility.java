package utils;

import android.util.Patterns;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;

/**
 * The class provides some useful functions on text processing tasks.
 */
@SuppressWarnings("unused")
public class TextUtility {

    public static String[] extractLinks(String text) {
        List<String> links = new ArrayList<>();
        Matcher matcher = Patterns.WEB_URL.matcher(text);
        while (matcher.find()) {
            String url = matcher.group();
            links.add(url);
        }

        return links.toArray(new String[links.size()]);
    }


    public static String capitalize(String string) {
        if (string == null || string.length() == 0) {
            return "";
        }
        char first = string.charAt(0);
        if (Character.isUpperCase(first)) {
            return string;
        } else {
            return Character.toUpperCase(first) + string.substring(1);
        }
    }
}
