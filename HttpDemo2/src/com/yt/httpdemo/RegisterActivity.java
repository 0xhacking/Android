package com.yt.httpdemo;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.NoHttpResponseException;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ConnectTimeoutException;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;

import com.yt.httpdemo.MainActivity.MyTask;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class RegisterActivity extends Activity {

	private EditText mNameText = null;
	private EditText mPasswordText = null;
	private EditText mMobileText = null;
	private EditText mEmailText = null;
	private EditText mBankIdText = null;
	private EditText mBankCodeText = null;
	private EditText mTaskTypeText = null;
	private Button  mRegisterButton = null;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_register);
		
      mRegisterButton = (Button) findViewById(R.id.register_submit_get);
		
		mRegisterButton.setOnClickListener(mGetClickListener);
	}
	private OnClickListener mGetClickListener = new View.OnClickListener() {

		@Override
		public void onClick(View v) {

			String mt = null;
			try {
				Log.i("mt", "a");
				mt = new MyTask().execute("http://192.168.1.154/bootstrap.php")
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

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.register, menu);
		return true;
	}
	
	class MyTask extends AsyncTask<String, Integer, String> {

		@Override
		protected String doInBackground(String... params) {
			String line=null;
			try {
				 mNameText = (EditText) findViewById(R.id.register_username);
			     mPasswordText = (EditText) findViewById(R.id.register_password);
			     mMobileText = (EditText) findViewById(R.id.register_mobile);
			     mEmailText = (EditText) findViewById(R.id.register_email);

			     mBankIdText = (EditText) findViewById(R.id.register_bank_id);
			     mBankCodeText = (EditText) findViewById(R.id.register_bank_code);

			     mTaskTypeText = (EditText) findViewById(R.id.register_task_type);
	

					String name = mNameText.getText().toString();
					String password = mPasswordText.getText().toString();
					
					String mobile = mMobileText.getText().toString();
					String email = mEmailText.getText().toString();
					
					String bank_id = mBankIdText.getText().toString();
					String bank_code = mBankCodeText.getText().toString();
					
					String task_type = mTaskTypeText.getText().toString();
					//String password = mPasswordText.getText().toString();
	            NameValuePair pair1 = new BasicNameValuePair("login", name);
	            NameValuePair pair2 = new BasicNameValuePair("pwd", password);
	            NameValuePair pair3 = new BasicNameValuePair("mobile", mobile);
	            NameValuePair pair4 = new BasicNameValuePair("email", email);
	            NameValuePair pair5 = new BasicNameValuePair("bank_id", bank_id);
	            NameValuePair pair6 = new BasicNameValuePair("bank_code", bank_code);
	            NameValuePair pair7 = new BasicNameValuePair("kind", task_type);
	            //NameValuePair pair2 = new BasicNameValuePair("pwd", password);
	            
	            NameValuePair pair8 = new BasicNameValuePair("android_login", "1");
	            
	            Log.i("mt",name);
			
	            List<NameValuePair> pairList = new ArrayList<NameValuePair>();
	            pairList.add(pair1);
	            pairList.add(pair2);
	            pairList.add(pair3);
	            pairList.add(pair4);
	            pairList.add(pair5);
	            pairList.add(pair6);
	            pairList.add(pair7);
	            pairList.add(pair8);
	           
	            

				Log.i("http", params[0]);
                HttpEntity requestHttpEntity = new UrlEncodedFormEntity(
                        pairList,"UTF-8");

				HttpPost httpPost = new HttpPost(params[0]);
				
				// 将请求体内容加入请求中
				httpPost.setEntity(requestHttpEntity);
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
					
					HttpResponse httpResponse = httpClient.execute(httpPost);
					
					line=EntityUtils.toString(httpResponse.getEntity(),"UTF-8");
							
					
					//if (httpResponse.getStatusLine().getStatusCode() != 200) {
					//	return null;
					//}
					//HttpEntity httpEntity = httpResponse.getEntity();
				
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
			return line ;
		}
		
		@Override
		protected void onPostExecute(String result) {
			 //ProgressBar update = (ProgressBar)UpdateDialog.findViewById(R.id.horizontalProgressBar);
           Log.i("mt","wt"+result);
           //new_view = (TextView) findViewById(R.id.new_view);
           //createNotification();
           Intent intent = new Intent(RegisterActivity.this, HomeActivity.class);
           
			if(result!=null)
			{
				
				
				SharedPreferences sharedPref =RegisterActivity.this.getPreferences(Context.MODE_PRIVATE);
				SharedPreferences.Editor editor = sharedPref.edit();
			    
				editor.putString(getString(R.string.save_data), result);
				editor.commit();
				SharedPreferences sharedPref2 = RegisterActivity.this.getPreferences(Context.MODE_PRIVATE);
				String defaultValue = getResources().getString(R.string.save_data_default);
				String highScore = sharedPref2.getString(getString(R.string.save_data), defaultValue);
				Log.i("msg", highScore);
				
		
				
				
			}
			else	
			{
			  Log.i("error","111");
					
				
			}
			super.onPostExecute(result);
			
		}

	}

}
