package com.xingcard.baymax;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.xingcard.R;
import com.xingcard.baymax.bean.ChatMessage;
import com.xingcard.baymax.utils.HttpUtils;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;


public class Baymax extends Activity {
    private Button mButton;
    private ListView mMsgs;
    private ChatMessageAdapter mAdapter;
    private List<ChatMessage> mDatas;

    private EditText mInputMsg;
    private Button mSendMsg;

    private Handler mHandler = new Handler() {
        public void handleMessage(android.os.Message msg) {
            // 等待接收，子线程完成数据的返回
            ChatMessage fromMessge = (ChatMessage) msg.obj;
            mDatas.add(fromMessge);
            mAdapter.notifyDataSetChanged();
            mMsgs.setSelection(mDatas.size() - 1);
        };

    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.baymax_main);

        initView();
        initDatas();
        // 初始化事件
        initListener();
    }

    private void initListener() {
        mButton.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                finish();
            }
        });
        mSendMsg.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                final String toMsg = mInputMsg.getText().toString();
                if (TextUtils.isEmpty(toMsg)) {
                    Toast.makeText(Baymax.this, "发送消息不能为空！", Toast.LENGTH_SHORT)
                            .show();
                    return;
                }

                ChatMessage toMessage = new ChatMessage();
                toMessage.setDate(new Date());
                toMessage.setMsg(toMsg);
                toMessage.setType(ChatMessage.Type.OUTCOMING);
                mDatas.add(toMessage);
                mAdapter.notifyDataSetChanged();
                mMsgs.setSelection(mDatas.size() - 1);

                mInputMsg.setText("");

                new Thread() {
                    public void run() {
                        ChatMessage fromMessage = HttpUtils.sendMessage(toMsg);
                        Message m = Message.obtain();
                        m.obj = fromMessage;
                        mHandler.sendMessage(m);
                    };
                }.start();

            }
        });
    }

    private void initDatas() {
        mDatas = new ArrayList<ChatMessage>();
        mDatas.add(new ChatMessage("你好 我叫大白(●—●) 是你的私人吐槽对象  我来自星空",
                ChatMessage.Type.INCOMING, new Date()));
        mAdapter = new ChatMessageAdapter(this, mDatas);
        mMsgs.setAdapter(mAdapter);
    }

    private void initView() {
        mButton = (Button) findViewById(R.id.lxr_back);
        mMsgs = (ListView) findViewById(R.id.id_listview_msgs);
        mInputMsg = (EditText) findViewById(R.id.id_input_msg);
        mSendMsg = (Button) findViewById(R.id.id_send_msg);
    }

}