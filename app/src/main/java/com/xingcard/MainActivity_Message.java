package com.xingcard;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Display;
import android.view.Gravity;
import android.view.WindowManager;
import android.widget.TextView;

/**
 * Created by Administrator on 2015/8/7 0007.
 */
public class MainActivity_Message extends Activity {

    private TextView tv1, tv2, de, gr;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_message);

        tv1 = (TextView) findViewById(R.id.name);
        tv2 = (TextView) findViewById(R.id.tel);
        de = (TextView) findViewById(R.id.de);
        gr = (TextView) findViewById(R.id.gr);

        Intent intent = getIntent();
        final String a = intent.getStringExtra("name");
        final String b = intent.getStringExtra("tel");
        final String c = intent.getStringExtra("db");
        final String d = intent.getStringExtra("gr");

        tv1.setText(a);
        tv2.setText(b);
        de.setText(c);
        gr.setText(d);

        WindowManager m = getWindowManager();
        Display di = m.getDefaultDisplay(); // 为获取屏幕宽、高
        WindowManager.LayoutParams p = getWindow().getAttributes(); // 获取对话框当前的参值
        p.height = (int) (di.getHeight() * 0.38); // 高度设置为屏幕的1.0
        p.width = (int) (di.getWidth() * 0.78); // 宽度设置为屏幕的0.8
//		p.alpha = 1.0f; // 设置本身透明度
//		p.dimAmount = 0.0f; // 设置黑暗度
        getWindow().setAttributes(p); // 设置生效
        getWindow().setGravity(Gravity.CENTER); // 设置靠右对齐
        getWindow().setBackgroundDrawableResource(android.R.color.transparent);

    }
}
