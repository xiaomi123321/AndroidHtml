package com.miracle.michael.mdgame.util;

import android.content.Context;
import android.content.Intent;

import com.miracle.michael.mdgame.activity.MainActivity;
import com.miracle.michael.mdgame.activity.WebViewActivity;
import com.miracle.michael.mdgame.activity.WebViewHtmlActivity;
import com.miracle.michael.mdgame.switcher.GameActivity;

public class OpenActivity {

    public static void openGameActivity(Context context, String url) {
        context.startActivity(new Intent(context, GameActivity.class).putExtra("url", url));
    }

    public static void openMainActivity(Context context) {
        context.startActivity(new Intent(context, WebViewHtmlActivity.class));
//        context.startActivity(new Intent(context, WebViewActivity.class));
    }
}
