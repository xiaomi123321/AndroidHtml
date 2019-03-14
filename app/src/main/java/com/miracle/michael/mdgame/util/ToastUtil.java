package com.miracle.michael.mdgame.util;

import android.content.Context;
import android.text.TextUtils;
import android.view.Gravity;
import android.widget.Toast;

import com.miracle.michael.mdgame.base.Zapp;

/**
 * <p/>
 * Description: Toast工具类
 */

@SuppressWarnings("unused")
public class ToastUtil {
    /**
     * 禁止重复提示
     */
    private static final long Interval = 3 * 1000;
    /**
     * 消息软引用
     */
    private static final SoftHashMap<String, Long> map = new SoftHashMap<>();
    /**
     * Toast 对象
     */
    private static Toast toast;

    public static void toast(String msg) {
        toast(Zapp.getApp(), msg);
    }

    public static void toast(int id) {
        toast(Zapp.getApp(), Zapp.getApp().getString(id));
    }

    public static void toast(Context context, int msg) {
        toast(context, Zapp.getApp().getString(msg));
    }

    public static void toast(Context context, String msg) {
        if (!TextUtils.isEmpty(msg)) {
            long preTime = 0;
            if (map.containsKey(msg)) {
                preTime = map.get(msg);
            }
            final long now = System.currentTimeMillis();
            if (now >= preTime + Interval) {
                if (toast != null) {
                    toast.cancel();
                }
                if (context != null) {
                    Toast toast = Toast.makeText(context, msg, Toast.LENGTH_SHORT);
                    toast.setGravity(Gravity.CENTER_VERTICAL, 0, 0);
                    toast.show();
                    map.put(msg, now);
                    ToastUtil.toast = toast;
                }
            }
        }
    }
}
