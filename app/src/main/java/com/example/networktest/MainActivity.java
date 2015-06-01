package com.example.networktest;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.xml.sax.InputSource;
import org.xml.sax.XMLReader;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.net.HttpURLConnection;
import java.net.URL;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;


public class MainActivity extends Activity {
    public static final int SHOW_RESPONSE=0;
    private Button sendRequest;
    private Button sendPost;
    private Button sendRequestByClient;
    private TextView response;
    private Handler handler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
           String text=(String)msg.obj;
           response.setText(text);
        }
    };
    private Handler handler2=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            String text=(String)msg.obj;
//            praseXmlWithPull(text);
              Log.d("MainActivityXml",text);
            praseXmlWithSax(text);
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
//                            URL url=new URL("http://139.129.15.121/ljwtest/get_data.xml");
                            URL url=new URL("http://139.129.15.121/ljwtest/get_data.json");
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
                            praseJSONWithJSONObject(buffer.toString());
//                            Message message=new Message();
//                            message.obj=buffer.toString();
//                            message.what=SHOW_RESPONSE;
//                          handler.sendMessage(message);
//                            handler2.sendMessage(message);

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
                    URL url=new URL("https://www.1000dd.com/login-index.shtml");
                    connection=(HttpURLConnection)url.openConnection();
                    connection.setRequestMethod("POST");
                    connection.setConnectTimeout(8000);
                    connection.setReadTimeout(8000);
                    DataOutputStream outputStream=new DataOutputStream(connection.getOutputStream());
                    outputStream.writeBytes("user_name=test001&pass_word=1081610");
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
        sendRequestByClient=(Button)findViewById(R.id.send_request_by_client);
        sendRequestByClient.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            CloseableHttpClient httpClient = HttpClientBuilder.create().build();
                            HttpGet httpGet=new HttpGet("http://3g.163.com/x");
                            HttpResponse httpResponse=httpClient.execute(httpGet);
                            if (httpResponse.getStatusLine().getStatusCode()==200)
                            {
                                HttpEntity entity=httpResponse.getEntity();
                                String response= EntityUtils.toString(entity,"utf-8");
                                Message message=new Message();
                                message.what=SHOW_RESPONSE;
                                message.obj=response.toString();
                                handler.sendMessage(message);
                            }
                        } catch (IOException e) {
                            e.printStackTrace();
                        } finally {
                        }

                    }
                }).start();
            }
        });
    }
    public void praseXmlWithPull(String xmldata){
        try {
            XmlPullParserFactory factory=XmlPullParserFactory.newInstance();
            XmlPullParser parser=factory.newPullParser();
            parser.setInput(new StringReader(xmldata));
            int eventType=parser.getEventType();
            String name="",id="",version="";
            while (eventType!=XmlPullParser.END_DOCUMENT)
            {
                String nodeName=parser.getName();
                switch (eventType)
                {
                    case XmlPullParser.START_TAG:
                        switch (nodeName)
                        {
                            case "id":
                                id=parser.nextText();
                                break;
                            case "name":
                                name=parser.nextText();
                                break;
                            case  "version":
                                version=parser.nextText();
                                break;
                            default:
                                break;
                        }
                        break;
                    case XmlPullParser.END_TAG:
                        if ("app".equals(nodeName))
                        {
                            Log.d("MainActivityXml","id is "+id);
                            Log.d("MainActivityXml","name is "+name);
                            Log.d("MainActivityXml","version is "+version);
                        }
                        break;
                    default:
                        break;
                }
                eventType=parser.next();
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
        }
    }
    public void praseXmlWithSax(String xmlData){
        Log.d("MainActivityXml",xmlData);
        try {
            SAXParserFactory factory=SAXParserFactory.newInstance();
            SAXParser parser=factory.newSAXParser();
            MyHandler handler=new MyHandler();
            XMLReader xmlReader=parser.getXMLReader();
            xmlReader.setContentHandler(handler);
            xmlReader.parse(new InputSource(new StringReader(xmlData)));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public void praseJSONWithJSONObject(String JSONData){
        try {
            JSONArray jsonArray=new JSONArray(JSONData);
            for(int i=0;i<jsonArray.length();i++)
            {
                JSONObject jsonObject=jsonArray.getJSONObject(i);
                String id=jsonObject.getString("id");
                String name=jsonObject.getString("name");
                String version=jsonObject.getString("version");
                Log.d("MainActivityXml","id is "+id);
                Log.d("MainActivityXml","name is "+name);
                Log.d("MainActivityXml","version is "+version);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


}
