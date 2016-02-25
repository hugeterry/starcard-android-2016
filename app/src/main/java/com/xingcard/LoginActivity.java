package com.xingcard;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

/**
 * Created by Administrator on 2015/8/12 0012.
 */
public class  LoginActivity extends Activity {

    private EditText et;
    private Button bt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        et = (EditText) findViewById(R.id.et);
        bt = (Button) findViewById(R.id.bt);

        bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String str = et.getText().toString();
                if (str.equals("")) {
                    AlertDialog.Builder builder = new Builder(LoginActivity.this);
                    // builder.setTitle("确认" ) ;
                    builder.setMessage("组织/社团ID不能为空");
                    builder.setPositiveButton("确认", null);
                    builder.show();
                } else {
                    Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                    intent.putExtra("str", str);
                    startActivity(intent);
                    LoginActivity.this.finish();
                }
            }
        });


    }
}
