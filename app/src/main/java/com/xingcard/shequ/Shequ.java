package com.xingcard.shequ;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.SimpleAdapter;

import com.xingcard.R;
import com.xingcard.shequ.bean.Lost;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.bmob.v3.Bmob;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.listener.FindListener;

public class Shequ extends Activity implements OnClickListener {
	private ListView mListView;

	private Button mButton;
	private Button bu_add;
	private SimpleAdapter ad;
	// 社区
	protected int mScreenWidth;
	RelativeLayout progress;
	PopupWindow morePop;
	RelativeLayout layout_action;
	LinearLayout layout_all;
	private List<Map<String, Object>> lists;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		Bmob.initialize(this, "035329ce80891f1f06c0b4a590c3fc8a");
		setContentView(R.layout.shequ_main);

		progress = (RelativeLayout) findViewById(R.id.progress);
		mButton = (Button) findViewById(R.id.bu_back);
		bu_add = (Button) findViewById(R.id.btn_add);
		initEvents();
		queryLost();

	}

	private void initEvents() {
		mListView = (ListView) findViewById(R.id.list_lost);
		lists = new ArrayList<Map<String, Object>>();
		ad = new SimpleAdapter(this, lists, R.layout.item_list, new String[] {
				"title", "describe", "name", "time" }, new int[] {
				R.id.tv_title, R.id.tv_describe, R.id.tv_photo, R.id.tv_time });

		mButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				finish();

			}
		});
		bu_add.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				Intent intent = new Intent(Shequ.this, AddShequ.class);

				startActivityForResult(intent, Constants.REQUESTCODE_ADD);

			}

		});
	}

	public void queryLost() {
		mListView.setVisibility(View.VISIBLE);
		BmobQuery<Lost> query = new BmobQuery<Lost>();
		query.setLimit(15);
		query.order("-createdAt");
		query.findObjects(Shequ.this, new FindListener<Lost>() {

			@Override
			public void onSuccess(List<Lost> title) {
				lists.clear();
				progress.setVisibility(View.GONE);
				for (Lost Lost : title) {
					Map<String, Object> map = new HashMap<String, Object>();
					map.put("title", Lost.getTitle());
					map.put("describe", Lost.getDescribe());
					map.put("name", Lost.getName());
					map.put("time", Lost.getCreatedAt());
					lists.add(map);
					mListView.setAdapter(ad);
					ad.notifyDataSetChanged();
				}
			}

			@Override
			public void onError(int arg0, String arg1) {
				progress.setVisibility(View.GONE);
				AlertDialog.Builder builder = new Builder(Shequ.this);
				builder.setMessage("网络不通畅哟，稍后再试一试吧");
				builder.setPositiveButton("确认", null);
				builder.show();
			}

		});
	}

	@Override
	public void onClick(View arg0) {

	}

	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		if (resultCode != RESULT_OK) {
			return;
		}
		switch (requestCode) {
			case Constants.REQUESTCODE_ADD:// 添加成功之后的回调

				queryLost();

				break;
		}
	}

	public class Constants {

		public static final int REQUESTCODE_ADD = 1;// 添加失物/招领
	}

}
