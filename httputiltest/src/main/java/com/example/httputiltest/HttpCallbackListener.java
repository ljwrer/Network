package com.example.httputiltest;

/**
 * Created by Ray on 2015/6/2.
 */
public interface HttpCallbackListener {
    void onFinish(String response);
    void onError(Exception e);
}
