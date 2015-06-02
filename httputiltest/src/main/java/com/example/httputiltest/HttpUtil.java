package com.example.httputiltest;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;


/**
 * Created by Ray on 2015/6/2.
 */
public class HttpUtil {
    public static void sendHttpRequest(final String address,final HttpCallbackListener listener){
        new Thread(new Runnable() {
            @Override
            public void run() {
                HttpURLConnection connection=null;
                try {
                    URL url=new URL(address);
                    connection=(HttpURLConnection)url.openConnection();
                    connection.setRequestMethod("GET");
                    connection.setReadTimeout(8000);
                    connection.setConnectTimeout(8000);
                    InputStream stream=connection.getInputStream();
                    BufferedReader reader=new BufferedReader(new InputStreamReader(stream));
                    StringBuilder builder=new StringBuilder();
                    String line;
                    while ((line=reader.readLine())!=null)
                    {
                        builder.append(line);
                    }
                    if (listener!=null)
                    {
                        listener.onFinish(builder.toString());
                    }
                } catch (MalformedURLException e) {
                    if(listener!=null)
                    {
                        listener.onError(e);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                }
            }
        }).start();
    }
}
