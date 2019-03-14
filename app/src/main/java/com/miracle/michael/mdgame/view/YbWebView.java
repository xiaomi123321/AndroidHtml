package com.miracle.michael.mdgame.view;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.webkit.CookieManager;
import android.webkit.WebSettings;
import android.webkit.WebView;

import com.miracle.michael.mdgame.interfaces.LoadProgressImp;


/**
 * Created by cfv on 2017/5/19.
 */
public class YbWebView extends WebView {

    private final int WEB_LOAD_PROGRESS = 95;

    private LoadProgressImp loadProgressImp;

    private Activity mActivity;

    private YbWebViewClient mWebViewClient;

    public void setActivity(Activity activity, LoadProgressImp imp) {
        this.mActivity = activity;
        this.loadProgressImp = imp;
        this.setWebChromeClient(new MyChromeClient(mActivity, imp));
    }


    public YbWebView(Context context) {
        super(context);
        initWebView();
        initSetting(context);
    }

    public YbWebView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initWebView();
        initSetting(context);
    }

    public void setOnWebStateListener(OnWebStateListener listener) {
        if (mWebViewClient != null) {
            mWebViewClient.setOnWebStateListener(listener);
        }
    }

    public void setOnOverrideUrlLoadListener(OnOverrideUrlLoadListener listener) {
        if(mWebViewClient != null){
            mWebViewClient.setOnOverrideUrlLoadListener(listener);
        }
    }



    public YbWebView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initWebView();
        initSetting(context);
    }


    private void initWebView() {
        mWebViewClient = new YbWebViewClient(this);
        setAlwaysDrawnWithCacheEnabled(true);
        setAnimationCacheEnabled(true);
        setDrawingCacheBackgroundColor(0x00000000);
        setDrawingCacheEnabled(true);
        setWillNotCacheDrawing(false);
        setSaveEnabled(true);
        setBackground(null);
        getRootView().setBackground(null);
        setFocusable(true);
        setFocusableInTouchMode(true);
        setHorizontalScrollBarEnabled(false);
        setVerticalScrollBarEnabled(false);
        setScrollbarFadingEnabled(true);
        this.setWebViewClient(mWebViewClient);
    }

    private void initSetting(Context context) {
        CookieManager cookieManager = CookieManager.getInstance();
        cookieManager.setAcceptCookie(true);
        WebSettings webSettings = getSettings();
        webSettings.setUseWideViewPort(true);
        webSettings.setAllowContentAccess(true);
        webSettings.setAllowFileAccess(true);
        webSettings.setAllowFileAccessFromFileURLs(true);
        webSettings.setAllowUniversalAccessFromFileURLs(true);
        webSettings.setAppCacheEnabled(true);
        webSettings.setAppCachePath(context.getCacheDir().toString());
        webSettings.setDatabaseEnabled(true);
        webSettings.setCacheMode(WebSettings.LOAD_DEFAULT);
        webSettings.setSaveFormData(true);
        webSettings.setSavePassword(true);
        webSettings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
        webSettings.setLoadWithOverviewMode(true);
        webSettings.setDomStorageEnabled(true);

        webSettings.setJavaScriptEnabled(true);
//
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            webSettings.setLoadsImagesAutomatically(true);
        } else {
            webSettings.setLoadsImagesAutomatically(false);
        }

    }

    public class MyChromeClient extends android.webkit.WebChromeClient {
        private ProgressDialog dialog;

        private LoadProgressImp imp;

        public MyChromeClient(Activity context, LoadProgressImp imp) {
            this.imp = imp;
        }

        @Override
        public void onProgressChanged(WebView view, int newProgress) {
            if (newProgress > WEB_LOAD_PROGRESS) {
                imp.loadStart();
            } else {
                imp.loadFinish();
            }
            super.onProgressChanged(view, newProgress);
        }

        @Override
        public void onReceivedTitle(WebView view, String title) {
            super.onReceivedTitle(view, title);
        }

    }

    public interface OnWebStateListener {
        void onPageFinish(WebView view, String url);
    }

    public interface OnOverrideUrlLoadListener {
        boolean isIntercept(String url);

        void handleTransaction(WebView view, String url);
    }

}
