package com.miracle.michael.mdgame.activity;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.miracle.michael.mdgame.R;
import com.miracle.michael.mdgame.util.NetStatusUtil;
import com.miracle.michael.mdgame.util.ToastUtil;
import com.miracle.michael.mdgame.view.Html5WebView;

public class MainActivity extends Activity {

    private Html5WebView webView;
    private TextView mTextView;
    private ProgressDialog loadingDialog;
//        private String mUrl = "http://play.7724.com/olgames/migonglianren/?from=wap";
//    private String mUrl = "http://wap.go007.com/dali/"; //15509841941  qwe123456  生活类的
//    private String mUrl = "http://www.toodaylab.com/"; //
//    private String mUrl = "http://www.fububu.com/"; //
    private String mUrl = "https://m.jd.com/";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);

        LinearLayout linearLayout = new LinearLayout(this);
        linearLayout.setOrientation(LinearLayout.VERTICAL);
        linearLayout.setLayoutParams(params);
        linearLayout.setBackgroundResource(R.color.mf5f5f5);
        mTextView = new TextView(this);
        mTextView.setLayoutParams(params);
        mTextView.setGravity(Gravity.CENTER);
        mTextView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);
//        mTextView.setTextColor(Color.BLACK);
        mTextView.setTextColor(Color.parseColor("#333333"));
        mTextView.setVisibility(View.GONE);
        mTextView.setText("加载失败！点击重试。");
        mTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadUrl();
            }
        });
        linearLayout.addView(mTextView);

        webView = new Html5WebView(this);
        loadingDialog=new ProgressDialog(this);
        loadingDialog.setMessage("加载中...");
        webView.setProgressDialog(loadingDialog);
        webView.setWebViewClient(new MyWebViewClient());
        webView.setWebChromeClient(new MyChromeClient());


        WebSettings s = webView.getSettings();
        s.setBuiltInZoomControls(false);
        s.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
        s.setUseWideViewPort(true);
        s.setLoadWithOverviewMode(true);
        s.setSavePassword(true);
        s.setSaveFormData(true);
        s.setJavaScriptEnabled(true);
        s.setGeolocationEnabled(true);
        s.setGeolocationDatabasePath("/data/data/org.itri.html5webview/databases/");
        s.setDomStorageEnabled(true);

        s.setAllowContentAccess(true);
        s.setAllowFileAccess(true);
        s.setAllowFileAccessFromFileURLs(true);
        s.setAllowUniversalAccessFromFileURLs(true);
        s.setAppCacheEnabled(true);
//        s.setAppCachePath(context.getCacheDir().toString());
        s.setDatabaseEnabled(true);
        s.setCacheMode(WebSettings.LOAD_DEFAULT);

        webView.requestFocus();
        webView.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);


        linearLayout.addView(webView);
        setContentView(linearLayout);
//        setContentView(webView);
        loadingDialog.show();
        webView.loadUrl(mUrl);

    }

    private void loadUrl() {
        loadingDialog.show();
        mTextView.setVisibility(View.GONE);
        webView.setVisibility(View.GONE);
        webView.loadUrl(mUrl);
        if (!NetStatusUtil.isConnected(this)) {
            ToastUtil.toast("请检查当前网络！");
        }
    }

    private final class MyWebViewClient extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {

            view.loadUrl(url);
            if (loadingDialog != null){
                loadingDialog.show();
            }

            //唤起微信
            if (url.startsWith("weixin://wap/pay?")) {
                try {
                    Uri u = Uri.parse(url);
                    Intent intent = new Intent(Intent.ACTION_VIEW, u);
                    startActivity(intent);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return true;
            }
            //唤起QQ
            if (url.startsWith("mqqapi://forward/url?")) {
                try {
                    Uri u = Uri.parse(url);
                    Intent intent = new Intent(Intent.ACTION_VIEW, u);
                    startActivity(intent);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return true;
            }

            //唤起支付宝
            if (url.startsWith("intent://") || url.startsWith("alipays://")) {
                try {
                    Intent intent = Intent.parseUri(url,
                            Intent.URI_INTENT_SCHEME);
                    intent.addCategory("android.intent.category.BROWSABLE");
                    intent.setComponent(null);
                    startActivity(intent);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return true;
            }
            if (url.endsWith(".apk")) {
                view.getContext().startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(url)));
                return true;
            }
            return super.shouldOverrideUrlLoading(view, url);
        }

        /**
         * 这里进行无网络或错误处理，具体可以根据errorCode的值进行判断，做跟详细的处理。
         *
         * @param view
         */
        // 旧版本，会在新版本中也可能被调用，所以加上一个判断，防止重复显示
        @Override
        public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
            super.onReceivedError(view, errorCode, description, failingUrl);
            //Log.e(TAG, "onReceivedError: ----url:" + error.getDescription());
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                return;
            }
            // 在这里显示自定义错误页
            showError();
        }

        // 新版本，只会在Android6及以上调用
        @TargetApi(Build.VERSION_CODES.M)
        @Override
        public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
            super.onReceivedError(view, request, error);
            if (request.isForMainFrame()) { // 或者： if(request.getUrl().toString() .equals(getUrl()))
                // 在这里显示自定义错误页
                showError();
            }
        }
    }

    private final class MyChromeClient extends WebChromeClient {
        // For Android >=3.0
        public void openFileChooser(ValueCallback<Uri> uploadMsg, String acceptType) {
//            if (acceptType.equals("image/*")) {
//                if (mUploadMessage != null) {
//                    mUploadMessage.onReceiveValue(null);
//                    return;
//                }
//                mUploadMessage = uploadMsg;
//                selectImage();
//            } else {
//                onReceiveValue();
//            }
        }

        // For Android < 3.0
        public void openFileChooser(ValueCallback<Uri> uploadMsg) {
            openFileChooser(uploadMsg, "image/*");
        }

        // For Android  >= 4.1.1
        public void openFileChooser(ValueCallback<Uri> uploadMsg, String acceptType, String capture) {
            openFileChooser(uploadMsg, acceptType);
        }

        // For Android  >= 5.0
        @Override
        @SuppressLint("NewApi")
        public boolean onShowFileChooser(WebView webView, ValueCallback<Uri[]> filePathCallback, FileChooserParams fileChooserParams) {
//            if (fileChooserParams != null && fileChooserParams.getAcceptTypes() != null
//                    && fileChooserParams.getAcceptTypes().length > 0 && fileChooserParams.getAcceptTypes()[0].equals("image/*")) {
//                if (mUploadMessageArray != null) {
//                    mUploadMessageArray.onReceiveValue(null);
//                }
//                mUploadMessageArray = filePathCallback;
//                selectImage();
//            } else {
//                onReceiveValue();
//            }
            return true;
        }

        @Override
        public void onProgressChanged(WebView view, int newProgress) {
            super.onProgressChanged(view, newProgress);
            if (newProgress == 100) {
                webView.setVisibility(View.VISIBLE);
                loadingDialog.dismiss();
            }
        }
    }

    private void showError() {
        webView.setVisibility(View.GONE);
        mTextView.setVisibility(View.VISIBLE);
    }


    private long mOldTime;

    /**
     * 点击“返回键”，返回上一层
     * 双击“返回键”，返回到最开始进来时的网页
     */
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (System.currentTimeMillis() - mOldTime < 1500) {
                webView.clearHistory();
                webView.loadUrl(mUrl);
            } else if (webView.canGoBack()) {
                webView.goBack();
            } else {
                finish();
            }
            mOldTime = System.currentTimeMillis();
            return true;
        }

        return super.onKeyDown(keyCode, event);
    }

    @Override
    protected void onDestroy() {
        // 销毁 WebView
        if (webView != null) {
            webView.loadDataWithBaseURL(null, "", "text/html", "utf-8", null);
            webView.clearHistory();

            ((ViewGroup) webView.getParent()).removeView(webView);
            webView.destroy();
            webView = null;
        }
        super.onDestroy();
    }

}
