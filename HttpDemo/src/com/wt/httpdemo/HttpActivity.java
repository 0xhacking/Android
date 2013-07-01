package com.wt.httpdemo;


import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class HttpActivity extends Activity
{
    private String TAG = "http";
    private EditText mNameText = null;
    private EditText mAgeText = null;

    private Button getButton = null;
    private Button postButton = null;

    private TextView mResult = null;

    // 基本地址：服务器ip地址：端口号/Web项目逻辑地址+目标页面（Servlet）的url-pattern
    private String baseURL = "http://10.0.2.2:80/test.php";

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        Log.i(TAG, "onCreate");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_http);

        mNameText = (EditText) findViewById(R.id.name);
        mAgeText = (EditText) findViewById(R.id.age);
        mResult = (TextView) findViewById(R.id.result);

        getButton = (Button) findViewById(R.id.submit_get);
        getButton.setOnClickListener(mGetClickListener);
        postButton = (Button) findViewById(R.id.submit_post);
        postButton.setOnClickListener(mPostClickListener);
    }

    private OnClickListener mGetClickListener = new View.OnClickListener()
    {

        @Override
        public void onClick(View v)
        {
            Log.i(TAG, "GET request");
            // 先获取用户名和年龄
            String name = mNameText.getText().toString();
            String age = mAgeText.getText().toString();

            // 使用GET方法发送请求,需要把参数加在URL后面，用？连接，参数之间用&分隔
            String url = baseURL + "?username=" + name + "&age=" + age;

            // 生成请求对象
            HttpGet httpGet = new HttpGet(url);
            HttpClient httpClient = new DefaultHttpClient();

            // 发送请求
            try
            {

                HttpResponse response = httpClient.execute(httpGet);

                // 显示响应
                showResponseResult(response);// 一个私有方法，将响应结果显示出来

            }
            catch (Exception e)
            {
                e.printStackTrace();
            }

        }
    };

    private OnClickListener mPostClickListener = new View.OnClickListener()
    {

        @Override
        public void onClick(View v)
        {
            Log.i(TAG, "POST request");
            // 先获取用户名和年龄
            String name = mNameText.getText().toString();
            String age = mAgeText.getText().toString();

            NameValuePair pair1 = new BasicNameValuePair("username", name);
            NameValuePair pair2 = new BasicNameValuePair("age", age);

            List<NameValuePair> pairList = new ArrayList<NameValuePair>();
            pairList.add(pair1);
            pairList.add(pair2);

            try
            {
                HttpEntity requestHttpEntity = new UrlEncodedFormEntity(
                        pairList);
                // URL使用基本URL即可，其中不需要加参数
                HttpPost httpPost = new HttpPost(baseURL);
                // 将请求体内容加入请求中
                httpPost.setEntity(requestHttpEntity);
                // 需要客户端对象来发送请求
                HttpClient httpClient = new DefaultHttpClient();
                // 发送请求
                HttpResponse response = httpClient.execute(httpPost);
                // 显示响应
                showResponseResult(response);
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }

        }
    };

    /**
     * 显示响应结果到命令行和TextView
     * @param response
     */
    private void showResponseResult(HttpResponse response)
    {
        if (null == response)
        {
            return;
        }

        HttpEntity httpEntity = response.getEntity();
        try
        {
            InputStream inputStream = httpEntity.getContent();
            BufferedReader reader = new BufferedReader(new InputStreamReader(
                    inputStream));
            String result = "";
            String line = "";
            while (null != (line = reader.readLine()))
            {
                result += line;

            }

            System.out.println(result);
            mResult.setText("Response Content from server: " + result);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

    }

}
