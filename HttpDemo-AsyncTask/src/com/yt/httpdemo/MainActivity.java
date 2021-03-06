package com.yt.httpdemo;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.NoHttpResponseException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ConnectTimeoutException;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class MainActivity extends Activity {
	private String TAG = "http";
	private EditText mNameText = null;
	private EditText mAgeText = null;

	private Button getButton = null;
	private Button postButton = null;

	private TextView new_view = null;

	// 基本地址：服务器ip地址：端口号/Web项目逻辑地址+目标页面（Servlet）的url-pattern
	private String url = "http://10.0.2.2:80/test.php";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		Log.i(TAG, "onCreate");
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		getButton = (Button) findViewById(R.id.submit_get);
		getButton.setOnClickListener(mGetClickListener);

	}

	private OnClickListener mGetClickListener = new View.OnClickListener() {

		@Override
		public void onClick(View v) {

			String mt = null;
			try {
				Log.i("mt", "a");
				mt = new MyTask().execute("http://10.0.2.2:80/test.php")
						.get();
				if (mt != "") {
					Log.i("mt", mt);
				} else {

					Log.i("mt", "nothing");
				}
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (ExecutionException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			// Log.i(TAG, "GET request");
			// 先获取用户名和年龄
			// String name = mNameText.getText().toString();
			// String age = mAgeText.getText().toString();

			// 使用GET方法发送请求,需要把参数加在URL后面，用？连接，参数之间用&分隔
			// String url = baseURL + "?username=" + name + "&age=" + age;

			// 生成请求对象
			/*
			 * HttpGet httpGet = new HttpGet(url); HttpClient httpClient = new
			 * DefaultHttpClient();
			 */
			// 发送请求
			// try
			// {
			// Log.i("http", "httpGet");
			// HttpResponse response = httpClient.execute(httpGet);
			// Log.i("http", "httpGet2");
			// 显示响应
			// showResponseResult(response);// 一个私有方法，将响应结果显示出来

			// }
			// catch (Exception e)
			// {
			// e.printStackTrace();
			// / }

		}
	};

	private OnClickListener mPostClickListener = new View.OnClickListener() {

		@Override
		public void onClick(View v) {
			Log.i(TAG, "POST request");
			// 先获取用户名和年龄
			String name = mNameText.getText().toString();
			String age = mAgeText.getText().toString();

			NameValuePair pair1 = new BasicNameValuePair("username", name);
			NameValuePair pair2 = new BasicNameValuePair("age", age);

			List<NameValuePair> pairList = new ArrayList<NameValuePair>();
			pairList.add(pair1);
			pairList.add(pair2);

			try {
				HttpEntity requestHttpEntity = new UrlEncodedFormEntity(
						pairList);
				// URL使用基本URL即可，其中不需要加参数
				HttpPost httpPost = new HttpPost(url);
				// 将请求体内容加入请求中
				httpPost.setEntity(requestHttpEntity);
				// 需要客户端对象来发送请求
				HttpClient httpClient = new DefaultHttpClient();
				// 发送请求
				HttpResponse response = httpClient.execute(httpPost);
				// 显示响应
				showResponseResult(response);
			} catch (Exception e) {
				e.printStackTrace();
			}

		}
	};

	/**
	 * 显示响应结果到命令行和TextView
	 * 
	 * @param response
	 */
	private void showResponseResult(HttpResponse response) {
		if (null == response) {
			return;
		}

		HttpEntity httpEntity = response.getEntity();
		try {
			InputStream inputStream = httpEntity.getContent();
			BufferedReader reader = new BufferedReader(new InputStreamReader(
					inputStream));
			String result = "";
			String line = "";
			while (null != (line = reader.readLine())) {
				result += line;

			}

			System.out.println(result);
			new_view.setText("Response Content from server: " + result);
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	class MyTask extends AsyncTask<String, Integer, String> {

		@Override
		protected String doInBackground(String... params) {
			String line;
			try {
				Log.i("http", params[0]);

				HttpGet httpPost = new HttpGet(params[0]);
				Log.i("debug","1");
				HttpParams httpParameters = new BasicHttpParams();
				Log.i("debug","2");
				// Set the timeout in milliseconds until a connection is
				// established.
				int timeoutConnection = 10000;
				Log.i("debug","3");
				HttpConnectionParams.setConnectionTimeout(httpParameters,
						timeoutConnection);
				Log.i("debug","4");
				// Set the default socket timeout (SO_TIMEOUT)
				// in milliseconds which is the timeout for waiting for data.
				int timeoutSocket = 10000;
				HttpConnectionParams
						.setSoTimeout(httpParameters, timeoutSocket);
				Log.i("debug","5");
				ResponseHandler<String> responseHandler = new BasicResponseHandler();
				Log.i("debug","6");
				DefaultHttpClient httpClient = new DefaultHttpClient(
						httpParameters);
				Log.i("debug","7");
				try {
					Log.i("code", httpPost.toString());
					
					String httpResponse = httpClient.execute(httpPost,responseHandler);
					Log.i("code", httpResponse);
					//if (httpResponse.getStatusLine().getStatusCode() != 200) {
					//	return null;
					//}
					//HttpEntity httpEntity = httpResponse.getEntity();
					line = "";
				} catch (ConnectTimeoutException e) {
					return "connect timeout";

				}

			} catch (UnsupportedEncodingException e) {
				line = "Can't connect to server1";
			} catch (MalformedURLException e) {
				line = "Can't connect to server2";
			} catch (NoHttpResponseException e)
			{
				 line="4";
				
			}
			catch (IOException e) {
				line = "Can't connect to server3";
			}
			return line;
		}

		@Override
		protected void onPostExecute(String result) {
			super.onPostExecute(result);
		}

	}

}
