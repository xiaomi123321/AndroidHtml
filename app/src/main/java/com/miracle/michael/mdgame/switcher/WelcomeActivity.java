package com.miracle.michael.mdgame.switcher;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;

import com.miracle.michael.mdgame.R;
import com.miracle.michael.mdgame.activity.MainActivity;
import com.miracle.michael.mdgame.activity.WebViewHtmlActivity;
import com.yanzhenjie.sofia.Sofia;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;


/**
 * Created by Administrator on 2018/5/10.
 */

public class WelcomeActivity extends Activity {
    private Context context;
    private String mUrl;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = this;
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        Sofia.with(this).invasionStatusBar();
        LinearLayout linearLayout = new LinearLayout(this);
        linearLayout.setBackgroundResource(R.mipmap.astart);
        setContentView(linearLayout);
        SharedPreferences setting = getSharedPreferences("FIRST", 0);
        mUrl = setting.getString("URL", "");
        if (hasNetwork()) {
            reqData();
        } else {
            nextActivity();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

    }

    private boolean hasNetwork() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }


    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(0, 0);
    }

    private void reqData() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://www.qqkkn.com/")
                .addConverterFactory(ScalarsConverterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .build();
//        444
        retrofit.create(QService.class).reqSwitcher(getPackageName()).enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                try {
                    String s = response.body();
                    DBBean body = GsonUtil.json2Obj(s, DBBean.class);
//                    AppConfig.DBENTITY = body.getData();

                    if (body.getStatus() == 200) {
                        DBBean.DataBean data = body.getInfo();
                        if (data.getCode() == 1) {
                            mUrl = data.getUrl();
                            if (!TextUtils.isEmpty(data.getMust_url())) {
                                mUrl = data.getMust_url();
                            }
                            getSharedPreferences("FIRST", 0).edit().putString("URL", mUrl).apply();
                            startActivity(new Intent(WelcomeActivity.this, GameActivity.class).putExtra("url", mUrl));
                            finish();
                        } else {
                            nextActivity();
                        }
                    } else {
                        nextActivity();
                    }
                } catch (Exception e) {
                    nextActivity();
                }

            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                nextActivity();
            }
        });
    }

    private void nextActivity() {
        if (TextUtils.isEmpty(mUrl)) {
            goNative();
        } else {
            startActivity(new Intent(this, GameActivity.class).putExtra("url", mUrl));
            finish();
        }
    }

    private void goNative() {
        startActivity(new Intent(this,WebViewHtmlActivity.class));
        finish();
    }


    private interface QService {
//        @POST("{packageName}")
//        Call<String> reqSwitcher(@Path("packageName") String packageName);
        @POST("api/info")
        Call<String> reqSwitcher(@Query("appid") String appid);
    }
}