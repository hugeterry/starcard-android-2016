package com.xingcard;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.view.Window;


/**
 * Created by Administrator on 2015/8/13 0013.
 */
public class SplashActivity extends Activity {

    private Boolean bl = false;
    private String str;
    private SharedPreferences value_first;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_welcome);

        Context sp = SplashActivity.this;
        value_first = sp.getSharedPreferences("value_first", MODE_APPEND);
        bl = value_first.getBoolean("value_first", false);
        str = value_first.getString("url_name", "");
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (bl == false) {
                    Intent intent = new Intent(SplashActivity.this, LoginActivity.class);

                    startActivity(intent);
                    SplashActivity.this.finish();
                } else {
                    Intent intent = new Intent(SplashActivity.this, MainActivity.class);
                    intent.putExtra("str", str);
                    startActivity(intent);
                    SplashActivity.this.finish();
                }
            }
        }, 1000);
    }
}
