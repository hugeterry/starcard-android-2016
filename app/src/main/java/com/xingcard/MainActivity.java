package com.xingcard;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.sevenheaven.segmentcontrol.SegmentControl;
import com.xingcard.address.HttpJson;
import com.xingcard.address.httpUtils.Depart;
import com.xingcard.address.httpUtils.PersonInfo;
import com.xingcard.address.ui.ClearEditText;
import com.xingcard.address.ui.PinnedHeaderExpandableListView;
import com.xingcard.address.ui.StickyLayout;
import com.xingcard.update.DownLoadDialog;
import com.xingcard.update.Update;
import com.xingcard.update.UpdateDialog;
import com.yalantis.guillotine.animation.GuillotineAnimation;

import java.util.ArrayList;
import java.util.List;


public class MainActivity extends AppCompatActivity {

    private static final long RIPPLE_DURATION = 250;

    private Handler handler = new Handler() {
    };
    private Handler handler1 = new Handler() {
    };
    private String utf8;
    //获取镰刀菜单控件
    private Toolbar toolbar; //标题栏
    private FrameLayout root; //布局
    private ImageView contentHamburger; //菜单按钮

    private SegmentControl seg;

    private LinearLayout feed_group;
    private LinearLayout profile_group;
    private LinearLayout activity_group;
    private LinearLayout setting_group;
    private View guillotineMenu;
    private GuillotineAnimation GuillotineAnimation;

    private SharedPreferences datas_departs;
    private SharedPreferences datas_person;
    private SharedPreferences value_first;
    private SharedPreferences sh_updateurl;

    //    获取通讯录控件
    private PinnedHeaderExpandableListView expandableListView;
    private StickyLayout stickyLayout;

    private ArrayList<Depart> departs;
    private ArrayList<List<PersonInfo>> pers;

    private Context context;

    private TextView tv01;

    private ClearEditText myClearEt;

    private String str;

    private HttpJson.MyexpandableListAdapter adapter;
    int now = 0;
    String url = "http://xingkongus.duapp.com/index.php/User/loginAPP";

    private String version = "1.5";//更新版本修改此处

    private String apkUrl = "";

    Handler up_handler = new Handler() {
        public void handleMessage(Message msg) {
            int i;
            i = msg.arg1;
            switch (i) {
                case 1:
                    // 弹出提示更新对话框
                    checkUpdateInfo();
                    break;
                default:
                    break;
            }

        }
    };


    class MyRunnable_update implements Runnable {

        @Override
        public void run() {
            // 检测更新
            Update update = new Update();
            update.start();

            Message msg = new Message();
            try {
                update.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            apkUrl = update.up_url;

            sh_updateurl = getSharedPreferences("sh_updateurl", MODE_APPEND);
            SharedPreferences.Editor up = sh_updateurl.edit();
            up.putString("sh_updateurl", apkUrl);
            up.commit();

            System.out.println("apkUrl11111111111111111" + apkUrl);
            if (update.version == null) {
                System.out.println("无联网，不更新");
                msg.arg1 = 2;
                up_handler.sendMessage(msg);
            } else if (!update.version.equals(version)) {
                System.out.println("需更新版本");
                msg.arg1 = 1;
                up_handler.sendMessage(msg);
            } else {
                System.out.println("版本已是最新");
                msg.arg1 = 2;
                up_handler.sendMessage(msg);
            }
        }

    }

    class MyRunnable implements Runnable {
        @Override
        public void run() {
            Looper.prepare();
            new HttpJson(url, adapter, context, tv01, expandableListView, stickyLayout, now, str).no_first_url();
            Looper.loop();
        }
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Intent intent = getIntent();
        str = intent.getStringExtra("str");

        initviews();
        initGui();

        new HttpJson(url, adapter, context, tv01, expandableListView, stickyLayout, now, str).start();

        Thread thread = new Thread(new MyRunnable());
        thread.start();

        Thread thread_update = new Thread(new MyRunnable_update());
        thread_update.start();

        search();
    }

    private void search() {

        myClearEt = (ClearEditText) this.findViewById(R.id.filter_edit);

        myClearEt.addTextChangedListener(new TextWatcher() {

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {
                // TODO Auto-generated method stub

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before,
                                      int count) {
                // TODO Auto-generated method stub
                new HttpJson(url, adapter, context, tv01, expandableListView, stickyLayout, now, str).filterData(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {
                // TODO Auto-generated method stub

            }
        });
    }


    private void initGui() {
        Toolbar.OnMenuItemClickListener onMenuItemClick = new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {

                switch (menuItem.getItemId()) {
                    case R.id.action_settings:
                        datas_departs = context.getSharedPreferences("datas_departs", MODE_APPEND);
                        SharedPreferences.Editor edit01 = datas_departs.edit();
                        edit01.clear();
                        edit01.commit();
                        datas_person = context.getSharedPreferences("datas_person", MODE_APPEND);
                        SharedPreferences.Editor edit02 = datas_person.edit();
                        edit02.clear();
                        edit02.commit();
                        value_first = context.getSharedPreferences("value_first", MODE_APPEND);
                        SharedPreferences.Editor edit03 = value_first.edit();
                        edit03.clear();
                        edit03.commit();
                        Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                        context.startActivity(intent);
                        finish();
                        break;
                }

                return true;
            }
        };

        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(null);

        toolbar.setOnMenuItemClickListener(onMenuItemClick);
        //弹出的菜单
        guillotineMenu = getLayoutInflater().inflate(R.layout.guillotine, null);

        contentHamburger.setOnClickListener(new OnClickListener() {
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
        feed_group.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View arg0) {
//                GuillotineAnimation.close();
                Intent intent = new Intent(MainActivity.this, TwoActivity.class);
                intent.putExtra("str", str);
                startActivity(intent);
                overridePendingTransition(R.anim.fade, R.anim.hold);
                finish();
            }

        });

        profile_group = (LinearLayout) findViewById(R.id.profile_group);
        profile_group.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View arg0) {
                GuillotineAnimation.close();
            }

        });
        activity_group = (LinearLayout) findViewById(R.id.activity_group);
        activity_group.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, AddActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.fade, R.anim.hold);
            }
        });
        setting_group = (LinearLayout) findViewById(R.id.settings_group);
        setting_group.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, AboutActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.fade, R.anim.hold);
            }
        });
    }


    private void initviews() {
        context = MainActivity.this;
        //镰刀菜单控件
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        root = (FrameLayout) findViewById(R.id.root);
        contentHamburger = (ImageView) findViewById(R.id.content_hamburger);

        pers = new ArrayList<List<PersonInfo>>();
        departs = new ArrayList<Depart>();

        tv01 = (TextView) findViewById(R.id.tv01);

        //联系人首页控件
        expandableListView = (PinnedHeaderExpandableListView) findViewById(R.id.expandablelist);
        stickyLayout = (StickyLayout) findViewById(R.id.sticky_layout);
        //标签选择控件
        seg = (SegmentControl) findViewById(R.id.seg);
        seg.setOnSegmentControlClickListener(new SegmentControl.OnSegmentControlClickListener() {
            @Override
            public void onSegmentControlClick(int index) {
                if (index == 0) {
                    now = 0;
//                    new HttpJson(url, adapter, context, tv01, expandableListView, stickyLayout, now, str).change();
                    String s = myClearEt.getText().toString();
                    new HttpJson(url, adapter, context, tv01, expandableListView, stickyLayout, now, str).filterData(s);
                } else if (index == 1) now = 1;
                {
//                    new HttpJson(url, adapter, context, tv01, expandableListView, stickyLayout, now, str).change();
                    String s = myClearEt.getText().toString();
                    System.out.println("sssssssssssssssssssssssss" + s);
                    new HttpJson(url, adapter, context, tv01, expandableListView, stickyLayout, now, str).filterData(s);
                }
            }
        });
    }

    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    // 外部接口让主Activity调用
    public void checkUpdateInfo() {
        showNoticeDialog();
    }

    private void showNoticeDialog() {
        Intent intent = new Intent();
        intent.setClass(MainActivity.this, UpdateDialog.class);
        startActivityForResult(intent, 100);
    }

    private void showDownloadDialog() {
        Intent intent = new Intent();
        intent.setClass(MainActivity.this, DownLoadDialog.class);
        System.out.println(apkUrl);
        startActivityForResult(intent, 0);
    }

    // 获取对话框的返回值
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (resultCode) {
            case 2:
                showDownloadDialog();
                break;
            case 3:
                break;
            default:
                break;
        }
    }
}
