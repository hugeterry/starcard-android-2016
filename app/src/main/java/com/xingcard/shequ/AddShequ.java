package com.xingcard.shequ;

import android.app.Activity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.xingcard.R;
import com.xingcard.shequ.bean.Lost;

import cn.bmob.v3.Bmob;
import cn.bmob.v3.listener.SaveListener;



public class AddShequ extends Activity implements OnClickListener {

	EditText edit_title, edit_photo, edit_describe;
	Button btn_back, btn_true;

	TextView tv_add;
	String from = "";

	String old_title = "";
	String old_describe = "";
	String old_phone = "";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.shequ_add);
		Bmob.initialize(this, "035329ce80891f1f06c0b4a590c3fc8a");

		initViews();
		initListeners();
		initData();
	}

	Toast mToast;

	public void ShowToast(String text) {
		if (!TextUtils.isEmpty(text)) {
			if (mToast == null) {
				mToast = Toast.makeText(getApplicationContext(), text,
						Toast.LENGTH_SHORT);
			} else {
				mToast.setText(text);
			}
			mToast.show();
		}
	}

	public void initViews() {
		// TODO Auto-generated method stub
		tv_add = (TextView) findViewById(R.id.tv_add);
		btn_back = (Button) findViewById(R.id.btn_back);
		btn_true = (Button) findViewById(R.id.btn_true);
		edit_photo = (EditText) findViewById(R.id.edit_photo);
		edit_describe = (EditText) findViewById(R.id.edit_describe);
		edit_title = (EditText) findViewById(R.id.edit_title);
	}

	public void initListeners() {
		// TODO Auto-generated method stub
		btn_back.setOnClickListener(this);
		btn_true.setOnClickListener(this);
	}

	public void initData() {
		// TODO Auto-generated method stub
		from = getIntent().getStringExtra("from");
		old_title = getIntent().getStringExtra("title");
		old_phone = getIntent().getStringExtra("phone");
		old_describe = getIntent().getStringExtra("describe");

		edit_title.setText(old_title);
		edit_describe.setText(old_describe);
		edit_photo.setText(old_phone);


	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		if (v == btn_true) {
			addByType();
		} else if (v == btn_back) {
			finish();
		}
	}

	String title = "";
	String describe = "";
	String photo = "";

	/**
	 * 根据类型添加社区内容 addByType
	 *
	 * @Title: addByType
	 * @throws
	 */
	private void addByType() {
		title = edit_title.getText().toString();
		describe = edit_describe.getText().toString();
		photo = edit_photo.getText().toString();

		if (TextUtils.isEmpty(title)) {
			ShowToast("请填写标题");
			return;
		}
		if (TextUtils.isEmpty(describe)) {
			ShowToast("请填写吐槽");
			return;
		}
		if (TextUtils.isEmpty(photo)) {
			ShowToast("请填写昵称");
			return;
		}

		addLost();

	}

	private void addLost() {
		Lost lost = new Lost();
		lost.setDescribe(describe);
		lost.setName(photo);
		lost.setTitle(title);
		lost.save(this, new SaveListener() {

			@Override
			public void onSuccess() {
				// TODO Auto-generated method stub
				ShowToast("吐槽成功!");
				setResult(RESULT_OK);
				finish();
			}

			@Override
			public void onFailure(int code, String arg0) {
				// TODO Auto-generated method stub
				ShowToast("吐槽失败:" + arg0);
			}
		});
	}

}
