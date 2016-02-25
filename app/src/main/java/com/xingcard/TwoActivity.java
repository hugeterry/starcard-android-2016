package com.xingcard;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.xingcard.baymax.Baymax;
import com.xingcard.shequ.Shequ;
import com.yalantis.guillotine.animation.GuillotineAnimation;

/**
 * Created by Administrator on 2015/8/6 0006.
 */
public class TwoActivity extends Activity {
    private Toolbar toolbar; //标题栏
    private FrameLayout root; //布局
    private ImageView contentHamburger; //菜单按钮

    private LinearLayout feed_group;
    private LinearLayout profile_group;
    private LinearLayout activity_group;
    private LinearLayout setting_group;

    private View guillotineMenu;
    private GuillotineAnimation GuillotineAnimation;

    private Button bt01, bt02;

    private String str;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_two);
        Intent intent = getIntent();
        str = intent.getStringExtra("str");

        initviews();
        initGui();
    }

    private void initviews() {
        //镰刀菜单控件
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        root = (FrameLayout) findViewById(R.id.root);
        contentHamburger = (ImageView) findViewById(R.id.content_hamburger);

        bt01 = (Button) findViewById(R.id.bt01);
        bt02 = (Button) findViewById(R.id.bt02);

        bt01.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(TwoActivity.this, Baymax.class);
                startActivity(intent);
            }
        });
        bt02.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(TwoActivity.this, Shequ.class);
                startActivity(intent);
            }
        });

    }

    private void initGui() {


        //弹出的菜单
        guillotineMenu = getLayoutInflater().inflate(R.layout.guillotine, null);

        contentHamburger.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                // 添加弹出的菜单
                root.addView(guillotineMenu);

                GuillotineAnimation = new GuillotineAnimation.GuillotineBuilder(guillotineMenu, guillotineMenu.findViewById(R.id.guillotine_hamburger), contentHamburger, feed_group)
//                        .setStartDelay(RIPPLE_DURATION)
                        .setActionBarViewForAnimation(toolbar)
                        .build();
                GuillotineAnimation.setUpOpeningView(contentHamburger);

                GuillotineAnimation.open();

                initGui_menu();

            }

        });
    }


    private void initGui_menu() {
        feed_group = (LinearLayout) findViewById(R.id.feed_group);
        feed_group.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                GuillotineAnimation.close();
            }

        });

        profile_group = (LinearLayout) findViewById(R.id.profile_group);
        profile_group.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                Intent intent = new Intent(TwoActivity.this, MainActivity.class);
                intent.putExtra("str", str);
                startActivity(intent);
                overridePendingTransition(R.anim.fade, R.anim.hold);
                finish();

            }
        });
        activity_group = (LinearLayout) findViewById(R.id.activity_group);
        activity_group.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(TwoActivity.this, AddActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.fade, R.anim.hold);
            }
        });
        setting_group = (LinearLayout) findViewById(R.id.settings_group);
        setting_group.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(TwoActivity.this, AboutActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.fade, R.anim.hold);
            }
        });

    }


}
