package com.xingcard.update;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;

import com.xingcard.MainActivity;
import com.xingcard.R;


public class UpdateDialog extends Activity {
	ImageView yes,no;
	TextView tv;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.updatedialog);

		yes = (ImageView) findViewById(R.id.updatedialog_yes);
		no = (ImageView) findViewById(R.id.updatedialog_no);
		tv = (TextView) findViewById(R.id.updatedialog_text);
		yes.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				Intent intent = new Intent(UpdateDialog.this, MainActivity.class);
				setResult(2,intent);
				finish();

			}
		});

		no.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				Intent intent = new Intent(UpdateDialog.this, MainActivity.class);
				setResult(1,intent);
				finish();
			}
		});
	}


}
