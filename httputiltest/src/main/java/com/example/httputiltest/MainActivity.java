package com.example.httputiltest;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;


public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        final TextView text=(TextView)findViewById(R.id.text);
        HttpUtil.sendHttpRequest("http://3g.163.com/x", new HttpCallbackListener() {
            @Override
            public void onFinish(String response) {
                Log.d("aaa",response);
                text.setText(response);
            }

            @Override
            public void onError(Exception e) {

            }
        });
    }
}
