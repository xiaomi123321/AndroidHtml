package com.miracle.michael.mdgame.view;

import android.graphics.Bitmap;
import android.net.http.SslError;
import android.os.Environment;
import android.text.TextUtils;
import android.util.Log;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;
import android.webkit.SslErrorHandler;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;


import com.miracle.michael.mdgame.util.CookieUtils;

import java.io.FileOutputStream;

import static android.content.ContentValues.TAG;

/**
 * Created by cfv on 2017/5/19.
 */
public class YbWebViewClient extends WebViewClient {

    private YbWebView mWebView;

    private boolean isLogin;

    private boolean isSuccess;

    private YbWebView.OnWebStateListener onWebStateListener;

    private YbWebView.OnOverrideUrlLoadListener onOverrideUrlLoadListener;

    public YbWebViewClient(YbWebView webView) {
        this.mWebView = webView;
    }

    @Override
    public boolean shouldOverrideUrlLoading(WebView view, String url) {
        if (onOverrideUrlLoadListener != null) {
            if (onOverrideUrlLoadListener.isIntercept(url)) {
                onOverrideUrlLoadListener.handleTransaction(view, url);
                return true;
            }
        }
        return false;
    }

    public void setOnWebStateListener(YbWebView.OnWebStateListener onWebStateListener) {
        this.onWebStateListener = onWebStateListener;
    }

    public void setOnOverrideUrlLoadListener(YbWebView.OnOverrideUrlLoadListener listener) {
        this.onOverrideUrlLoadListener = listener;
    }

    @Override
    public void onPageFinished(WebView view, String url) {
        super.onPageFinished(view, url);
        Log.i("yrk", url);
        if (this.mWebView != null && !this.mWebView.getSettings().getLoadsImagesAutomatically()) {
            this.mWebView.getSettings().setLoadsImagesAutomatically(true);
        }

        String cookie = CookieManager.getInstance().getCookie(url);
        if (isLogin && cookie != null && cookie.contains("uid=") && cookie.contains("token=") && cookie.contains("account=")) {
            Log.i("cookie", cookie);
            CookieUtils.saveCookieToLocal(view.getContext(), cookie);
            CookieSyncManager.createInstance(view.getContext()).sync();
        } else if (!TextUtils.isEmpty(CookieUtils.getCookieFromLocal(view.getContext()))) {
            CookieUtils.syncCookie(view.getContext(), url, cookie);
        } else {
            CookieUtils.saveCookieToLocal(view.getContext(), "");
        }
//        if (this.onWebStateListener != null) {
//            this.onWebStateListener.onPageFinish(view, url);
//        }
        if (url.contains("login")) {
            isLogin = true;
        } else {
            isLogin = false;
        }
//        if (this.onWebStateListener != null) {
//            this.onWebStateListener.onPageFinish(view, "error");
//        }
//        save(view.getDrawingCache());
    }

    @Override
    public void onPageStarted(WebView view, String url, Bitmap favicon) {
        String cookieFromurl = CookieManager.getInstance().getCookie(url);
        Log.i("cookie", "cookieFromUrl:" + cookieFromurl);
        String cookie = CookieUtils.getCookieFromLocal(view.getContext());
        if (!TextUtils.isEmpty(cookie) && cookie != null && cookie.contains("uid=") && cookie.contains("token=") && cookie.contains("account=")) {
            CookieUtils.syncCookie(view.getContext(), url, cookie);
            CookieSyncManager.getInstance().sync();
        }
        super.onPageStarted(view, url, favicon);
    }

    @Override
    public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
        handler.proceed();
    }

    @Override
    public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
        if (this.onWebStateListener != null) {
            view.clearView();
            this.onWebStateListener.onPageFinish(view, "error");
        }
        //        super.onReceivedError(view, request, error);
    }


    public void save(Bitmap bitmap) {
        try {
            String fileName = Environment.getExternalStorageDirectory().getPath() + "/webview_capture" + System.currentTimeMillis() + ".jpg";
            FileOutputStream fos = new FileOutputStream(fileName);
            //压缩bitmap到输出流中
            bitmap.compress(Bitmap.CompressFormat.JPEG, 70, fos);
            fos.close();
        } catch (Exception e) {
            Log.e(TAG, e.getMessage());
        } finally {
            if (bitmap != null) {
                bitmap.recycle();
            }
        }
    }

}
