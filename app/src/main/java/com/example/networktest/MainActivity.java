package com.example.networktest;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;


public class MainActivity extends Activity {
    public static final int SHOW_RESPONSE=0;
    private Button sendRequest;
    private Button sendPost;
    private TextView response;
    private Handler handler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
           String text=(String)msg.obj;
           response.setText(text);
        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        sendRequest=(Button)findViewById(R.id.send_request);
        response=(TextView)findViewById(R.id.response_text);
        sendRequest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        HttpURLConnection connection=null;
                        try {
                            URL url=new URL("http://3g.163.com/x");
                            connection=(HttpURLConnection)url.openConnection();
                            connection.setConnectTimeout(8000);
                            connection.setReadTimeout(8000);
                            connection.setRequestMethod("GET");
                            InputStream stream=connection.getInputStream();
                            BufferedReader reader=new BufferedReader(new InputStreamReader(stream));
                            StringBuffer buffer=new StringBuffer();
                            String line;
                            while ((line=reader.readLine())!=null)
                            {
                                buffer.append(line);
                            }
                            Message message=new Message();
                            message.obj=buffer.toString();
                            message.what=SHOW_RESPONSE;
                            handler.sendMessage(message);
                        } catch (Exception e) {
                            e.printStackTrace();
                        } finally {
                            if(connection!=null)
                            {
                                connection.disconnect();
                            }
                        }
                    }
                }).start();
            }
        });
        sendPost=(Button)findViewById(R.id.send_post);
        sendPost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                HttpURLConnection connection=null;
                try {
                    URL url=new URL("http://3g.163.com/x");
                    connection=(HttpURLConnection)url.openConnection();
                    connection.setRequestMethod("POST");
                    connection.setConnectTimeout(8000);
                    connection.setReadTimeout(8000);
                    DataOutputStream outputStream=new DataOutputStream(connection.getOutputStream());
                    outputStream.writeBytes("username=admin&password=123456");
                } catch (IOException e) {
                    e.printStackTrace();
                } finally {
                    if (connection!=null)
                    {
                        connection.disconnect();
                    }
                }
            }
        });
    }


}
