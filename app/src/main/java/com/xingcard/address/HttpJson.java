package com.xingcard.address;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Handler;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.BaseExpandableListAdapter;
import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.TextView;

import com.indris.material.RippleView;
import com.xingcard.LoginActivity;
import com.xingcard.MainActivity_Message;
import com.xingcard.R;
import com.xingcard.address.httpUtils.Depart;
import com.xingcard.address.httpUtils.PersonInfo;
import com.xingcard.address.search.CharacterParser;
import com.xingcard.address.ui.PinnedHeaderExpandableListView;
import com.xingcard.address.ui.StickyLayout;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2015/8/12 0012.
 */
public class HttpJson extends Thread implements
        ExpandableListView.OnChildClickListener,
        ExpandableListView.OnGroupClickListener, PinnedHeaderExpandableListView.OnHeaderUpdateListener,
        StickyLayout.OnGiveUpTouchEventListener {

    private static final long RIPPLE_DURATION = 250;

    private Handler handler = new Handler() {
    };
    private String utf8;
    private ArrayList<Depart> departs;
    private ArrayList<List<PersonInfo>> pers;

    private SharedPreferences datas_departs;
    private SharedPreferences datas_person;
    private SharedPreferences value_first;

    public static final int MODE_APPEND = 32768;
    private String url;
    private MyexpandableListAdapter adapter;
    private Context context;
    private PinnedHeaderExpandableListView expandableListView;
    private StickyLayout stickyLayout;

    private TextView textView;

    private String db;

    private Boolean bl = true;

    private String str;

    /**
     * 汉字转换成拼音的类
     */
    private CharacterParser characterParser;


    int now = 0;

    public HttpJson(String url, MyexpandableListAdapter adapter,
                    Context context, TextView textView, PinnedHeaderExpandableListView expandableListView,
                    StickyLayout stickyLayout, int now, String str) {
        this.url = url;
        this.adapter = adapter;
        this.context = context;
        this.textView = textView;
        this.expandableListView = expandableListView;
        this.stickyLayout = stickyLayout;
        this.now = now;
        this.str = str;
    }

    public void run() {
        value_first = context.getSharedPreferences("value_first", MODE_APPEND);
        bl = value_first.getBoolean("value_first", false);
        if (bl == false) {//第一次使用
            first_url();
        } else {//非第一次
            System.out.println("ffffffffffffffffffffffffffffff");
            handler.post(new Runnable() {
                @Override
                public void run() {
                    departs = sh_depaterment();
                    pers = sh_person();
                    adapter = new MyexpandableListAdapter(context);
                    expandableListView.setAdapter(adapter);
                    expandableListView.setOnHeaderUpdateListener(HttpJson.this);
                    expandableListView.setOnChildClickListener(HttpJson.this);
                    expandableListView.setOnGroupClickListener(HttpJson.this);
                    stickyLayout.setOnGiveUpTouchEventListener(HttpJson.this);
//                  no_first_url();
                }
            });
//            no_first_url();
        }
    }

    public void change() {
        //更改长短号标签引用方法
        handler.post(new Runnable() {
            @Override
            public void run() {
                departs = sh_depaterment();
                pers = sh_person();
                adapter = new MyexpandableListAdapter(context);
                expandableListView.setAdapter(adapter);
                expandableListView.setOnHeaderUpdateListener(HttpJson.this);
                expandableListView.setOnChildClickListener(HttpJson.this);
                expandableListView.setOnGroupClickListener(HttpJson.this);
                stickyLayout.setOnGiveUpTouchEventListener(HttpJson.this);
            }
        });
    }

    public void no_first_url() {
        try {
            URL httpUrl = new URL(url);

            HttpURLConnection conn = (HttpURLConnection) httpUrl
                    .openConnection();
            conn.setRequestMethod("POST");
            conn.setReadTimeout(3000);
            conn.setDoOutput(true);

            OutputStream out = conn.getOutputStream();
            final String content = "key=" + 82015 + "&name=" + str;
            out.write(content.getBytes());
//              System.out.println("res="+responsecode);
            if (conn.getResponseCode() == 200) {
                BufferedReader reader = new BufferedReader(new InputStreamReader(
                        conn.getInputStream()));
                StringBuffer sb = new StringBuffer();
                String str;

                while ((str = reader.readLine()) != null) {
                    sb.append(str);
                }
                utf8 = new String(sb.toString().getBytes(), "utf-8");
                System.out.println("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~" + utf8);
                JSONObject object = new JSONObject(utf8);
                int result = object.getInt("result");
                if (result == 200) {
                } else {
                    departs = parseJson_depaterment(utf8.toString());
                    pers = parseJson_person(utf8.toString());
                }
            }
        } catch (MalformedURLException e) {
            // TODO Auto-generated catch block
            System.out.println("ssssssssssssssssssssssssss2");
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            System.out.println("网络不通畅呦");
            e.printStackTrace();
        } catch (JSONException e) {
            System.out.println("别盗用我们接口呦，您的数据已经上报至服务器~~~");
            e.printStackTrace();
        }
    }

    private void first_url() {
        try {
            URL httpUrl = new URL(url);

            HttpURLConnection conn = (HttpURLConnection) httpUrl
                    .openConnection();
            conn.setRequestMethod("POST");
            conn.setReadTimeout(3000);
            conn.setDoOutput(true);

            OutputStream out = conn.getOutputStream();
            final String content = "key=" + 82015 + "&name=" + str;
            out.write(content.getBytes());
//              System.out.println("res="+responsecode);
            if (conn.getResponseCode() == 200) {
                BufferedReader reader = new BufferedReader(new InputStreamReader(
                        conn.getInputStream()));
                StringBuffer sb = new StringBuffer();
                String str;

                while ((str = reader.readLine()) != null) {
                    sb.append(str);
                }
                utf8 = new String(sb.toString().getBytes(), "utf-8");
                System.out.println("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~" + utf8);
                JSONObject object = new JSONObject(utf8);
                int result = object.getInt("result");
                if (result == 200) {
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            AlertDialog.Builder builder = new AlertDialog.Builder(context);
                            builder.setTitle("小星提醒你~~~");
                            builder.setMessage("您的ID不正确呦...重新试试看吧");
                            builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface arg0, int arg1) {
                                    Intent intent = new Intent(context, LoginActivity.class);
                                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                    context.startActivity(intent);
                                }
                            }).create().show();
                        }
                    });

                } else {
                    handler.post(new Runnable() {
                        @Override
                        public void run() {

                            departs = parseJson_depaterment(utf8.toString());
                            pers = parseJson_person(utf8.toString());
                            textView.setText(db);
                            adapter = new MyexpandableListAdapter(context);
                            expandableListView.setAdapter(adapter);
                            expandableListView.setOnHeaderUpdateListener(HttpJson.this);
                            expandableListView.setOnChildClickListener(HttpJson.this);
                            expandableListView.setOnGroupClickListener(HttpJson.this);
                            stickyLayout.setOnGiveUpTouchEventListener(HttpJson.this);

                            sh_first();

                        }
                    });
                }
            } else {
                System.out.println("服务器发生错误");
                sh_catch();
            }
        } catch (MalformedURLException e) {
            // TODO Auto-generated catch block
            System.out.println("ssssssssssssssssssssssssss2");
            sh_catch();
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            System.out.println("网络不通畅呦");
            sh_one();
            e.printStackTrace();
        } catch (JSONException e) {
            System.out.println("别盗用我们接口呦，您的数据已经上报至服务器~~~");
            sh_catch();
            e.printStackTrace();
        }
    }

    private void sh_first() {
        value_first = context.getSharedPreferences("value_first", MODE_APPEND);
        SharedPreferences.Editor ed = value_first.edit();
        ed.putBoolean("value_first", true);
        ed.putString("url_name", str);
        ed.commit();
    }

    private void sh_one() {
        handler.post(new Runnable() {
            @Override
            public void run() {
                departs = sh_depaterment();
                if (departs == null) {

                    AlertDialog.Builder builder = new AlertDialog.Builder(context);
                    builder.setTitle("小星提醒你~~~");
                    builder.setMessage("首次使用需要联网获取通讯录呦，重新试试吧");
                    builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface arg0, int arg1) {
                            Intent intent = new Intent(context, LoginActivity.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            context.startActivity(intent);
                        }
                    }).create().show();

                    System.out.println("第一次使用请联网呦~~~~~~~~~~~");

                } else {
                    pers = sh_person();
                    adapter = new MyexpandableListAdapter(context);
                    expandableListView.setAdapter(adapter);
                    expandableListView.setOnHeaderUpdateListener(HttpJson.this);
                    expandableListView.setOnChildClickListener(HttpJson.this);
                    expandableListView.setOnGroupClickListener(HttpJson.this);
                    stickyLayout.setOnGiveUpTouchEventListener(HttpJson.this);
//                            // 展开所有group
//                            for (int i = 0, count = expandableListView.getCount(); i < count;
//                                 i++) {
//                                expandableListView.expandGroup(i);
//                            }
                }
            }
        });
    }

    private void sh_catch() {
        handler.post(new Runnable() {
            @Override
            public void run() {
                departs = sh_depaterment();
                pers = sh_person();
                adapter = new MyexpandableListAdapter(context);
                expandableListView.setAdapter(adapter);
                expandableListView.setOnHeaderUpdateListener(HttpJson.this);
                expandableListView.setOnChildClickListener(HttpJson.this);
                expandableListView.setOnGroupClickListener(HttpJson.this);
                stickyLayout.setOnGiveUpTouchEventListener(HttpJson.this);
//                            // 展开所有group
//                            for (int i = 0, count = expandableListView.getCount(); i < count;
//                                 i++) {
//                                expandableListView.expandGroup(i);
//                            }
            }
        });
    }

    private ArrayList<List<PersonInfo>> sh_person() {
        datas_person = context.getSharedPreferences("datas_person", MODE_APPEND);
        ArrayList<List<PersonInfo>> persons = new ArrayList<List<PersonInfo>>();
        int lenth_i = datas_person.getInt("lenth_i", 0);
//        System.out.println("lenth_i=" + lenth_i);

        for (int i = 0; i < lenth_i; i++) {
            ArrayList<PersonInfo> personInfoArrayList = new ArrayList<PersonInfo>();
            int lenth_j = datas_person.getInt("lenth_j_" + i, 0);
//            System.out.println("lenth_j_" + i + "=" + lenth_j);

            for (int j = 0; j < lenth_j; j++) {
                PersonInfo personInfo = new PersonInfo();

                String name = datas_person.getString("name" + i + " " + j, "");
                String tel = datas_person.getString("tel" + i + " " + j, "");
                String phone = datas_person.getString("phone" + i + " " + j, "");

//                System.out.println(name);
//                System.out.println(tel);
//                System.out.println(phone);

                personInfo.setName(name);
                personInfo.setTel(tel);
                personInfo.setPhone(phone);
                personInfoArrayList.add(personInfo);
            }
            persons.add(personInfoArrayList);

        }
        return persons;

    }

    private ArrayList<List<PersonInfo>> parseJson_person(String json) {
        try {
            datas_person = context.getSharedPreferences("datas_person", MODE_APPEND);
            JSONObject object = new JSONObject(json);
            int result = object.getInt("result");
            if (result == 80805012) {
                SharedPreferences.Editor editor2 = datas_person.edit();
                editor2.clear();
                editor2.commit();

                ArrayList<List<PersonInfo>> persons = new ArrayList<List<PersonInfo>>();

                JSONArray personTo = object.getJSONArray("departments");

                SharedPreferences.Editor editor1 = datas_person.edit();
                editor1.putInt("lenth_i", personTo.length());
                editor1.commit();

//                System.out.println(personTo);
                for (int i = 0; i < personTo.length(); i++) {
                    JSONObject PersonData = personTo.getJSONObject(i);
                    try {
                        JSONArray members = PersonData.getJSONArray("members");
                        System.out.println(members);
                        ArrayList<PersonInfo> personInfoArrayList = new ArrayList<PersonInfo>();
                        for (int j = 0; j < members.length(); j++) {
                            PersonInfo personInfo = new PersonInfo();
                            JSONObject people = members.getJSONObject(j);
                            String name = people.getString("username");
                            String tel = people.getString("tel");
                            String phone = people.getString("phone");

                            SharedPreferences.Editor editor = datas_person.edit();
                            editor.putString("name" + i + " " + j, name);
                            editor.putString("tel" + i + " " + j, tel);
                            editor.putString("phone" + i + " " + j, phone);
                            editor.commit();

                            SharedPreferences.Editor editor0 = datas_person.edit();
                            editor0.putInt("lenth_j_" + i, members.length());
                            editor0.commit();
                            System.out.println("~~~~~~~~~这个是获取新消息中~~~~~~~~~~~~~");
                            personInfo.setName(name);
                            personInfo.setTel(tel);
                            personInfo.setPhone(phone);
                            personInfoArrayList.add(personInfo);

                        }
                        persons.add(personInfoArrayList);

                    } catch (JSONException e) {
                        ArrayList<PersonInfo> personInfoArrayList = new ArrayList<PersonInfo>();
                        PersonInfo personInfo = new PersonInfo();
                        personInfo.setName("此列表为空");
                        personInfo.setTel("");
                        personInfo.setPhone("");
                        personInfoArrayList.add(personInfo);

                        SharedPreferences.Editor editor = datas_person.edit();
                        editor.putInt("lenth_j_" + i, 1);
                        editor.putString("name" + i + " " + 0, "此列表为空");
                        editor.putString("tel" + i + " " + 0, "");
                        editor.putString("phone" + i + " " + 0, "");
                        editor.commit();


                        persons.add(personInfoArrayList);
                    }
                }
                return persons;
            } else {
//                Toast.makeText(context, "error", 1).show();
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    private ArrayList<Depart> sh_depaterment() {
        datas_departs = context.getSharedPreferences("datas_departs", MODE_APPEND);
        ArrayList<Depart> de = new ArrayList<Depart>();
        int lenth_de = datas_departs.getInt("lenth_de", 0);
        db = datas_departs.getString("department_belong", "");
        textView.setText(db);
//        System.out.println("lenth_de=" + lenth_de);
        for (int i = 0; i < lenth_de; i++) {
            Depart departObject = new Depart();
            String name = datas_departs.getString("name" + i, "");
//            System.out.println("dadadadadadadadadadadadad=" + name);
            departObject.setTitle(name);
            de.add(departObject);
        }
        if (lenth_de == 0) {
            return null;

        } else
            return de;
    }

    private ArrayList<Depart> parseJson_depaterment(String json) {
        try {
            datas_departs = context.getSharedPreferences("datas_departs", MODE_APPEND);
            JSONObject object = new JSONObject(json);
            int result = object.getInt("result");
            ArrayList<Depart> de = new ArrayList<Depart>();
            if (result == 80805012) {
                db = object.getString("department_belong");
//                textView.setText(db);
                JSONArray department = object.getJSONArray("departments");
                SharedPreferences.Editor editor2 = datas_departs.edit();
                editor2.clear();
                editor2.commit();

                SharedPreferences.Editor editor0 = datas_departs.edit();
                editor0.putInt("lenth_de", department.length());
                editor0.putString("department_belong", db);
                editor0.commit();

                for (int i = 0; i < department.length(); i++) {
                    Depart departObject = new Depart();
                    JSONObject dapart = department.getJSONObject(i);
                    String name = dapart.getString("name");
                    departObject.setTitle(name);

                    SharedPreferences.Editor editor = datas_departs.edit();
                    editor.putString("name" + i, name);
                    editor.commit();

//                    System.out.println(name);
                    de.add(departObject);
                }

                return de;
            } else {
//                Toast.makeText(context, "error", 1).show();
            }

        } catch (JSONException e) {
            // TODO Auto-generated catch block
            System.out.println("退出至登录页");
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 根据输入框中的值来过滤数据并更新ListView
     *
     * @param filterStr
     */
    public void filterData(String filterStr) {

        departs = sh_depaterment();
        pers = sh_person();
        adapter = new MyexpandableListAdapter(context);
        expandableListView.setAdapter(adapter);
        expandableListView.setOnHeaderUpdateListener(HttpJson.this);
        expandableListView.setOnChildClickListener(HttpJson.this);
        expandableListView.setOnGroupClickListener(HttpJson.this);
        stickyLayout.setOnGiveUpTouchEventListener(HttpJson.this);

        // 实例化汉字转拼音类
        characterParser = CharacterParser.getInstance();

//        pinyinComparator = new PinyinComparator();
        ArrayList<Depart> groupFilterList = new ArrayList<Depart>();
        List<PersonInfo> tempFilterList;
        ArrayList<List<PersonInfo>> childFilterList = new ArrayList<List<PersonInfo>>();

        if (TextUtils.isEmpty(filterStr)) {
            groupFilterList = departs;
            childFilterList = pers;
//            noSearchResultTv.setVisibility(View.GONE);
        } else {
            groupFilterList.clear();
            childFilterList.clear();
            for (int i = 0; i < departs.size(); i++) {
                //标记departGroup是否加入元素
                int isAddGroup = 0;
                tempFilterList = new ArrayList<PersonInfo>();
                Depart sortModel = departs.get(i);
                String name = sortModel.getTitle();
                // depart有字符直接加入
                if (name.indexOf(filterStr.toString()) != -1
                        || characterParser.getSelling(name).startsWith(
                        filterStr.toString())) {
                    if (!groupFilterList.contains(sortModel)) {
                        groupFilterList.add(sortModel);
                        isAddGroup = 1;
                    }
                }

                for (int j = 0; j < pers.get(i).size(); j++) {
                    PersonInfo sortChildModel = pers.get(i)
                            .get(j);
                    String childName = sortChildModel.getName();
                    String childTel = sortChildModel.getTel();
                    String childPhone = sortChildModel.getPhone();
                    // child有字符直接加入，其父也加入
                    if (childName.indexOf(filterStr.toString()) != -1
                            || characterParser.getSelling(childName)
                            .startsWith(filterStr.toString())

                            || childTel.indexOf(filterStr.toString()) != -1
                            || characterParser.getSelling(childTel)
                            .startsWith(filterStr.toString())

                            || childPhone.indexOf(filterStr.toString()) != -1
                            || characterParser.getSelling(childPhone)
                            .startsWith(filterStr.toString())
                            ) {
                        tempFilterList.add(sortChildModel);
                        if (!groupFilterList.contains(departs.get(i))) {
                            groupFilterList.add(departs.get(i));
                            isAddGroup = 2;
                        }
                    }

                }
//                Collections.sort(tempFilterList, pinyinComparator);

                //isAddGroup为0 搜索框无数据
                //            1 中心
                //            2 姓名

                if (isAddGroup == 1) {
                    childFilterList.add(pers.get(i));
                } else if (isAddGroup == 2) {
                    childFilterList.add(tempFilterList);
                }
            }

            // 根据a-z进行排序
//            Collections.sort(departs, pinyinComparator);
        }

        if (adapter != null) {
            departs = groupFilterList;
            pers = childFilterList;
            adapter.notifyDataSetChanged();

            if (TextUtils.isEmpty(filterStr)) {
                for (int i = 0; i < departs.size(); i++) {
//                    if (i == 0) {
//                        expandableListView.expandGroup(i);
//                        continue;
//                    }
                    expandableListView.collapseGroup(i);
                }
            } else {
                //搜索的结果全部展开
                for (int i = 0; i < departs.size(); i++) {
                    expandableListView.expandGroup(i);
                }
            }
        }

//        //如果查询的结果为0时，显示为搜索到结果的提示
//        if (groupFilterList.size() == 0) {
//            noSearchResultTv.setVisibility(View.VISIBLE);
//        } else {
//            noSearchResultTv.setVisibility(View.GONE);
//        }
    }

    /***
     * 数据源
     *
     * @author HugeTerry
     */
    public class MyexpandableListAdapter extends BaseExpandableListAdapter {
        private Context context;
        private LayoutInflater inflater;
//        ArrayList<Depart> departs;

        public MyexpandableListAdapter(Context context) {
            this.context = context;
            inflater = LayoutInflater.from(context);
        }


//        public void setData(ArrayList<Depart> data) {
//            this.departs = data;
//        }

        // 返回父列表个数
        @Override
        public int getGroupCount() {
            return departs.size();
        }

        // 返回子列表个数
        @Override
        public int getChildrenCount(int groupPosition) {
            return pers.get(groupPosition).size();
        }

        @Override
        public Object getGroup(int groupPosition) {

            return departs.get(groupPosition);
        }

        @Override
        public Object getChild(int groupPosition, int childPosition) {
            return pers.get(groupPosition).get(childPosition);
        }

        @Override
        public long getGroupId(int groupPosition) {
            return groupPosition;
        }

        @Override
        public long getChildId(int groupPosition, int childPosition) {
            return childPosition;
        }

        @Override
        public boolean hasStableIds() {

            return true;
        }

        @Override
        public View getGroupView(int groupPosition, boolean isExpanded,
                                 View convertView, ViewGroup parent) {
            GroupHolder groupHolder = null;
            if (convertView == null) {
                groupHolder = new GroupHolder();
                convertView = inflater.inflate(R.layout.group, null);
                groupHolder.textView = (TextView) convertView
                        .findViewById(R.id.group);
                convertView.setTag(groupHolder);
                groupHolder.imageView = (ImageView) convertView
                        .findViewById(R.id.image);
            } else {
                groupHolder = (GroupHolder) convertView.getTag();
            }
            groupHolder.textView.setText(((Depart) getGroup(groupPosition))
                    .getTitle());

            groupHolder.imageView.setImageResource(R.drawable.expanded);
            if (isExpanded)// ture is Expanded or false is not isExpanded
            {
                groupHolder.imageView.setImageResource(R.drawable.collapse);
            }
//            else {
//                System.out.println("starte22222222222222222222222222222");
//
//                System.out.println("enddd22222222222222222222222222222");
//            }
            return convertView;
        }

        @Override
        public View getChildView(final int groupPosition,
                                 final int childPosition, boolean isLastChild, View convertView,
                                 ViewGroup parent) {
            ChildHolder childHolder = null;
            if (convertView == null) {
                childHolder = new ChildHolder();
                convertView = inflater.inflate(R.layout.child, null);

                childHolder.textName = (TextView) convertView
                        .findViewById(R.id.name);
                childHolder.textTel = (TextView) convertView
                        .findViewById(R.id.tel);
                childHolder.imageView = (ImageView) convertView
                        .findViewById(R.id.image);

                childHolder.button1 = (Button) convertView
                        .findViewById(R.id.button1);
                childHolder.button2 = (Button) convertView
                        .findViewById(R.id.button2);
//                childHolder.rv= (RippleView) findViewById(R.id.btn_rip);
                childHolder.rv = (RippleView) convertView.findViewById(R.id.rip);

            } else {
                childHolder = (ChildHolder) convertView.getTag();
            }
            if (((PersonInfo) getChild(groupPosition,
                    childPosition)).getName() == "此列表为空") {
                childHolder.button1.setVisibility(View.INVISIBLE);
                childHolder.button2.setVisibility(View.INVISIBLE);
            } else {
                childHolder.button1.setVisibility(View.VISIBLE);
                childHolder.button2.setVisibility(View.VISIBLE);
            }
            childHolder.textName.setText(((PersonInfo) getChild(groupPosition,
                    childPosition)).getName());
            if (now == 0) {
                childHolder.textTel.setText(
                        String.valueOf(((PersonInfo) getChild(groupPosition,
                                childPosition)).getTel()));
            } else if (now == 1) {
                childHolder.textTel.setText(
                        String.valueOf(((PersonInfo) getChild(groupPosition,
                                childPosition)).getPhone()));
            }
            childHolder.button1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    if (now == 0) {
                        Intent intent = new Intent(Intent.ACTION_DIAL, Uri
                                .parse("tel:" + ((PersonInfo) getChild(groupPosition,
                                        childPosition)).getTel()));
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        context.startActivity(intent);
                    } else if (now == 1) {
                        Intent intent = new Intent(Intent.ACTION_DIAL, Uri
                                .parse("tel:" + ((PersonInfo) getChild(groupPosition,
                                        childPosition)).getPhone()));
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        context.startActivity(intent);
                    }
                }
            });

            childHolder.button2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (now == 0) {
                        Intent intent = new Intent(Intent.ACTION_SENDTO, Uri
                                .parse("smsto:" + ((PersonInfo) getChild(groupPosition,
                                        childPosition)).getTel()));
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        context.startActivity(intent);
                    } else if (now == 1) {
                        Intent intent = new Intent(Intent.ACTION_SENDTO, Uri
                                .parse("smsto:" + ((PersonInfo) getChild(groupPosition,
                                        childPosition)).getPhone()));
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        context.startActivity(intent);
                    }
                }
            });

            convertView.setTag(childHolder);

            return convertView;
        }

        @Override
        public boolean isChildSelectable(int groupPosition, int childPosition) {
            return true;
        }
    }

    @Override
    public boolean onGroupClick(final ExpandableListView parent, final View v,
                                int groupPosition, final long id) {

        return false;
    }

    @Override
    public boolean onChildClick(ExpandableListView parent, View v,
                                int groupPosition, int childPosition, long id) {
//        Toast.makeText(context,
//                pers.get(groupPosition).get(childPosition).getName(), Toast.LENGTH_SHORT)
//                .show();
        String name = pers.get(groupPosition).get(childPosition).getName();
        String tel = pers.get(groupPosition).get(childPosition).getTel();
        String phone = pers.get(groupPosition).get(childPosition).getPhone();
        String gr = departs.get(groupPosition).getTitle();
        Intent intent = new Intent(context, MainActivity_Message.class);
        intent.putExtra("db", db);
        intent.putExtra("gr", gr);
        intent.putExtra("name", name);
        if (now == 0)
            intent.putExtra("tel", tel);
        else if (now == 1) intent.putExtra("tel", phone);
        context.startActivity(intent);
        return false;
    }

    class GroupHolder {
        TextView textView;
        ImageView imageView;
    }

    class ChildHolder {
        TextView textName;
        TextView textTel;
        ImageView imageView;
        Button button1;
        Button button2;

        RippleView rv;
    }

    @Override
    public View getPinnedHeader() {
        LayoutInflater inflater = LayoutInflater.from(context);
        View headerView = (ViewGroup) inflater.inflate(
                R.layout.group, null);
        headerView.setLayoutParams(new AbsListView.LayoutParams(AbsListView.LayoutParams.MATCH_PARENT,
                AbsListView.LayoutParams.WRAP_CONTENT));

        return headerView;
    }

    @Override
    public void updatePinnedHeader(View headerView, int firstVisibleGroupPos) {
        Depart firstVisibleGroup = (Depart) adapter
                .getGroup(firstVisibleGroupPos);
        TextView textView = (TextView) headerView.findViewById(R.id.group);
        textView.setText(firstVisibleGroup.getTitle());
    }

    @Override
    public boolean giveUpTouchEvent(MotionEvent event) {
        if (expandableListView.getFirstVisiblePosition() == 0) {
            View view = expandableListView.getChildAt(0);
            if (view != null && view.getTop() >= 0) {
                return true;
            }
        }
        return false;
    }
}