package com.miracle.michael.mdgame.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;
import android.util.Log;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;

/**
 * Created by XXX on 2017/9/28.
 */

public class CookieUtils {

    private static final String COOKIE_SPF_NAME = "cookie_spf_name";

    public static void saveCookieToLocal(Context context, String cookie) {
        SharedPreferences contextSharedPreferences = context.getSharedPreferences(COOKIE_SPF_NAME, Context.MODE_PRIVATE);
        contextSharedPreferences.edit().putString("cookie", cookie).commit();
    }

    public static String getCookieFromLocal(Context context) {
        return context.getSharedPreferences(COOKIE_SPF_NAME, Context.MODE_PRIVATE).getString("cookie", "");
    }


    public static boolean syncCookie(Context context, String url, String cookie) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            CookieSyncManager.createInstance(context);
        }
        Log.i("cookie", "getCookie:" + cookie);
        CookieManager cookieManager = CookieManager.getInstance();
        String[] strings = cookie.split(";");
        if (strings.length > 0) {
            for (String string : strings) {
                cookieManager.setCookie(url, string);//如果没有特殊需求，这里只需要将session id以"key=value"形式作为cookie即可
            }
        }
        String newCookie = cookieManager.getCookie(url);
        if (cookie.equals(newCookie)) {
            Log.i("COOKIE", "true");
        } else {
            Log.i("COOKIE", "false");
        }
        return true;
    }

}
