package com.intuit.schedulingapp;

import android.os.StrictMode;
import android.util.Log;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by cpingale on 4/15/16.
 */
public class HTTPConnector {

    public static HttpResponse configureHTTP() {

        HttpClient httpClient = new DefaultHttpClient();
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();

        StrictMode.setThreadPolicy(policy);

        HttpPost httpPost = new HttpPost("http://search-e2e.platform.intuit.net/action/v1/access?encapjson=true");

        httpPost.addHeader("Authorization", "Intuit_IAM_Authentication intuit_appid=Intuit.platform.sts.sts.test, intuit_app_secret=3smCfomnHI5jGevWPKOIbV");
        httpPost.addHeader("Content-Type", "application/json");

        //Post Data
        List<NameValuePair> nameValuePair = new ArrayList<NameValuePair>(2);
        //nameValuePair.add(new BasicNameValuePair());
        //nameValuePair.add(new BasicNameValuePair());


        //Encoding POST data
        try {
            httpPost.setEntity(new UrlEncodedFormEntity(nameValuePair));
        } catch (UnsupportedEncodingException e) {
            // log exception
            e.printStackTrace();
        }
        HttpResponse response=null;
        //making POST request.
        try {
            response = httpClient.execute(httpPost);
            // write response to log
            Log.d("Http Post Response:", response.toString());
        } catch (ClientProtocolException e) {
            // Log exception
            e.printStackTrace();
        } catch (IOException e) {
            // Log exception
            e.printStackTrace();
        }
        return  response;
    }

}
